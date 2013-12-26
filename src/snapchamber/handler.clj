(ns snapchamber.handler
  (:require [compojure.core :refer [defroutes]]
            [snapchamber.routes.home :refer [home-routes]]
            [snapchamber.routes.api  :refer [api-routes]]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.schedule.simple
             :refer [schedule
                     repeat-forever
                     with-interval-in-minutes]]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]
            [snapchamber.db :as db]))


;; playing with quartzite
(j/defjob CleanupSnaps [ctx]
  (db/cleanup-snaps!))

(def job
  (j/build
    (j/of-type CleanupSnaps)
    (j/with-identity (j/key "jobs.cleanup-snaps.1"))))

(def trigger
  (t/build
    (t/with-identity (t/key "triggers.1"))
    (t/start-now)
    (t/with-schedule (schedule
                       (repeat-forever)
                       (with-interval-in-minutes 15)))))


;; ;; ;; ;;

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))


(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/append})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "snapchamber.log" :max-size (* 512 1024) :backlog 10})

  (if (env :selmer-dev) (parser/cache-off!))
  (timbre/info "snapchamber started successfully")

  ;; Quartzite scheduler
  (qs/initialize)
  (qs/start)
  (qs/schedule job trigger))


(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  (timbre/info "snapchamber is shutting down..."))


(defn template-error-page [handler]
  (if (env :selmer-dev)
    (fn [request]
      (try
        (handler request)
        (catch clojure.lang.ExceptionInfo ex
          (let [{:keys [type error-template] :as data} (ex-data ex)]
            (if (= :selmer-validation-error type)
              {:status 500
               :body (parser/render error-template data)}
              (throw ex))))))
    handler))


(def app (middleware/app-handler
           ;; add your application routes here
           [home-routes api-routes app-routes]
           ;; add custom middleware here
           :middleware [template-error-page]
           ;; add access rules here
           :access-rules []
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))

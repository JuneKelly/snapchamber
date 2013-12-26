(ns snapchamber.routes.api
  (:use compojure.core)
  (:require [liberator.core
             :refer [defresource
                     request-method-in]]
            [snapchamber.util :as util]
            [snapchamber.db :as db]))


(defn save-snap
  "Generate an id for the new snap and save it to database,
   returning the new snap id"
  [context]
  (let [params (get-in context [:request :params])
        snap-id (util/short-id)]
    (do
      (db/create-snap snap-id
                      (params :imageData))
      {:snap-id snap-id})))


;; needed to specify the representation of response
(def rep-map
  {:representation {:media-type "application/json"}})


(defn post-malformed?
  [params]
  (if (empty? (:imageData params))
    [true, (merge rep-map {:snap-error "imageData required"})]
    [false, rep-map]))


(defn snap-request-malformed? [context]
  (let [method (get-in context [:request :request-method])
        params (get-in context [:request :params])]
    (if (= method :get)
      [false, rep-map] ;; presume we're fine with GET
      (if (= method :post)
        (post-malformed? params)))))


(defresource snap [snap-id]
  :service-available? true
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get :post)

  :handle-ok
  (fn [_]
    (let [snap (db/get-snap snap-id)]
      {:snapId snap-id
       :imageData (:imageData snap)}))

  :malformed?
  snap-request-malformed?

  :handle-malformed
  (fn [context]
    {:error (:snap-error context)})

  :post! save-snap
  :handle-created (fn [context] {:snapId (context :snap-id)}))


(defroutes api-routes
  (POST "/api/snap" [] snap)
  (GET "/api/snap/:snap-id" [snap-id] (snap snap-id)))

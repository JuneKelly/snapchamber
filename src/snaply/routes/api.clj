(ns snaply.routes.api
  (:use compojure.core)
  (:require [liberator.core
             :refer [defresource
                     request-method-in]]))


(defn create-snap
  [context]
  (println (get-in context [:request :params])))


(defn get-snap-id
  [context]
  (comment "todo"))


(defresource snap
  :service-available? true
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :get :post)

  :malformed?
  (fn [context]
    (let [params (get-in context [:request :params])]
      (empty? (params :imageData))))

  :handle-malformed
  (fn [_]
    (str "Error: imageData required"))

  :post! create-snap
  :handle-created (fn [_] {:snapId "asdf"}))


(defroutes api-routes
  (ANY "/api/snap" [] snap))

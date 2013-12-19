(ns snaply.routes.api
  (:use compojure.core)
  (:require [liberator.core
             :refer [defresource
                     request-method-in]]
            [snaply.util :as util]
            [snaply.db :as db]))


(defn save-snap
  [context]
  (let [params (get-in context [:request :params])
        snap-id (util/short-id)]
    (do
      (db/create-snap snap-id
                      (params :imageData))
      {:snap-id snap-id})))


(defresource create-snap
  :service-available? true
  :available-media-types ["application/json"]
  :method-allowed? (request-method-in :post)

  :malformed?
  (fn [context]
    (let [params (get-in context [:request :params])]
      (empty? (params :imageData))))

  :handle-malformed
  (fn [_]
    (str "Error: imageData required"))

  :post! save-snap
  :handle-created (fn [context] {:snapId (context :snap-id)}))


(defroutes api-routes
  (POST "/api/snap" [] create-snap))

(ns snaply.routes.api
  (:use compojure.core)
  (:require [liberator.core
             :refer [defresource
                     request-method-in]]
            [snaply.util :as util]
            [snaply.db :as db]))


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


(defn request-malformed? [context]
  (let [method (get-in context [:request :request-method])
        params (get-in context [:request :params])]
    [(if (= method :get)
      false  ;; presume request is fine if GET method
      (empty? (params :imageData)))
     {:representation {:media-type "application/json"}}]))


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
  request-malformed?

  :handle-malformed
  (fn [_]
    {:error "imageData required"})

  :post! save-snap
  :handle-created (fn [context] {:snapId (context :snap-id)}))


(defroutes api-routes
  (POST "/api/snap" [] snap)
  (GET "/api/snap/:snap-id" [snap-id] (snap snap-id)))

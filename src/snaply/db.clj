(ns snaply.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [snaply.util :refer [datetime]]))


(mg/connect-via-uri! "mongodb://localhost/snaply")


(defn create-snap [id image-data]
  (let [doc {:_id id
             :imageData image-data
             :created (datetime)}]
    (do
      (mc/insert "snap" doc))))


(defn get-snap [snap-id]
  (let [doc (mc/find-one-as-map "snap" {:_id snap-id})]
    doc))

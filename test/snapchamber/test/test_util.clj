(ns snapchamber.test.test-util
  (:require [snapchamber.db :as db]
            [environ.core :refer [env]]
            [monger.core :as mg]
            [monger.db :as md]
            [monger.collection :as mc]
            [monger.query :as mq]
            monger.joda-time))

;; database operations
(mg/connect-via-uri! (env :db-uri))


(defn drop-database! []
  (md/drop-db (mg/get-db "snapchamber")))


(defn populate-snaps! []
  (do
    (db/create-snap
      "123" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAB")
    (db/create-snap
      "456" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAC")
    (db/create-snap
      "789" "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAD")))

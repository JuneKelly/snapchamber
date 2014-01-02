(ns snapchamber.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            monger.joda-time
            [snapchamber.util :refer [datetime]]
            [environ.core :refer [env]]
            [noir.util.crypt :refer [md5]]
            [clj-time.core :refer [now minus hours]]))


(mg/connect-via-uri! (env :db-uri))

;; Snaps

;; Create
(defn create-snap [id image-data]
  (let [doc {:_id id
             :imageData image-data
             :_md5 (md5 image-data)
             :created (datetime)}]
    (do
      (mc/insert "snap" doc))))


;; Find
(defn get-snap [snap-id]
  (let [doc (mc/find-one-as-map "snap" {:_id snap-id})]
    doc))


(defn get-snap-by-md5 [image-hash]
  (let [doc (mc/find-one-as-map "snap" {:_md5 image-hash})]
    doc))


;; Check existence
(defn snap-exists? [snap-id]
  (not
    (nil? (get-snap snap-id))))


(defn image-hash-exists? [image-hash]
  (not
    (nil? (get-snap-by-md5 image-hash))))


;; Delete
(defn remove-snaps [snap-ids]
  (mc/remove "snap" {:_id {:$in snap-ids}}))


;; Cleanup of old snaps
(defn get-old-snap-ids [cut-off-datetime]
  (let [old (mc/find-maps "snap"
                          {:created {:$lt cut-off-datetime}}
                          [:_id])]
    (map #(get %1 :_id) old)))


(defn cleanup-snaps! []
  (let [cutoff (minus (now) (hours 6))
        snap-ids (get-old-snap-ids cutoff)]
    (do
      (println ">> Snaps to delete: " snap-ids)
      (remove-snaps snap-ids))))


;; Stats

(defn stats-snap-created []
  (mc/update "scStats"
             {:_id "snap"}
             {:$inc {:snapsCreated 1.0}}
             :upsert true))


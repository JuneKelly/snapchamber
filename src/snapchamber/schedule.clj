(ns snapchamber.schedule
  (:require [snapchamber.db :as db]
            [clojurewerkz.quartzite.scheduler :as qs]
            [clojurewerkz.quartzite.schedule.simple
             :refer [schedule
                     repeat-forever
                     with-interval-in-minutes]]
            [clojurewerkz.quartzite.jobs :as j]
            [clojurewerkz.quartzite.triggers :as t]))


(j/defjob CleanupSnaps [ctx]
  (db/cleanup-snaps!))


(def cleanup-job
  (j/build
    (j/of-type CleanupSnaps)
    (j/with-identity (j/key "jobs.cleanup-snaps.1"))))


(def cleanup-trigger
  (t/build
    (t/with-identity (t/key "triggers.1"))
    (t/start-now)
    (t/with-schedule (schedule
                       (repeat-forever)
                       (with-interval-in-minutes 15)))))


(defn start-scheduled-tasks! []
  (do
    (qs/initialize)
    (qs/start)
    (qs/schedule cleanup-job
                 cleanup-trigger)))

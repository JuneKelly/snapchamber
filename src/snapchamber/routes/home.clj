(ns snapchamber.routes.home
  (:use compojure.core)
  (:require [snapchamber.views.layout :as layout]
            [snapchamber.util :as util]))


(defn home-page []
  (layout/render
    "base.html"))


(defroutes home-routes
  (GET "/" [] (home-page)))

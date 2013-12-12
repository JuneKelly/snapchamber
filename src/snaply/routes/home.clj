(ns snaply.routes.home
  (:use compojure.core)
  (:require [snaply.views.layout :as layout]
            [snaply.util :as util]))


(defn home-page []
  (layout/render
    "base.html"))


(defroutes home-routes
  (GET "/" [] (home-page)))

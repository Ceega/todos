(ns clojurescript-start-for-browser.core
  (:require [reagent.dom :as rdom]))

(defn main-view []
  [:h1 "Hello world!"])

(defn start {:dev/after-load true} []
  (rdom/render [main-view] (js/document.getElementById "root")))

(defn init []
  (start))

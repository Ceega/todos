(ns todos.core
  (:require [reagent.dom :as rdom]
            [todos.views :as views]))

(defn start {:dev/after-load true} []
  (rdom/render [views/main-view] (js/document.getElementById "root")))

(defn init []
  (start))

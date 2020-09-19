(ns todos.views
  (:require [re-frame.core :as rf]
            [todos.events]
            [todos.subs]))

(defn main-view []
  [:h1 "Hello world!"])

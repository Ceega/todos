(ns todos.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :new-todo
 (fn [db _]
   (:new-todo db)))

(rf/reg-sub
 :todos
 (fn [db _]
   (:todos db)))

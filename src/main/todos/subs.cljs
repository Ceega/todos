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

(rf/reg-sub
 :filtering-mode
 (fn [db _]
   (-> db :filtering-mode (or :all))))

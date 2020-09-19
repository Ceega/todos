(ns todos.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :set-new-todo
 (fn [db [_ new-todo]]
   (assoc db :new-todo new-todo)))

(rf/reg-event-db
 :add-todo
 (fn [db [_ new-todo clear-new-todo?]]
   (cond-> db
     true
     (update :todos #(conj (or % []) {:v new-todo
                                      :completed? false}))

     clear-new-todo?
     (dissoc :new-todo))))

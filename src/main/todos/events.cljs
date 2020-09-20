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

(rf/reg-event-db
 :delete-todo
 (fn [db [_ idx]]
   (update db :todos #(vec (concat (subvec % 0 idx)
                                   (subvec % (inc idx)))))))

(rf/reg-event-db
 :toggle-todo-completed
 (fn [db [_ idx]]
   (update-in db [:todos idx :completed?] not)))

(rf/reg-event-db
 :set-filtering-mode
 (fn [db [_ k]]
   (assoc db :filtering-mode k)))

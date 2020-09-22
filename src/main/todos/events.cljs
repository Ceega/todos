(ns todos.events
  (:require [clojure.edn :as edn]
            [re-frame.core :as rf]))

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
 :set-edited-todo
 (fn [db [_ k]]
   (assoc db :edited-todo k)))

(rf/reg-event-db
 :edit-todo
 (fn [db [_ idx v]]
   (assoc-in db [:todos idx :v] v)))

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

(rf/reg-event-db
 :clear-completed
 (fn [db _]
   (update db :todos #(vec (remove :completed? %)))))

;;
;; LocalStorage support
;;

(def db-to-local-storage
  (rf/->interceptor
   :id :db-to-local-storage
   :after (fn [ctx]
            (assoc-in ctx
                      [:effects :set-local-storage]
                      {:k "db"
                       :v (-> ctx :effects :db)}))))

(rf/reg-global-interceptor db-to-local-storage)

(rf/reg-fx
 :set-local-storage
 (fn [{:keys [k v]}]
   (.setItem (.-localStorage js/window) (str k) (str v))))

(rf/reg-cofx
 :local-storage
 (fn [coeffects k]
   (assoc coeffects
          :local-storage
          (.getItem (.-localStorage js/window) k))))

(rf/reg-event-fx
 :load-local-storage
 [(rf/inject-cofx :local-storage "db")]
 (fn [{:keys [db local-storage]}]
   {:db (edn/read-string local-storage)}))

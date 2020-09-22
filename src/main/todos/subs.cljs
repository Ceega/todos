(ns todos.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :db
 (fn [db _]
   (update db :filtering-mode #(or % :all))))

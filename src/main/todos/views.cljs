(ns todos.views
  (:require [re-frame.core :as rf]
            [todos.events]
            [todos.subs]))

(defn text-input-view [{:keys [class v on-change on-enter placeholder]}]
  [:input
   {:type "text"
    :class class
    :value v
    :on-change #(when on-change
                  (on-change (-> % .-target .-value)))
    :on-key-down #(when (and on-enter (= (.-keyCode %) 13))
                    (on-enter v))
    :placeholder placeholder
    :autoFocus true}])

(defn controls-view [{:keys [new-todo]}]
  [:div.controls
   [text-input-view {:class "controls__new-todo"
                     :v new-todo
                     :on-change #(rf/dispatch [:set-new-todo %])
                     :on-enter #(rf/dispatch [:add-todo % true])
                     :placeholder "What needs to be done?"}]])

(defn main-view []
  (let [new-todo @(rf/subscribe [:new-todo])]
    [:div.main
     [:div.header
      [:h1.header__text "todos"]]
     [controls-view {:new-todo new-todo}]]))

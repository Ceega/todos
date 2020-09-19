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

(defn top-controls-view [{:keys [new-todo]}]
  [:div.top-controls
   [text-input-view {:class "top-controls__new-todo"
                     :v new-todo
                     :on-change #(rf/dispatch [:set-new-todo %])
                     :on-enter #(rf/dispatch [:add-todo % true])
                     :placeholder "What needs to be done?"}]])

(defn todo-view [{:keys [v completed?]}]
  [:li.todo v])

(defn todos-list-view [{:keys [todos]}]
  [:ul.todos-list
   (for [[idx todo] (map-indexed vector todos)]
     [todo-view (assoc todo :key idx)])])

(defn main-view []
  (let [new-todo @(rf/subscribe [:new-todo])
        todos @(rf/subscribe [:todos])]
    [:div.main
     [:div.header
      [:h1.header__text "todos"]]
     [:div.controls-and-todos
      [top-controls-view {:new-todo new-todo}]
      (when-not (empty? todos)
        [todos-list-view {:todos todos}])]
     [:div.footer
      [:p.footer__row "Double click to edit a todo"]
      [:p.footer__row "Look inspired by TodoMVC,
                       but not part of the official project"]]]))

(ns todos.views
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [todos.events]
            [todos.subs]))

(defn text-input-view [{:keys [class v on-change on-enter placeholder]}]
  [:input
   {:type "text"
    :class class
    :value v
    :on-change #(when on-change
                  (on-change (-> % .-target .-value)))
    :on-key-down #(when (and on-enter
                           (= (.-keyCode %) 13)
                           (-> v str/blank? not))
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

(defn todo-view [{:keys [key v completed?]}]
  [:li.todo
   [:label.todo__label v]
   [:button.todo__delete-button
    {:on-click #(rf/dispatch [:delete-todo key])}
    "✕"]])

(defn todos-list-view [{:keys [todos]}]
  [:ul.todos-list
   (for [[idx todo] (map-indexed vector todos)]
     [todo-view (assoc todo :key idx)])])

(defn bottom-controls-view [{:keys [todos]}]
  (let [todo-count (->> todos
                        (filter #(-> % :completed? false?))
                        count)]
    [:div.bottom-controls
     [:span.bottom-controls__count (str todo-count
                                        (if (= todo-count 1) " item" " items")
                                        " left")]]))

(defn main-view []
  (let [new-todo @(rf/subscribe [:new-todo])
        todos @(rf/subscribe [:todos])]
    [:div.main
     [:div.header
      [:h1.header__text "todos"]]
     [:div.controls-and-todos
      [top-controls-view {:new-todo new-todo}]
      (when-not (empty? todos)
        [todos-list-view {:todos todos}])
      (when-not (empty? todos)
        [bottom-controls-view {:todos todos}])]
     [:div.footer
      [:p.footer__row "Double click to edit a todo"]
      [:p.footer__row "Look inspired by TodoMVC,
                       but not part of the official project"]]]))

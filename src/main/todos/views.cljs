(ns todos.views
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [todos.events]
            [todos.subs]))

(defn text-input-view [{:keys [class v on-change on-enter on-blur placeholder]}]
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
    :on-blur (when on-blur on-blur)
    :placeholder "What needs to be done?"
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
   [:input.todo__checkbox
    {:type "checkbox"
     :checked completed?
     :on-change #(rf/dispatch [:toggle-todo-completed key])}]
   [:label.todo__label
    {:class (when completed? "todo__label--completed")
     :on-double-click #(rf/dispatch [:set-edited-todo key])}
    v]
   [:button.todo__delete-button
    {:on-click #(rf/dispatch [:delete-todo key])}
    "✕"]])

(defn edited-todo-view [{:keys [key v]}]
  [:li.todo
   [text-input-view
    {:class "todo__edit-input"
     :v v
     :on-change #(rf/dispatch [:edit-todo key %])
     :on-enter #(rf/dispatch [:set-edited-todo nil])
     :on-blur #(rf/dispatch [:set-edited-todo nil])}]])

(defn visible-todo? [{:keys [completed?]} filtering-mode]
  (or (= filtering-mode :all)
      (and (= filtering-mode :active) (not completed?))
      (and (= filtering-mode :completed) completed?)))

(defn todos-list-view [{:keys [todos filtering-mode edited-todo]}]
  [:ul.todos-list
   (for [[idx todo] (map-indexed vector todos)
         :let [todo (assoc todo :key idx)]
         :when (visible-todo? todo filtering-mode)]
     (if (= idx edited-todo)
       [edited-todo-view todo]
       [todo-view todo]))])

(defn bottom-controls-view [{:keys [todos filtering-mode]}]
  (let [todo-count (->> todos
                        (filter #(-> % :completed? false?))
                        count)]
    [:div.bottom-controls
     [:span.bottom-controls__count (str todo-count
                                        (if (= todo-count 1) " item" " items")
                                        " left")]
     [:div.bottom-controls-filtering-buttons
      (for [k [:all :active :completed]]
        [:button.bottom-controls-filtering-buttons__button
         {:key k
          :class (when (= k filtering-mode)
                   "bottom-controls-filtering-buttons__button--active")
          :on-click #(rf/dispatch [:set-filtering-mode k])}
         (-> k name str/capitalize)])]
     [:button.bottom-controls__clear-button
      {:on-click #(rf/dispatch [:clear-completed])}
      "Clear completed"]]))

(defn main-view* []
  (let [db @(rf/subscribe [:db])
        empty-todos? (-> db :todos empty?)]
    [:div.main
     [:div.header
      [:h1.header__text "todos"]]
     [:div.controls-and-todos
      [top-controls-view db]
      (when-not empty-todos?
        [todos-list-view db])
      (when-not empty-todos?
        [bottom-controls-view db])]
     [:div.footer
      [:p.footer__row "Double click to edit a todo"]
      [:p.footer__row "Look inspired by TodoMVC,
                       but not part of the official project"]]]))

(defn main-view []
  (r/create-class
   {:component-did-mount
    #(rf/dispatch [:load-local-storage])
    :reagent-render
    main-view*}))

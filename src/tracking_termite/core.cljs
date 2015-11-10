(ns tracking-termite.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(enable-console-print!)

(defonce app-state (atom {:user {:name "Nils"
                                 :image "http://photos.gograph.com/thumbs/CSP/CSP769/k7698832.jpg"
                                 :age 32}}))

(defn read [{:keys [state] :as env} key params]
  (let [st @state]
    (if-let [[_ value] (find st key)]
      {:value value}
      {:value :not-found})))

(defui UserInfo
  static om/Ident
  (ident [this {:keys [name]}]
         [:user/by-name name])
  static om/IQuery
  (query [this]
         '[:name :image :age])
  Object
  (render [this]
          (println (om/props this))
          (let [{:keys [name image age]} (om/props this)]
            (dom/div nil
                     (dom/img #js {:src image} nil)
                     (dom/h2 nil name)
                     (dom/label nil age)
                     ))))

(def user-info (om/factory UserInfo))

(defui RootView
  static om/IQuery
  (query [this]
         [:user])
  Object
  (render [this]
          (let [user (:user (om/props this))]
            (dom/div #js {:className "row"}
                     [(dom/div #js {:className "col-md-3"} "foo")
                      (dom/div #js {:className "col-md-2"}
                               (user-info user))]))))

(def reconciler
  (om/reconciler {:state app-state
                  :parser (om/parser {:read read})}))

(om/add-root! reconciler RootView (gdom/getElement "app"))

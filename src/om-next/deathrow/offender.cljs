(ns deathrow.offender
  (:require [deathrow.common :as c]
            [deathrow.utils :as utils]
            [deathrow.parser :as p]
            [goog.object :as gobj]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(defui LastQuote
  static om/IQuery
  (query [this]
    [:firstName :lastName :lastStmt :profileUrl :dateExecuted])
  Object
  (render [this]
    (when-let [{:keys [lastStmt profileUrl dateExecuted firstName lastName]} (om/props this)]
      (dom/blockquote nil
        (dom/p nil (utils/normalize-string lastStmt))
        (dom/footer nil
          (dom/cite #js{:title (utils/display-name firstName lastName)}
            (dom/a #js{:href profileUrl}
              (utils/display-name firstName lastName)))
          (str ", executed " dateExecuted))))))

(def last-quote
  (om/factory LastQuote))

(defui Offender
  static om/IQuery
  (query [this]
    (into [{:data (om/get-query LastQuote)}]
          (om/get-query c/GenericPanel)))
  Object
  (componentWillMount [this]
    (utils/highlight-nav 1))
  (render [this]
    (let [{:keys [data] :as props} (om/props this)]
      (c/generic-panel props
        (c/panel-body nil
          (last-quote data))))))

(-> Offender (utils/set-display-name "Offender"))

(def reconciler)

(defn root
  ([] (root "/offenders/random"))
  ([path]
   (let [rec (om/reconciler {:state (c/build-app-state {:path path})
                             :parser (om/parser {:read p/read})
                             :send (utils/get-ajax-next path)
                             :remotes [:remote]})]
     (set! reconciler rec)
     (om/add-root! reconciler Offender c/app-element))))

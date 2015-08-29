(ns deathrow.offender
  (:require
    [om.core :as om]
    [om.dom :as dom]
    [deathrow.common :as c]
    [deathrow.utils :as utils]
    [goog.string :as gstr]))

(def view-state (atom {:path nil
                       :offender nil}))

(defn on-receive-offender
  [state owner offender]
  (om/update-state! owner (fn [s] (assoc s :error false :loading false)))
  (om/transact! state :offender
    #(js->clj offender :keywordize-keys true)))

(defn last-quote-view
  [offender owner]
  (reify
    om/IRender
    (render [_]
      (dom/blockquote nil
        ;; TODO: is this something we want to be doing here or in e.g. IWillUpdate?
        (dom/p nil (gstr/unescapeEntities (:lastStmt offender)))
        (dom/footer nil
          (dom/cite #js{:title (utils/display-name offender)}
            (dom/a #js{:href (:profileUrl offender)}
              (utils/display-name offender)))
          (str ", executed " (:dateExecuted offender)))))))

(defn offender-view
  [data owner]
  (reify
    om/IInitState
    (init-state [_]
      {:loading false
        :error false})
    om/IWillMount
    (will-mount [_]
      (om/set-state! owner :loading true)
      (utils/get-ajax (:path data)
                      {:success #(on-receive-offender data owner %)
                        :error
                        #(om/update-state! owner
                          (fn [s] (assoc s :error true :loading false)))}))
    om/IRenderState
    (render-state [_ {:keys [loading error]}]
      (om/build c/generic-panel (if error {} (:offender data))
        {:opts
          {:view (cond
                  loading c/spinner
                  error c/error-msg
                  :else last-quote-view)}}))))

(defn root
  ([] (root "/offenders/random"))
  ([path]
    (om/root offender-view (swap! view-state assoc :path path)
      {:target (. js/document (getElementById "content"))})))

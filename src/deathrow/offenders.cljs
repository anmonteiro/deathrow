(ns deathrow.offenders
  (:require [deathrow.utils :as utils]
            [deathrow.common :as c]
            [goog.string :as gstr]
            [om.core :as om]
            [om.dom :as dom]))

(defn pager
  [state owner]
  (om/component
    (let [{:keys [prev next]} state]
      (dom/ul #js {:className "pager"}
        (dom/li #js {:className (str "previous" (when (zero? prev) " disabled"))}
          (let [text (gstr/unescapeEntities "&larr; Previous")]
            (if (pos? prev)
              (dom/a #js {:href (str "/offenders/page/" prev)} text)
              (dom/span nil text))))
        (dom/li #js {:className "next"}
          (dom/a #js {:href (str "/offenders/page/" next)} (gstr/unescapeEntities "Next &rarr;")))))))

(defn offender-row
  [offender owner]
  (om/component
    (dom/tr nil
      (dom/td nil (:executionNo offender))
      (dom/td nil
        (dom/a #js{:href (str "/offenders/" (:executionNo offender))}))
      (dom/td nil (utils/display-name offender))
      (dom/td nil (:race offender))
      (dom/td nil (:age offender)))))

(defn offenders-table
  [state owner]
  (om/component
    (let [paging (:paging state)
          pager (om/build pager paging)]
      (dom/div #js{:className "panel panel-default"}
        (dom/div #js{:className "panel-heading"} pager)
        (dom/table #js{:className "table table-hover"}
          (dom/thead nil
            (dom/tr nil
              (dom/th nil "Execution #")
              (dom/th nil "Name")
              (dom/th nil "Race")
              (dom/th nil "Age")))
          (dom/tbody nil
            (om/build-all offender-row (:data state))))
        (dom/div #js{:className "panel-footer text-center"} pager)))))

(defn offenders-component
  [state owner]
  (reify
    om/IInitState
    (init-state [_]
      {:error false})
    om/IWillMount
    (will-mount [_]
      (utils/highlight-nav 2)
      (utils/get-ajax (:path state)
                      {:success
                      #(om/transact! state
                        (fn [st]
                          (merge st (js->clj % :keywordize-keys true))))
                        :error #(om/set-state! owner :error true)}))
    om/IRenderState
    (render-state [_ {:keys [error]}]
      (cond
        error (om/build c/generic-panel {} {:opts {:view c/error-msg}})
        :else (om/build offenders-table state)))))

(defn root
  [path]
  (om/root offenders-component {:path path}
    {:target (. js/document (getElementById "content"))}))

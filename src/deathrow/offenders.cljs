(ns deathrow.offenders
  (:require [deathrow.common :as c]
            [deathrow.utils :as utils]
            [goog.string :as gstr]
            [om.core :as om]
            [om.dom :as dom]))

(defn on-receive-data [state owner data]
  (om/transact! state
                #(merge % (js->clj data :keywordize-keys true))))

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
        (dom/a #js{:href (str "/offenders/" (:executionNo offender))}
               (utils/display-name offender)))
      (dom/td nil (:race offender))
      (dom/td nil (:age offender)))))

(defn offenders-partial
  [cl-name state owner]
  (om/component
    (dom/div #js{:className cl-name}
      (om/build pager (:paging state)))))

(defn offenders-table
  [state owner]
  (om/component
    (dom/table #js{:className "table table-hover"}
      (dom/thead nil
        (dom/tr nil
          (dom/th nil "Execution #")
          (dom/th nil "Name")
          (dom/th nil "Race")
          (dom/th nil "Age")))
      (dom/tbody nil
        (om/build-all offender-row (:data state))))))

(defn offenders-component
  [state owner]
  (om/component
    (om/build c/generic-panel
              state
              {:opts {:view offenders-table
                      :heading (partial offenders-partial "panel-heading")
                      :footer (partial offenders-partial "panel-footer")}})))

(defn- build-app-state []
  (merge @c/app-state {:nav-pos 2
                        :on-success on-receive-data}))

(defn root [path]
  (om/root offenders-component (assoc (build-app-state) :path path)
           {:target c/app-element}))

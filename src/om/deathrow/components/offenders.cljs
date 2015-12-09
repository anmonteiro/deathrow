(ns deathrow.components.offenders
  (:require [deathrow.components.common :as c]
            [deathrow.utils :as utils]
            [goog.string :as gstr]
            [om.core :as om]
            [om.dom :as dom]))

(defn pager
  [state owner]
  (om/component
    (let [{:keys [prev next]} (:paging state)
          loading (:loading state)]
      (dom/ul #js {:className "pager"}
        (dom/li #js {:className (str "previous" (when (or (zero? prev) loading) " disabled"))}
          (let [text (gstr/unescapeEntities "&larr; Previous")]
            (if (and (pos? prev) (not loading))
              (dom/a #js {:href (str "/offenders/page/" prev)} text)
              (dom/span nil text))))
        (dom/li #js {:className (str "next" (when loading " disabled"))}
          (let [text (gstr/unescapeEntities "Next &rarr;")]
            (if (not loading)
              (dom/a #js {:href (str "/offenders/page/" next)} text)
              (dom/span nil text))))))))

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
      (om/build pager state))))

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
        (om/build-all offender-row (:data state) {:key :executionNo})))))

(defn offenders-component
  [state owner]
  (om/component
   (om/build c/generic-panel
             state
             {:opts {:view offenders-table
                     :heading (partial offenders-partial "panel-heading")
                     :footer (partial offenders-partial "panel-footer")}})))

(ns deathrow.components.offender
  (:require [deathrow.components.common :as c]
            [deathrow.utils :as utils]
            [om.core :as om]
            [om.dom :as dom]))

;; ToDo: read from upcoming :offenders array in state if cached.
(defn last-quote-view
  [state owner]
  (om/component
    (let [offender (:data state)]
      (dom/blockquote nil
        (dom/p nil (utils/normalize-string (:lastStmt offender)))
        (dom/footer nil
          (dom/cite #js{:title (utils/display-name offender)}
            (dom/a #js{:href (:profileUrl offender)}
              (utils/display-name offender)))
          (str ", executed " (:dateExecuted offender)))))))

(defn offender-component
  [state owner]
  (om/component
    (om/build c/generic-panel state
              {:opts {:view (partial c/panel-body-partial last-quote-view)}})))

(ns deathrow.offender
  (:require [deathrow.common :as c]
            [deathrow.utils :as utils]
            [om.core :as om]
            [om.dom :as dom]))

(defn on-receive-offender
  [state owner data]
  (om/transact! state :data
    #(js->clj data :keywordize-keys true)))

(defn last-quote-view
  [state owner]
  (om/component
    (when-let [offender (:data state)]
      (dom/blockquote nil
        (dom/p nil (utils/normalize-string (:lastStmt offender)))
        (dom/footer nil
          (dom/cite #js{:title (utils/display-name offender)}
            (dom/a #js{:href (:profileUrl offender)}
              (utils/display-name offender)))
          (str ", executed " (:dateExecuted offender)))))))

(defn- build-app-state []
  (merge @c/app-state {:nav-pos 1
                        :on-success on-receive-offender}))

(defn root
  ([] (root "/offenders/random"))
  ([path]
   (om/root c/generic-panel (assoc (build-app-state) :path path)
            {:target c/app-element
             :opts {:view (partial c/panel-body-partial last-quote-view)}})))

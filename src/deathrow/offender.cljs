(ns deathrow.offender
  (:require [om.core :as om]
            [om.dom :as dom]
            [deathrow.common :as c]
            [deathrow.utils :as utils]
            [goog.string :as gstr]))

(defn on-receive-offender
  [state owner data]
  (om/transact! state :data
    #(js->clj data :keywordize-keys true)))

(defn last-quote-view
  [state owner]
  (om/component
    (let [offender (:data state)]
      (dom/blockquote nil
        ;; TODO: is this something we want to be doing here or in e.g. IWillUpdate?
        (dom/p nil (gstr/unescapeEntities (:lastStmt offender)))
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

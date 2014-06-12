(ns deathrow.models.offenders
    (:require [jayq.core :as jayq :refer [$ ajax]]
        [deathrow.constants :as C]
        [deathrow.models.core :as v :refer [render get-ajax]]
        [deathrow.views.main :as m :refer [spinner]]
        [deathrow.views.offenders.random :as t :refer [last-quote error-quote quote-wrapper]]
        [deathrow.views.offenders.main :as z :refer [offenders-table offender-row]]))

(def content-container ($ :.content))

(defn render-content
    [view]
    (render content-container view))

(defn get-random-offender
    [path]
    (let [ajax-timeout (atom 0)]
        (get-ajax path
            {:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render-content (spinner))) 1000))
            :success #(do (js/clearTimeout @ajax-timeout) (render-content (last-quote (js->clj %1 :keywordize-keys true))))
            :error #(do (js/clearTimeout @ajax-timeout) (render-content (error-quote)))})))

(defn get-offenders
    [path]    
    (let [ajax-timeout (atom 0)]
        (get-ajax path
            {:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render-content (spinner))) 1000))
            :success #(do (js/clearTimeout @ajax-timeout) (render-content (offenders-table (js->clj %1 :keywordize-keys true))))
            :error #(do (js/clearTimeout @ajax-timeout) (render-content (error-quote)))})))




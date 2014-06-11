(ns deathrow.models.offenders
    (:require [jayq.core :as jayq :refer [$ ajax]]
        [deathrow.constants :as C]
        [deathrow.models.core :as v :refer [render get-ajax]]
        [deathrow.views.main :as m :refer [spinner]]
        [deathrow.views.offenders.random :as t :refer [last-quote error-quote quote-wrapper]]
        [deathrow.views.offenders.main :as z :refer [offenders-table offender-row]]))

(def content-container ($ :.content))


(def test-offender
    {
    :age 42
    :lastName "Aguilar"
    :lastStmtUrl "http://www.tdcj.state.tx.us/death_row/dr_info/aguilarjesuslast.html"
    :dateExecuted "05/24/2006"
    :executionNo 365
    :firstName "Jesus"
    :race "Hispanic"
    :profileUrl "http://www.tdcj.state.tx.us/death_row/dr_info/aguilarjesus.jpg"
    :_id 999191
    :lastStmt " Aguilar, Jesus Ledesma\n Last Statement:\n Yes sir.  I would like to say to my family, I am alright. (Spanish) Where are you Leo; are you there Leo? (Spanish) Don&apos;t lie man.  Be happy.  Are you happy?  Are you all happy? (Spanish)  "
  })

(defn render-in-quote
    [view]
    (render content-container view))

(defn render-offenders-table
    [offenders]
    (render-in-quote (offenders-table offenders)))

(defn get-random-offender
    [path]
    (let [ajax-timeout (atom 0)]
        (get-ajax path
            {:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render-in-quote (spinner))) 1000))
            :success #(do (js/clearTimeout @ajax-timeout) (render-in-quote (last-quote (js->clj %1 :keywordize-keys true))))
            :error #(do (js/clearTimeout @ajax-timeout) (render-in-quote (error-quote)))})))

(defn get-offenders
    [path]
    (let [ajax-timeout (atom 0)]
        (get-ajax path
            {:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render-in-quote (spinner))) 1000))
            :success #(do (js/clearTimeout @ajax-timeout) (render-offenders-table (:data (js->clj %1 :keywordize-keys true))))
            :error #(do (js/clearTimeout @ajax-timeout) (render-in-quote (error-quote)))})))




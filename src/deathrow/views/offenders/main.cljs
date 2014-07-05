(ns deathrow.views.offenders.main
  (:use-macros [crate.def-macros :only [defpartial defelem defhtml]])
  (:require [crate.core :as crate]
            [crate.compiler :as compiler]
            [deathrow.constants :as C]))

(defpartial offender-row
  [offender]
  [:tr
   [:td (:executionNo offender)]
   [:td [:a {:href (str "/offenders/" (:executionNo offender))}
         (str (:firstName offender) " " (:lastName offender))]]
   [:td (:race offender)]
   [:td (:age offender)]])

(defpartial pager
  [prev next]
  [:ul.pager
   [:li {:class (str "previous" (when (= prev 0) " disabled"))}
    [:a {:href (when (not (zero? prev)) (str "/offenders/page/" prev))} (crate/raw "&larr; Previous")]]
   [:li.next
    [:a {:href (str "/offenders/page/" next)} (crate/raw "Next &rarr;")]]])

;; this one is not used
(defpartial pagination
  [prev next]
  [:ul.pagination
   [:li {:class (when (= prev 0) "disabled")}
    [:a {:href C/HIST-SEPARATOR} (crate/raw "&larr;")]]
   [:li.active
    [:a {:href C/HIST-SEPARATOR} "1"]]
   [:li
    [:a {:href C/HIST-SEPARATOR} "2"]]
   [:li
    [:a {:href C/HIST-SEPARATOR} "3"]]
   [:li
    [:a {:href C/HIST-SEPARATOR} "4"]]
   [:li
    [:a {:href C/HIST-SEPARATOR} "5"]]
   [:li
    [:a {:href C/HIST-SEPARATOR} (crate/raw "&rarr;")]]])


(defpartial offenders-table
  [response]
  (let [data (:data response)
        paging (:paging response)]
    [:div.panel.panel-default
     [:div.panel-heading (pager (:prev paging) (:next paging))]
     [:table.table.table-hover
      [:thead
			  		[:tr
			  			[:th "Execution #"]
			  			[:th "Name"]
			  			[:th "Race"]
			  			[:th "Age"]]]
			  	[:tbody (for [offender data] (offender-row offender))]]
     [:div.panel-footer.text-center (pager (:prev paging) (:next paging))]]))


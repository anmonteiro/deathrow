(ns deathrow.views
	(:use-macros [crate.def-macros :only [defpartial]])
	(:require [crate.core :as crate]))

(defpartial last-quote
	[offender]
	[:blockquote
		[:p (crate/raw (:lastStmt offender))]
		[:footer
			[:cite {:title (str (:firstName offender) " " (:lastName offender))}
				[:a {:href (:profileUrl offender)} (str (:firstName offender) " " (:lastName offender))]]
				(str ", executed " (:dateExecuted offender))]])


(defpartial error-quote []
	[:h2 "There was an error loading your request. "
		[:small "Try again!"]])

(defpartial spinner []
    [:div#circularG.center-block.spinner.fade
        [:div#circularG_1.circularG]
        [:div#circularG_2.circularG]
        [:div#circularG_3.circularG]
        [:div#circularG_4.circularG]
        [:div#circularG_5.circularG]
        [:div#circularG_6.circularG]
        [:div#circularG_7.circularG]
        [:div#circularG_8.circularG]])
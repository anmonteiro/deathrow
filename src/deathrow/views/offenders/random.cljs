(ns deathrow.views.offenders.random
	(:use-macros [crate.def-macros :only [defpartial defelem defhtml]])
	(:require [crate.core :as crate]
		[crate.compiler :as compiler]
		[deathrow.util :refer [log]]
		))

;; How to make this return two elements?
;; Shouldn't need to wrap under div.panel-body
(defelem quote-wrapper
	[content]
	[:div.panel.panel-default
		[:div.panel-body
			[:div.row
				[:div.col-md-10.col-md-offset-1.well.quote content]]
			[:div.col-md-4.col-md-offset-4.text-center
				[:a.btn.btn-default.load-statement {:href "/offenders/random"}
					"Load another random statement!"]]]])

(defpartial last-quote
	[offender]
	(quote-wrapper
		[:blockquote
			[:p (crate/raw (:lastStmt offender))]
			[:footer
				[:cite {:title (str (:firstName offender) " " (:lastName offender))}
					[:a {:href (:profileUrl offender)} (str (:firstName offender) " " (:lastName offender))]]
					(str ", executed " (:dateExecuted offender))]]))


(defpartial error-quote
	[]
	(quote-wrapper
			[:h2 "There was an error loading your request. "
				[:small "Try again!"]]))

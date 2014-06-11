(ns deathrow.views.offenders.main
	(:use-macros [crate.def-macros :only [defpartial defelem defhtml]])
	(:require [crate.core :as crate]
		[crate.compiler :as compiler]))

(defpartial offender-row
	[offender]
	[:tr
		[:td (:executionNo offender)]
		[:td (str (:firstName offender) " " (:lastName offender))]
		[:td (:race offender)]
		[:td (:age offender)]])

(defpartial offenders-table
	[offenders]
	[:div.panel.panel-default
		[:div.panel-heading
			[:ul.pager
				[:li.previous.disabled
					[:a {:href "deathrow"} (crate/raw "&larr; Older")]]
				[:li.next
					[:a {:href "deathrow"} (crate/raw "Newer &rarr;")]]]]
		[:table.table.table-hover
			[:thead
		  		[:tr
		  			[:th "#"]
		  			[:th "Name"]
		  			[:th "Race"]
		  			[:th "Age"]]]
		  	[:tbody (for [offender offenders] (offender-row offender))]]
		[:div.panel-footer]])


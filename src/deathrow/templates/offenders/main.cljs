(ns deathrow.templates.offenders.main
	(:use-macros [crate.def-macros :only [defpartial defelem]])
	(:require [crate.core :as crate]))


(defpartial offender-row
	[offender]
	)

(defelem offenders-table
  [offenders]
  [:table (for [offender offenders] (offender-row offender))])

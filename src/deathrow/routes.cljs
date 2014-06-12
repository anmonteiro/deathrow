(ns deathrow.routes
	(:require
		[jayq.core :as jayq :refer [$ ajax]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h :refer [navigate-callback]]
		[deathrow.models.offenders :as offenders]
		[secretary.core :as secretary :include-macros true :refer [defroute]]))

(declare random-path)
(declare offenders-path)

(defroute root-path "/"
	[]
	(secretary/dispatch! (random-path)))

(defroute offenders-path #"(/offenders((/page)?(/\d+)?))"
	[match rest page id]
	(if (or (empty? rest) (not (empty? page)))
		(offenders/get-offenders match)
		(offenders/get-random-offender match)
	))

(defroute random-path "/offenders/random"
	[]
	(offenders/get-random-offender (random-path)))

(defroute error-path "*"
	[]
	(log "ERROR: NOT FOUND"))

(navigate-callback
	#(do ;(log "NAVIGATE event")
		;(log (str "TOKEN: " (.-token %)))
		;(.preventDefault %)
		;(.setToken hist (.-token %))
		(secretary/dispatch! (.-token %))))





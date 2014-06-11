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

(comment (defroute offenders-path "/offenders"
	[]
	(secretary/dispatch! (random-path)))
)

(defroute offenders-path-page #"/offenders(/page/(\d+))?"
	[group id]
	(.log js/console group)
	(.log js/console id)
	)
;(offenders/get-offenders (offenders-path))
(defroute random-path "/offenders/random"
	[]
	(offenders/get-random-offender (random-path)))

(navigate-callback
	#(do (log "NAVIGATE event")
		(log (str "TOKEN: " (.-token %)))
		;(.preventDefault %)
		;(.setToken hist (.-token %))
		(secretary/dispatch! (.-token %))))




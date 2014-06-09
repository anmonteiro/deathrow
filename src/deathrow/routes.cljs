(ns deathrow.routes
	(:require
		[jayq.core :as jayq :refer [$]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h]
		[deathrow.views :as v]
		[secretary.core :as secretary :include-macros true :refer [defroute]]
		[waltz.state :as state]))

;; No need for this just yet
;; (:use-macros [waltz.macros :only [in out defstate defevent]])


(def quote-container ($ :.quote))

(defn render-spinner
	[]
	(-> quote-container
		.empty
		(.append v/spinner)))


(defn render-quote
	[offender]
	(-> quote-container
		.empty
		(.append (v/last-quote offender))))

(defn render-error-quote
	[]
	(-> quote-container
 		.empty
 		(.append v/error-quote)))

(defn init-random-btn-event
	[]
	(-> ($ ".load-statement, .page-header a")
		(.on "click"
			#(do ;(log %)
				(.preventDefault %)
				(h/dispatch! (.-pathname (.-target %)))
				;(log (.getToken hist))
				;(.setToken hist (.-pathname (.-target %)))
				;(secretary/dispatch! (.-pathname (.-target %)))
				;(.setToken hist (.-pathname (.-target %)))
				))))
				
(declare random-path)
(declare offenders-path)
(defroute root-path "/"
	[]
	(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
	            {:dataType "json"
	             :success  (fn [data status xhr]
	             				(render-quote (js->clj data :keywordize-keys true)))
	             :error render-error-quote
	             :timeout 10000}))

(defroute offenders-path "/offenders"
	[]
	(log (offenders-path)))

(defroute random-path (str (offenders-path) "/random")
	[]
	)

(h/navigate-callback
	#(do ;(log "NAVIGATE event")
		;(log (str "TOKEN: " (.-token %)))
		;(.preventDefault %)
		;(.setToken hist (.-token %))
		(secretary/dispatch! (.-token %))))



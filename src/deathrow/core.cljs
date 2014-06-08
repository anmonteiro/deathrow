(ns deathrow.core
	(:use-macros [crate.def-macros :only [defpartial]])
	(:require
		[jayq.core :as jayq :refer [$]]
		[jayq.util :refer [log]]
		[crate.core :as crate]
		[secretary.core :as secretary :include-macros true :refer [defroute]]
		[goog.events :as events]
		[goog.history.EventType :as EventType]
		[goog.history.Html5History :as history5])
	
	)
;(:import goog.History.Html5History)
(def hist (goog.history.Html5History.))

(defroute offenders-path "/"
	[]
	(jayq/document-ready
		(do (secretary/dispatch! (random-path))
			(init-random-btn-event))))
;; (.setToken hist "/offenders/random")
(defroute offenders-path "/offenders"
	[]
	(log (offenders-path)))

(defroute random-path (str (offenders-path) "/random")
	[]
	(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
	            {:dataType "json"
	             :success  (fn [data] (render-quote (js->clj data :keywordize-keys true)))
	             :error render-error-quote})
	;(.setToken hist (random-path))
	)

(defpartial last-quote
	[offender]
	[:blockquote
		[:p (crate/raw (:lastStmt offender))]
		[:footer
			[:cite {:title (str (:firstName offender) " " (:lastName offender))}
				[:a {:href (:profileUrl offender)} (str (:firstName offender) " " (:lastName offender))]]
				(str ", executed " (:dateExecuted offender))]])


(defpartial error-quote
	[]
	[:h2 "There was an error loading your request. "
		[:small "Try again!"]])


(defn render-quote
	[offender]
	(-> ($ ".quote")
		(.empty)
		(.append (last-quote offender))))

(defn render-error-quote
	[]
	(-> ($ ".quote")
 		.empty
 		(.append error-quote)))

(defn init-random-btn-event
	[]
	(-> ($ ".load-statement")
		;(.on "click" #(do (secretary/dispatch! (random-path)) (.preventDefault %)))
		))

(defn init-history
	[]
	(doto hist
		(goog.events/listen EventType/NAVIGATE #(do (log "NAVIGATE event")
													(log %)
													(log (str "TOKEN: " (.-token %)))
													(.stopPropagation %)
													(secretary/dispatch! (.-token %))
													;(.setToken hist (.-token %))
													(log "oi"))))
		(.setUseFragment false)
		(.setPathPrefix "")
		(.setEnabled true))

(defn init
	[]
	(do
		(log "Initializing...")
		))

(init-history)


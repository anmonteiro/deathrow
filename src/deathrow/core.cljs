(ns deathrow.core
	(:use-macros [crate.def-macros :only [defpartial]])
	(:require
		[jayq.core :as jayq :refer [$]]
		[jayq.util :refer [log]]
		[crate.core :as crate]))

(enable-console-print!)


(defpartial lastQuote
	[offender]
	[:blockquote
		[:p (crate/raw (.-lastStmt offender))]
		[:footer
			[:cite {:title (str (.-firstName offender) " " (.-lastName offender))}
				[:a {:href (.-profileUrl offender)} (str (.-firstName offender) " " (.-lastName offender))]]
				(str ", executed " (.-dateExecuted offender))]])


(defn render-quote
	[offender]
	(-> ($ ".quote")
		(.empty)
		(.append (lastQuote offender))))


(defn fetch-random-offender
	[]
	(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
	                {:dataType "json"
	                 :success  (fn [data] (render-quote data))}))


(defn init-random-btn-event
	[]
	(-> ($ ".load-statement")
		(.on "click" fetch-random-offender)))


(defn init
	[]
	(do
		(jayq/document-ready fetch-random-offender)
		(init-random-btn-event)))

(init)


(ns deathrow.routes
	(:require
		[jayq.core :as jayq :refer [$ ajax]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h :refer [navigate-callback]]
		[deathrow.constants :as C]
		[deathrow.templates.main :as m :refer [spinner]]
		[deathrow.templates.offenders.random :as t :refer [last-quote error-quote quote-wrapper]]
		[deathrow.views.core :as v :refer [render]]
		[deathrow.views.offenders.random :as views]
		[secretary.core :as secretary :include-macros true :refer [defroute]]))

(declare random-path)
(declare offenders-path)

(defroute root-path "/"
	[]
	(secretary/dispatch! (random-path)))

(defroute offenders-path "/offenders"
	[]
	(log (offenders-path)))

(defroute random-path "/offenders/random"
	[]
	(let [ajax-timeout (atom 0)]
		(ajax (str C/AJAX-HOSTNAME (random-path))
			{:dataType "json"
			:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render views/quote-container spinner)) 1000))
			:success #(do (js/clearTimeout @ajax-timeout) (render views/quote-container (last-quote (js->clj %1 :keywordize-keys true))))
			:error #(do (js/clearTimeout @ajax-timeout) (render views/quote-container (error-quote)))
			:timeout 10000})))

(navigate-callback
	#(do (log "NAVIGATE event")
		(log (str "TOKEN: " (.-token %)))
		;(.preventDefault %)
		;(.setToken hist (.-token %))
		(secretary/dispatch! (.-token %))))




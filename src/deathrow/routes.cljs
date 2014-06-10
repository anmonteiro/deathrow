(ns deathrow.routes
	(:require-macros [deathrow.macros :as macros])
	(:require
		[jayq.core :as jayq :refer [$]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h]
		[deathrow.views :as v]
		[secretary.core :as secretary :include-macros true :refer [defroute]]
		;[waltz.state :as state]
		))

;; No need for this just yet
;; (:use-macros [waltz.macros :only [in out defstate defevent]])


(def quote-container ($ :.quote))


(defn render
	([$elem view]
	(-> $elem
		.empty
		(.append view)))
	([$elem view obj]
	(-> $elem
		.empty
		(.append (view obj)))))


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
	(let [ajax-timeout (atom 0)]
		(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
			{:dataType "json"
			:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (macros/render-quote-container v/spinner)) 1000))
			:success #(do (js/clearTimeout @ajax-timeout) (macros/render-quote-container (v/last-quote (js->clj %1 :keywordize-keys true))))
			:error #(macros/render-quote-container error-quote)
			:timeout 10000})))

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



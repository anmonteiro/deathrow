(ns deathrow.routes
	(:use-macros [deathrow.macros :only [render-quote-container]])
	(:require
		[jayq.core :as jayq :refer [$ ajax]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h]
		[deathrow.constants :as c]
		[deathrow.views :as v]
		[secretary.core :as secretary :include-macros true :refer [defroute]]
		;[waltz.state :as state]
		))

;; No need for this just yet
;; (:use-macros [waltz.macros :only [in out defstate defevent]])


(def quote-container ($ :.quote))

(defn render
	[$elem view]
	(-> $elem
		.empty
		(.append view)))


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
		(ajax "http://deathrow.herokuapp.com/offenders/random"
			{:dataType "json"
			:beforeSend #(reset! ajax-timeout (js/setTimeout (fn [] (render-quote-container v/spinner)) 1000))
			:success #(do (js/clearTimeout @ajax-timeout) (render-quote-container (v/last-quote (js->clj %1 :keywordize-keys true))))
			:error #(do (js/clearTimeout @ajax-timeout) (render-quote-container v/error-quote))
			:timeout 10000})))

(defroute offenders-path "/offenders"
	[]
	(log (offenders-path)))

(defroute random-path "/offenders/random"
	[]
	)

(h/navigate-callback
	#(do (log "NAVIGATE event")
		(log (str "TOKEN: " (.-token %)))
		;(.preventDefault %)
		;(.setToken hist (.-token %))
		(secretary/dispatch! (.-token %))))




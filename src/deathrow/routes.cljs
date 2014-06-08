(ns deathrow.routes
	(:require
		[jayq.core :as jayq :refer [$]]
		[deathrow.util :refer [log]]
		[deathrow.history :as h]
		[deathrow.views :as v]
		[secretary.core :as secretary :include-macros true :refer [defroute]]))

(defn render-quote
	[offender]
	(-> ($ ".quote")
		(.empty)
		(.append (v/last-quote offender))))

(defn render-error-quote
	[]
	(-> ($ ".quote")
 		.empty
 		(.append v/error-quote)))

(defn init-random-btn-event
	[]
	(-> ($ "a")
		(.on "click"
			#(do (log %)
				(.preventDefault %)
				(h/dispatch! (.-pathname (.-target %)))
				;(log (.getToken hist))
				;(.setToken hist (.-pathname (.-target %)))
				))))
				;(secretary/dispatch! (.-pathname (.-target %)))
				;(.setToken hist (.-pathname (.-target %)))

(declare random-path)
(declare offenders-path)
(defroute root-path "/"
	[]
	(log "oi")
	(h/set-token! (random-path)))

(defroute offenders-path "/offenders"
	[]
	(log (offenders-path)))

(defroute random-path (str (offenders-path) "/random")
	[]
	(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
	            {:dataType "json"
	             :success  (fn [data] (render-quote (js->clj data :keywordize-keys true)))
	             :error render-error-quote
	             :timeout 0}))


(h/navigate-callback #(do (log "NAVIGATE event")
	(log %)
	;(.preventDefault %)
	(log (str "TOKEN: " (.-token %)))
	;(.setToken hist (.-token %))
	(secretary/dispatch! (.-token %))))



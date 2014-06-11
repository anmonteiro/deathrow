(ns deathrow.views.core
	(:require [jayq.core :as jayq :refer [$]]
		[deathrow.history :as h :refer [dispatch!]]))

(defn render
	[$elem view]
	(-> $elem
		.empty
		(.append view)))

(defn block-internal-urls
	[]
	(.delegate ($ js/document) :a "click"
		(fn [e]
			(when (= (.-hostname (.-target e)) (-> js/window .-location .-hostname))
				(do (.preventDefault e)
					(h/dispatch! (.-pathname (.-target e))))))))

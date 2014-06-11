(ns deathrow.models.core
	(:require [jayq.core :as jayq :refer [$ ajax]]
		[deathrow.history :as h :refer [dispatch!]]
		[deathrow.constants :as C]))

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

(defn get-ajax
    [path settings]
    (ajax (str C/AJAX-HOSTNAME path)
        (merge
        	{:dataType "json"
        	:timeout 10000} settings)))


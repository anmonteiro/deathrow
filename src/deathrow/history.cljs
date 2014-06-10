(ns deathrow.history
	(:require
		[deathrow.util :refer [log]]
		[deathrow.constants :as C]
		[secretary.core :as secretary]
		[goog.events :as events]
		[goog.History :as History]
		[goog.history.EventType :as EventType]
		[goog.history.Html5History :as Html5History]))

(declare history)
(def prev (atom "/"))

(defn init-history
	[]
	(let [hist (if (Html5History/isSupported)
					(goog.history.Html5History.)
					(goog.History.))]
		(doto hist
			(.setUseFragment false)
			;; hack for GitHub pages
			;; see jekyll/#332 (https://github.com/jekyll/jekyll/issues/332)
			;; for an explanation
			(.setPathPrefix (str C/basepath)))
		hist))

(defn navigate-callback
  ([callback-fn]
   (navigate-callback history callback-fn))
  ([hist callback-fn]
   (doto history
       (events/listen EventType/NAVIGATE
			(fn [e]
			(callback-fn e)))
       (.setEnabled true))))

(defn get-token
  []
  (.getToken history))

(defn set-token!
  [token]
  (.setToken history token))

(defn replace-token! [token] (.replaceToken history token))

(defn dispatch!
	[url]
	(.setToken history url)
	(if (= url @prev)
		(.dispatchEvent history (goog.history.Event. url false))
		(reset! prev url)))

(def history (init-history))

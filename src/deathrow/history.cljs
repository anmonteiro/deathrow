(ns deathrow.history
  (:require [clojure.string :as string]
            [deathrow.constants :as C]
            [goog.events :as events]
            [goog.History :as History]
            [goog.history.Html5History :as Html5History]
            [secretary.core :as secretary]))

(def ^:private *history*)

(defn navigate-callback [hist callback-fn]
   (doto hist
     (events/listen goog.history.EventType.NAVIGATE
                    (fn [e]
                      (callback-fn e)))))

(defn get-token [history]
  (.getToken history))

(defn set-token! [history token]
  (.setToken history token))

(defn replace-token!
  ([token] (replace-token! *history* token))
  ([history token]
    (.replaceToken history token)))

(defn disable-erroneous-popstate! [history]
  (if (.-useFragment_ history)
      (events/unlisten (.-window_ history)
                       goog.events.EventType.POPSTATE
                       (.-onHistoryEvent_ history)
                       false
                       history)))

(defn setup-link-dispatcher! [history]
  (events/listen js/document "click"
                (fn [e]
                  (let [target (.. e -target)
                        tag-name (.-tagName target)
                        token (str (.-pathname target) (.-search target) (.-hash target))]
                    (when (= tag-name "A")
                      (.preventDefault e)
                      (replace-token! history token))))))

(defn init-history []
  (let [hist (if (Html5History/isSupported)
               (goog.history.Html5History. js/window)
               (goog.History.))]
    (set! *history* hist)
    (doto hist
      (.setUseFragment true)
      (.setPathPrefix "/")
      (navigate-callback #(secretary/dispatch! (.-token %)))
      (disable-erroneous-popstate!)
      (.setEnabled true)
      (setup-link-dispatcher!))))

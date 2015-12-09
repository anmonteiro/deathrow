(ns deathrow.history
  (:require [clojure.string :as string]
            [deathrow.constants :as C]
            [goog.events :as events]
            [goog.History :as History]
            [goog.history.Html5History :as Html5History]
            [secretary.core :as secretary]))

(defonce ^{:private true
           :dynamic true} *history* nil)

(defonce ^{:private true
           :dynamic true} *event-keys* [])

(defn navigate-callback [hist callback-fn]
  (let [key (events/listen hist goog.history.EventType.NAVIGATE callback-fn)]
    (set! *event-keys* (conj *event-keys* key))))

(defn get-token
  ([] (get-token *history*))
  ([history] (.getToken history)))

(defn set-token!
  ([token] (set-token! *history* token))
  ([history token] (.setToken history token)))

(defn replace-token!
  ([token] (replace-token! *history* token))
  ([history token]
    (.replaceToken history token)))

(defn disable-erroneous-popstate! [history]
  (when (.-useFragment_ history)
      (events/unlisten (.-window_ history)
                       goog.events.EventType.POPSTATE
                       (.-onHistoryEvent_ history)
                       false
                       history)))

(defn- handle-click [e]
  (let [target (.. e -target)
        tag-name (.-tagName target)
        token (str (.-pathname target) (.-search target) (.-hash target))
        target-host (.. target -hostname)
        curr-host (.. js/window -location -hostname)]
    (when (and (= tag-name "A") (= curr-host target-host))
      (.preventDefault e)
      (set-token! *history* token))))

(defn setup-link-dispatcher! [history]
  (let [key (events/listen js/document "click" handle-click)]
    (set! *event-keys* (conj *event-keys* key))))

(defn on-navigate-event [e]
  (.log js/console "NAV: " (.-token e))
  (secretary/dispatch! (.-token e)))

(defn- teardown-events! []
  (doseq [k *event-keys*]
    (events/unlistenByKey k))
  (set! *event-keys* []))

(defn- dispose! []
  (when-let [hist *history*]
    (teardown-events!)
    (.dispose hist)
    (set! *history* nil)))

(defn init-history []
  (let [hist (if (Html5History/isSupported)
               (goog.history.Html5History. js/window)
               (goog.History.))]
    (set! *history* hist)
    (.log js/console "TOKEN: " (get-token hist))
    (doto hist
      (.setUseFragment true)
      (.setPathPrefix "/")
      (navigate-callback on-navigate-event)
      (disable-erroneous-popstate!)
      (.setEnabled true)
      (setup-link-dispatcher!))))

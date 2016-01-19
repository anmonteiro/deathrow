(ns deathrow.controllers.navigation
  (:require [deathrow.api :as api]
            [deathrow.utils :as utils :refer [put!]]
            [clojure.string :as str]))


;; --- Navigation Multimethod Declarations ---

(defmulti navigated-to
  (fn [history-imp navigation-point args state] navigation-point))

(defmulti post-navigated-to!
  (fn [history-imp navigation-point args previous-state current-state]
    navigation-point))

;; --- Navigation Multimethod Implementations ---

;; --- Default methods

(defn navigated-default [navigation-point args state]
  (-> state
      (assoc :navigation-point navigation-point
             :navigation-data args)))

(defmethod navigated-to :default
  [history-imp navigation-point args state]
  (navigated-default navigation-point args state))

(defn post-default [navigation-point args]
  #_(utils/set-page-title! (or (:_title args)
                          (str/capitalize (name navigation-point)))))

(defmethod post-navigated-to! :default
  [history-imp navigation-point args previous-state current-state]
  (post-default navigation-point args))

;; --- Navigation helper

(defmethod navigated-to :navigate!
  [history-imp navigation-point args state]
  state)

(defmethod post-navigated-to! :navigate!
  [history-imp navigation-point {:keys [path replace-token?]} previous-state current-state]
  (let [path (if (= \/ (first path))
               path ;(subs path 1)
               path)]
    (if replace-token? ;; Don't break the back button if we want to redirect someone
      (.replaceToken history-imp path)
      (.setToken history-imp path))))

;; --- App specific methods

(defmethod navigated-to :landing
  [history-imp navigation-point args state]
  (-> state
      (assoc :navigation-point navigation-point
             :navigation-data args)))

(defmethod post-navigated-to! :landing
  [history-imp navigation-point args previous-state current-state]
  (put! (get-in current-state [:comms :nav])
        [:navigate! {:path (api/offender-token :random-offender)}]))

(defmethod navigated-to :random-offender
  [history-imp navigation-point args state]
  (-> state
      (assoc :navigation-point navigation-point
             :navigation-data args
             :data :loading)))

(defn post-navigated-to-single-offender
  [navigation-point args state]
  (let [api-ch (get-in state [:comms :api])
        offender-url (api/offender-url navigation-point args)]
    (utils/get-ajax offender-url
                    {:success
                      (fn [data]
                        (put! api-ch [navigation-point :success data]))
                     :error
                      (fn [msg]
                        (put! api-ch [navigation-point :error nil]))})))

(defmethod post-navigated-to! :random-offender
  [history-imp navigation-point args previous-state current-state]
  (post-navigated-to-single-offender navigation-point args current-state))

(defmethod navigated-to :offender
  [history-imp navigation-point args state]
  (-> state
      (assoc :navigation-point navigation-point
             :navigation-data args
             :data :loading)))

(defmethod post-navigated-to! :offender
  [history-imp navigation-point args previous-state current-state]
  (post-navigated-to-single-offender navigation-point args current-state))

(defmethod navigated-to :offenders
  [history-imp navigation-point args state]
  (-> state
      (assoc :navigation-point navigation-point
             :navigation-data args
             :data :loading)))

(defmethod post-navigated-to! :offenders
  [history-imp navigation-point args previous-state current-state]
  (let [api-ch (get-in current-state [:comms :api])
        url (api/offenders-url (:path args))]
    (utils/get-ajax url
                    {:success
                      (fn [data]
                        (put! api-ch [:offenders :success data]))
                     :error
                      (fn [msg]
                        (put! api-ch [:offenders :error nil]))})))

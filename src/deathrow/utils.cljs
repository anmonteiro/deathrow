(ns deathrow.utils
  (:require [deathrow.constants :as C]
            [deathrow.history :as h]
            [goog.net.XhrIo :as xhr]
            [goog.events :as gevts]))

(defn log [v & text]
  (let [vs (if (string? v)
             (apply str v text)
             v)]
    (. js/console (log vs))
    v))

(defn display-name
  [{:keys [firstName lastName]}]
  (str firstName " " lastName))

(defn highlight-nav
  [n]
  (let [all (-> js/Array
              .-prototype
              .-slice
              (.call (.querySelectorAll js/document "nav > ul > li")))
        el (.querySelector js/document (str "nav > ul > li:nth-child(" n ")"))]
    (doseq [elem all] (.setAttribute elem "class" ""))
    (set! (.-className el) "active")))

(defn get-ajax
  [path settings]
  (let [request (new goog.net.XhrIo)
        success-fn (:success settings)
        error-fn (:error settings)
        opts (apply dissoc settings [:success :error])]
    (.setTimeoutInterval request 15000)
    (gevts/listen request goog.net.EventType.COMPLETE
      (fn []
        (if (.isSuccess request)
          (success-fn (.getResponseJson request))
          (error-fn (.getLastError request)))))
    (.send request (str C/AJAX-ENDPOINT path) "GET" (.toString opts))))

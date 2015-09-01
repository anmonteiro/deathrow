(ns deathrow.core
  (:require
    [deathrow.utils :as utils]
    [deathrow.history :as h :refer [navigate-callback]]
    [secretary.core :as secretary :refer-macros [defroute]]
    [deathrow.offender :as o]
    [deathrow.offenders :as os]
    [om.core :as om]))

(declare random-path)
(declare offenders-path)

(defroute root-path "/"
  []
  (h/dispatch! (random-path)))

(defroute offenders-path #"(/offenders((/page)?(/\d+)?))"
  [match rest page id]
  (if (or (empty? rest) (not (empty? page)))
    (os/root match)
    (o/root match)))

(defroute random-path "/offenders/random" []
  (o/root))

(defroute error-path "*"
  []
  (utils/log "ERROR: NOT FOUND"))

(navigate-callback
  #(do ;(log "NAVIGATE event")
     ;(log (str "TOKEN: " (.-token %)))
     ;(.preventDefault %)
     ;(.setToken hist (.-token %))
     (secretary/dispatch! (.-token %))))

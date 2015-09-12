(ns deathrow.core
  (:require [deathrow.history :as history]
            [deathrow.offender :as o]
            [deathrow.offenders :as os]
            [secretary.core :as secretary :refer-macros [defroute]]))

(declare random-path)

(defn define-routes! []
  (defroute root-path "/" []
    (history/replace-token! (random-path)))
  (defroute offenders-path #"(/offenders((/page)?(/\d+)?))" [match rest page id]
    (if (or (empty? rest) (not (empty? page)))
      (os/root match)
      (o/root match)))
  (defroute random-path "/offenders/random" []
    (o/root))
  (defroute error-path "*" []
    (.log js/console "ERROR: NOT FOUND")))

(defn setup! []
  (define-routes!)
  (history/init-history))

(defn- teardown! []
  (history/dispose!))

(defn fig-reload-hook []
  (teardown!)
  (setup!))

(setup!)

(ns deathrow.routes
  (:require [cljs.core.async :as async]
            [secretary.core :as secretary :refer-macros [defroute]]))

(defn- send-nav!
  ([nav-chan nav-target]
    (async/put! nav-chan nav-target {}))
  ([nav-chan nav-target args]
    (async/put! nav-chan [nav-target args])))

(defn define-routes! [state]
  (let [nav-ch (get-in @state [:comms :nav])]
    (defroute root-path "/" []
      ;; ToDo: redirect to random-path
      (send-nav! nav-ch :landing {}))
    ;; TODO: revisit this regex
    (defroute offenders-path #"/offenders((/page)?(/\d+)?)" [match page id]
      (if (and (empty? page) (not (empty? id)))
        (send-nav! nav-ch :offender {:path match})
        (send-nav! nav-ch :offenders {:path match})))
    (defroute random-path "/offenders/random" []
      (send-nav! nav-ch :random-offender {}))
    (defroute error-path "*" []
      (.log js/console "ERROR: NOT FOUND")
      (send-nav! nav-ch :error {:status 404}))))

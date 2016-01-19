(ns deathrow.core
  (:require [cljs.core.async :as async :refer [chan close!]]
            [deathrow.components.app :as app]
            [deathrow.controllers.api :as api-con]
            [deathrow.controllers.navigation :as nav-con]
            [deathrow.history :as history]
            [deathrow.routes :as routes]
            [deathrow.state :as state]
            [deathrow.utils :as utils]
            [om.core :as om])
  (:require-macros [cljs.core.async.macros :as am :refer [go alt!]]))
(enable-console-print!)
(defonce navigation-ch (chan))
(defonce api-ch (chan))

(def ^:private debug-state)

(defn install-om [state container comms]
  (om/root app/app
           state
           {:target container
            :shared {:comms comms}}))

(defn find-app-container []
  (.getElementById js/document "app"))

(defn reinstall-om []
  (install-om debug-state (find-app-container) (:comms @debug-state)))

(defn find-top-level-node []
  (.-body js/document))

(defn app-state []
  (let [initial-state (state/initial-state)]
    (-> initial-state
        (merge {:navigation-point :landing
                :comms {:nav navigation-ch
                        :api api-ch
                        :nav-mult (async/mult navigation-ch)
                        :api-mult (async/mult api-ch)}})
        (atom))))

(defn api-handler
  [value state container]
  (let [previous-state @state
        message (first value)
        status (second value)
        api-data (nth value 2 nil)]
         (swap! state (partial api-con/api-event container message status api-data))
         (api-con/post-api-event! container message status api-data previous-state @state)))

(defn nav-handler
  [[navigation-point {:keys [query-params] :as args} :as value] state history]
  (let [previous-state @state]
    (swap! state (partial nav-con/navigated-to history navigation-point args))
    (nav-con/post-navigated-to! history navigation-point args previous-state @state)))

(defn main [state top-level-node history-imp]
  (let [comms       (:comms @state)
        container   (find-app-container)
        nav-tap (chan)
        api-tap (chan)]
    (routes/define-routes! state)
    (install-om state container comms)

    (async/tap (:nav-mult comms) nav-tap)
    (async/tap (:api-mult comms) api-tap)

    (go (while true
          (alt!
            nav-tap ([v] (nav-handler v state history-imp))
            api-tap ([v] (api-handler v state container))
            ;; Capture the current history for playback in the absence
            ;; of a server to store it
            (async/timeout 10000) (do #_(print "TODO: print out history: ")))))
    ;; hack to redirect to :random-offender
    (async/put! navigation-ch [:navigate! {:path "offenders/random"}])))

(defn ^:export setup! []
  (let [state (app-state)
        top-level-node (find-top-level-node)
        history-imp (history/new-history-imp top-level-node)]
    (set! debug-state state)
    (main state top-level-node history-imp)))

(setup!)

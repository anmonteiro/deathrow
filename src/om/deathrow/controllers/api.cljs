(ns deathrow.controllers.api
  (:require ))

(defmulti api-event
  ;; target is the DOM node at the top level for the app
  ;; message is the dispatch method (1st arg in the channel vector)
  ;; args is the 2nd value in the channel vector)
  ;; state is current state of the app
  ;; return value is the new state
  (fn [target message status args state] [message status]))

(defmulti post-api-event!
  (fn [target message status args previous-state current-state] [message status]))

(defmethod api-event :default
  [target message status args state]
  ;; subdispatching for state defaults
  (let [submethod (get-method api-event [:default status])]
    (if submethod
      (submethod target message status args state)
      (do (js/console.log "Unknown api: " message args)
          state))))

(defmethod post-api-event! :default
  [target message status args previous-state current-state]
  ;; subdispatching for state defaults
  (let [submethod (get-method post-api-event! [:default status])]
    (if submethod
      (submethod target message status args previous-state current-state)
      (js/console.log "Unknown api: " message status args))))

(defmethod api-event [:default :success]
  [target message status args state]
  (js/console.log "No api for" (clj->js [message status]))
  state)

(defmethod post-api-event! [:default :success]
  [target message status args previous-state current-state]
  ;#_(js/console.log "No post-api for: " (clj->js [message status]))
  )

(defmethod api-event [:default :error]
  [target message status args state]
  (js/console.log "No api for" (clj->js [message status]))
  state)

(defmethod post-api-event! [:default :error]
  [target message status args previous-state current-state]
  #_(put! (get-in current-state [:comms :errors]) [:api-error args])
  #_(js/console.log "No post-api for: " (clj->js [message status])))

(defmethod api-event [:random-offender :success]
  [target message status args state]
  (merge state args))

(defmethod api-event [:random-offender :error]
  [target message status args state]
  (assoc state :data status))

(defmethod post-api-event! [:random-offender :success]
  [target message status args previous-state current-state]
  current-state)

(defmethod api-event [:offender :success]
  [target message status args state]
  (merge state args))

(defmethod api-event [:offender :error]
  [target message status args state]
  (assoc state :data status))

(defmethod api-event [:offenders :success]
  [target message status args state]
  (merge state args))

(defmethod api-event [:offenders :error]
  [target message status args state]
  (assoc state :data status))

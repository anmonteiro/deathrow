(ns deathrow.parser
  (:require [deathrow.utils :as utils]
            [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state]} k _]
  (let [st @state]
    {:value (get st k)}))

(defmethod read :data
  [{:keys [state ast] :as env} k _]
  (let [st @state]
    (if-let [val (get st k)]
      {:value val}
      {:remote ast})))

(ns deathrow.state)

(defn initial-state []
  {:navigation-point nil
   :path nil
   :nav-pos nil
   :data nil
   :on-success nil
   :loading false
   :error false})

(ns deathrow.api)

(def base-url "/offenders")

(defn offender-url [k & [args]]
  (condp = k
    :random-offender (str base-url "/random")
    :offender (str base-url (:path args))))

(defn offenders-url [& [path]]
  (let [base-url "/offenders"]
   (if path
     (str base-url path)
     base-url)))

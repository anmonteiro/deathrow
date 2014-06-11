(ns deathrow.core
	(:require
		[deathrow.util :refer [log]]
		[deathrow.routes :as routes]
		[deathrow.models.core :as view]))


(defn init
	[]
	(view/block-internal-urls))


(init)

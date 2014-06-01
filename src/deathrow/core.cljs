(ns deathrow.core
	(:require [jayq.core :as jayq :refer [$]]))

(enable-console-print!)


(.log js/console
	(-> ($ js/document)
		(jayq/find :body)))
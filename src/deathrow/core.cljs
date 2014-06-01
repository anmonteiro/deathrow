(ns deathrow.core
	(:use-macros [crate.def-macros :only [defpartial]])
	(:require
		[jayq.core :as jayq :refer [$]]
		[jayq.util :refer [log]]
		[crate.core :as crate]))

(enable-console-print!)
(def test-offender
	{
    :age 42
    :lastName "Aguilar"
    :lastStmtUrl "http://www.tdcj.state.tx.us/death_row/dr_info/aguilarjesuslast.html"
    :dateExecuted "05/24/2006"
    :executionNo 365
    :firstName "Jesus"
    :race "Hispanic"
    :profileUrl "http://www.tdcj.state.tx.us/death_row/dr_info/aguilarjesus.jpg"
    :_id 999191
    :lastStmt " Aguilar, Jesus Ledesma\n Last Statement:\n Yes sir.  I would like to say to my family, I am alright. (Spanish) Where are you Leo; are you there Leo? (Spanish) Don&apos;t lie man.  Be happy.  Are you happy?  Are you all happy? (Spanish)  "
  })

(defpartial lastQuote
	[offender]
	[:blockquote
		[:p (crate/raw (.-lastStmt offender))]
		[:footer
			[:cite {:title (str (.-firstName offender) " " (.-lastName offender))}
				[:a {:href (.-profileUrl offender)} (str (.-firstName offender) " " (.-lastName offender))]]
				", executed 05/24/2006"]])

(defn render-quote
	[offender]
	(-> ($ js/document)
		(jayq/find ".col-md-8.well")
		(.append (lastQuote offender))))

(defn fetch-random-offender
	[]
	(jayq/ajax "http://deathrow.herokuapp.com/offenders/random"
	                {:dataType "json"
	                 :success  (fn [data] (render-quote data))}))



(defn init
	[]
	(jayq/document-ready fetch-random-offender))

(init)

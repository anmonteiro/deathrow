(ns deathrow.test
    (:require-macros [cemerick.cljs.test
                    :refer (is deftest with-test run-tests testing test-var)])
    (:require [cemerick.cljs.test :as t]
                [deathrow.core :as deathrow]))

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

(deftest test-render-quote
    (is (not (nil? (re-find (deathrow/render-quote test-offender) (str (:firstName test-offender) " " (:lastName test-offender)))))))
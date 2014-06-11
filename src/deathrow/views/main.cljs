(ns deathrow.views.main
	(:use-macros [crate.def-macros :only [defpartial]])
	(:require [crate.core :as crate]))

(defpartial spinner []
    [:div#circularG.center-block.spinner.fade
        [:div#circularG_1.circularG]
        [:div#circularG_2.circularG]
        [:div#circularG_3.circularG]
        [:div#circularG_4.circularG]
        [:div#circularG_5.circularG]
        [:div#circularG_6.circularG]
        [:div#circularG_7.circularG]
        [:div#circularG_8.circularG]])

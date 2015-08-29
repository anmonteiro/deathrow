(ns deathrow.common
  (:require
    [om.core :as om :include-macros true]
    [om.dom :as dom]))

(defn error-msg
  [state owner]
  (om/component
    (dom/h2 nil "There was an error loading your request. "
      (dom/small nil "Try again!"))))

(defn spinner
  [state owner]
  (om/component
    (dom/div #js{:id "circularG" :className "center-block spinner fade"}
      (dom/div #js{:id "circularG_1" :className "circularG"})
      (dom/div #js{:id "circularG_2" :className "circularG"})
      (dom/div #js{:id "circularG_3" :className "circularG"})
      (dom/div #js{:id "circularG_4" :className "circularG"})
      (dom/div #js{:id "circularG_5" :className "circularG"})
      (dom/div #js{:id "circularG_6" :className "circularG"})
      (dom/div #js{:id "circularG_7" :className "circularG"})
      (dom/div #js{:id "circularG_8" :className "circularG"}))))

(defn generic-panel
  ([state owner] (generic-panel state owner nil))
  ([state owner opts]
  (om/component
    (dom/div #js{:className "panel panel-default"}
      (dom/div #js{:className "panel-body"}
        (dom/div #js{:className "row"}
          (dom/div #js{:className "col-md-10 col-md-offset-1 well quote"}
            (om/build (:view opts) state)))
        (dom/div #js{:className "col-md-4 col-md-offset-4 text-center"}
          (dom/a #js{:className "btn btn-default load-statement"
                    :href "#/offenders/random"}
                  "Load another random statement")))))))

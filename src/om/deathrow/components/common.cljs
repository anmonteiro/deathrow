(ns deathrow.components.common
  (:require [deathrow.history :as h]
            [deathrow.utils :as utils]
            [om.core :as om]
            [om.dom :as dom]))

(defn panel-body-partial
  [content state owner]
  (om/component
    (dom/div #js{:className "panel-body"}
      (dom/div #js{:className "row"}
        (dom/div #js{:className "col-md-10 col-md-offset-1 well quote"}
          (om/build content state)))
      (dom/div #js{:className "col-md-4 col-md-offset-4 text-center"}
        (dom/button #js{:className "btn btn-default load-statement"
                        :onClick #(h/replace-token! "/offenders/random")}
          "Load another random statement")))))

(defn error-msg*
  [state owner]
  (om/component
    (dom/h2 nil "There was an error loading your request. "
      (dom/small nil "Try again!"))))

(def error-msg
  (partial panel-body-partial error-msg*))

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
   (reify
     om/IRender
     (render [_]
      (dom/div #js{:className "panel panel-default"}
               (when-let [panel-heading (:heading opts)]
                 (om/build panel-heading state))
               (condp = (:data state)
                 :loading (om/build spinner {})
                 :error (om/build error-msg {})
                 ;; else
                 (om/build (:view opts) state))
               (when-let [panel-footer (:footer opts)]
                 (om/build panel-footer state)))))))

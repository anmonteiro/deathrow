(ns deathrow.components.app
  (:require [deathrow.components.common :as common]
            [deathrow.components.offender :as offender]
            [deathrow.components.offenders :as offenders]
            [om.core :as om]
            [om.dom :as dom]))

(defn dominant-component [app-state owner]
  ;; map navigation point in app state to components
  ;(js/console.log "navigation point: " (str (:navigation-point app-state)))
  (case (:navigation-point app-state)
    ;; for now, :landing redirects to :random-offender
    :landing #(om/component (dom/div nil))
    :offender offender/offender-component
    :random-offender offender/offender-component
    :offenders offenders/offenders-component))

(defn maybe-active
  [curr expected]
  (when (= curr expected)
    #js {:className "active"}))

(defn app* [state owner opts]
  (reify
    om/IDisplayName (display-name [_] "App")
    om/IRender
    (render [_]
      (let [dom-com (dominant-component state owner)]
        (dom/div #js {:className "container"}
          (dom/header nil
            (dom/h1 #js {:className "page-header"}
              (dom/a #js {:href "/"} "Deathrow")
              " "
              (dom/small nil "Random last statements from executed offenders"))
            (dom/nav #js {:className "navbar" :role "navigation"}
              (dom/ul #js {:className "nav nav-pills"}
                (dom/li (maybe-active (:navigation-point state) :random-offender)
                  (dom/a #js {:href "/offenders/random"} "Random Statement"))
                (dom/li (maybe-active (:navigation-point state) :offenders)
                  (dom/a #js {:href "/offenders"} "Executed Offenders")))))
          (dom/div #js {:className "row"}
            (om/build dom-com state))
          (dom/footer #js {:className "row"}
            (dom/div #js {:className "col-md-6 col-md-offset-3"}
              (dom/p #js {:className "text-center"}
                "Made by "
                (dom/a #js {:href "https://github.com/anmonteiro"} "anmonteiro")
                " - "
                (dom/a #js {:className "badge alert-info"
                            :href "http://www.tdcj.state.tx.us/death_row/dr_executed_offenders.html"}
                  "Information Source")))))))))

(defn app [state owner opts]
  (reify
    om/IDisplayName (display-name [_] "App Wrapper")
    om/IRender (render [_] (om/build app* state {:opts opts}))))

(ns deathrow.common
  (:require [deathrow.history :as h]
            [deathrow.utils :as utils]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(def app-element (.getElementById js/document "content"))

(defonce app-state {:path nil})
                    ;:data nil
                    ;; :data is always remote, adding it to our state map will break,
                    ;; because Om Next will merge incoming novelty with merge-with


(defn build-app-state [& args]
  (-> (apply merge app-state args)
      (atom)))

(defui PanelBody
  Object
  (render [this]
    (dom/div #js {:className "panel-body"}
      (dom/div #js {:className "row"}
        (apply dom/div #js {:className "col-md-10 col-md-offset-1 well quote"}
          (om/children this)))
      (dom/div #js {:className "col-md-4 col-md-offset-4 text-center"}
        (dom/button #js {:className "btn btn-default load-statement"
                         :onClick #(h/replace-token! "/offenders/random")}
          "Load another random statement")))))

(def panel-body
  (om/factory PanelBody))

(defn error-msg []
  (panel-body nil
    (dom/h2 nil "There was an error loading your request. "
      (dom/small nil "Try again!"))))

(defn spinner []
  (dom/div #js{:id "circularG" :className "center-block spinner fade"}
    (dom/div #js{:id "circularG_1" :className "circularG"})
    (dom/div #js{:id "circularG_2" :className "circularG"})
    (dom/div #js{:id "circularG_3" :className "circularG"})
    (dom/div #js{:id "circularG_4" :className "circularG"})
    (dom/div #js{:id "circularG_5" :className "circularG"})
    (dom/div #js{:id "circularG_6" :className "circularG"})
    (dom/div #js{:id "circularG_7" :className "circularG"})
    (dom/div #js{:id "circularG_8" :className "circularG"})))

(defui GenericPanel
  Object
  (initLocalState [this]
    {:loading true
     :eror false})
  (componentWillReceiveProps [this next-props]
    ;; is data already here?
    ;; Assume that :data is where it lives, so this is not that generic after all
    (let [data (get next-props :data)
          loc-st {:loading false}
          new-st (if (= data :not-found)
                   (merge loc-st {:error true})
                   loc-st)]
      (when data
        (om/set-state! this new-st))))
  (render [this]
    (let [{:keys [loading error]} (om/get-state this)
          {:keys [nav-pos data] :as props} (om/props this)
          children (cond
                     loading [(spinner)]
                     error [(error-msg)]
                     :else (om/children this))]
      (apply dom/div #js {:className "panel panel-default"}
        children))))

(def generic-panel
  (om/factory GenericPanel))

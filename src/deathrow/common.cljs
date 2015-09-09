(ns deathrow.common
  (:require [deathrow.utils :as utils]
            [om.core :as om :include-macros true]
            [om.dom :as dom]))

(def app-element (.getElementById js/document "content"))

(def app-state (atom {:path nil
                       :nav-pos nil
                       :data nil
                       :on-success nil}))

(defn panel-body-partial
  [content state owner]
  (om/component
    (dom/div #js{:className "panel-body"}
      (dom/div #js{:className "row"}
        (dom/div #js{:className "col-md-10 col-md-offset-1 well quote"}
          (om/build content state)))
      (dom/div #js{:className "col-md-4 col-md-offset-4 text-center"}
        (dom/a #js{:className "btn btn-default load-statement"
                   :href "/offenders/random"}
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

(defn on-success*
  [state owner data]
  (om/update-state! owner #(assoc % :error false :loading false))
  (when-let [on-success (:on-success state)]
    (on-success state owner data)))

(defn on-error*
  [state owner err]
  (om/update-state! owner
                    #(assoc % :error true :loading false))
  (when-let [on-err (:on-error state)]
    (on-err state owner err)))

(defn generic-panel
  ([app owner] (generic-panel app owner nil))
  ([app owner opts]
   (reify
     om/IInitState
     (init-state
      [_]
      {:loading false
       :error false})
     om/IWillMount
     (will-mount [_]
      (utils/highlight-nav (:nav-pos app))
      (om/set-state! owner :loading true)
      (utils/get-ajax (:path app)
                      {:success #(on-success* app owner %)
                       :error #(on-error* app owner %)}))
     om/IRenderState
     (render-state [_ {:keys [loading error]}]
      (dom/div #js{:className "panel panel-default"}
        (when-let [panel-heading (:heading opts)]
          (om/build panel-heading app))
          (cond
            loading (om/build spinner {})
            error (om/build error-msg {})
            :else (om/build (:view opts) app))
        (when-let [panel-footer (:footer opts)]
          (om/build panel-footer app)))))))

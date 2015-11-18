(ns deathrow.offenders
  (:require [deathrow.common :as c]
            [deathrow.parser :as p]
            [deathrow.utils :as utils]
            [goog.string :as gstr]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(defui Pager
  static om/IQuery
  (query [this]
    [:paging])
  Object
  (render [this]
    (let [{:keys [prev next]} (om/props this)
          ;; ToDo: handle loading since it's in generic panel local state
          loading false ;(:loading state)
          ]
      (dom/ul #js {:className "pager"}
        (dom/li #js {:className (str "previous" (when (or (zero? prev) loading) " disabled"))}
          (let [text (gstr/unescapeEntities "&larr; Previous")]
            (if (and (pos? prev) (not loading))
              (dom/a #js {:href (str "/offenders/page/" prev)} text)
              (dom/span nil text))))
        (dom/li #js {:className (str "next" (when loading " disabled"))}
          (let [text (gstr/unescapeEntities "Next &rarr;")]
            (if (not loading)
              (dom/a #js {:href (str "/offenders/page/" next)} text)
              (dom/span nil text))))))))

(def pager (om/factory Pager))

(defui OffenderRow
  static om/IQuery
  (query [this]
    [:executionNo :firstName :lastName :race :age])
  Object
  (render [this]
    (let [{:keys [executionNo firstName lastName race age]} (om/props this)]
      (dom/tr nil
        (dom/td nil executionNo)
        (dom/td nil
          (dom/a #js{:href (str "/offenders/" executionNo)}
                 (utils/display-name firstName lastName)))
        (dom/td nil race)
        (dom/td nil age)))))

(def offender-row (om/factory OffenderRow {:keyfn :executionNo}))

(defn- panel-heading-or-footer
  [class-name pager-state]
  (dom/div #js{:className class-name}
    (pager pager-state)))

(def panel-heading
  (partial panel-heading-or-footer "panel-heading"))

(def panel-footer
  (partial panel-heading-or-footer "panel-footer"))

(defn offenders-table
  [{:keys [data] :as props}]
  (dom/table #js{:className "table table-hover"}
    (dom/thead nil
      (dom/tr nil
        (dom/th nil "Execution #")
        (dom/th nil "Name")
        (dom/th nil "Race")
        (dom/th nil "Age")))
    (apply dom/tbody nil
      (map offender-row data))))

(defui Offenders
  static om/IQuery
  (query [this]
    (vec (concat [{:data (om/get-query OffenderRow)}]
                 (om/get-query Pager))))
  Object
  (componentWillMount [this]
    (utils/highlight-nav 2))
  (render [this]
    (let [props (om/props this)
          {:keys [paging]} props]
      (c/generic-panel props
        (panel-heading paging)
        (offenders-table props)
        (panel-footer paging)))))

(-> Offenders (utils/set-display-name "Offenders"))

(def reconciler)

(defn root [path]
  (let [rec (om/reconciler {:state (c/build-app-state {:path path :nav-pos 2})
                            :parser (om/parser {:read p/read})
                            :send (utils/get-ajax-next path)})]
    (set! reconciler rec)
    (om/add-root! reconciler Offenders c/app-element)))

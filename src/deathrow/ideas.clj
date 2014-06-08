(fn [e]
	(callback-fn {:token (keyword (.-token e))
				  :type (.-type e)
				  :navigation? (.-isNavigation e)}))

	


(defn init-history
  []
  (let [history (if (history5/isSupported)
                	(goog.history.Html5History.)
                  	(goog.History.))]
	  (.setEnabled history true)
      (gevents/unlisten (.-window_ history) (.-POPSTATE gevents/EventType) ; This is a patch-hack to ignore double events
      		            (.-onHistoryEvent_ history), false, history)
      history))
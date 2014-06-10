(ns deathrow.macros)


(defmacro render-quote-container
	[view]
	`((partial deathrow.routes/render deathrow.routes/quote-container) ~view))

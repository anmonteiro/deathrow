(defproject deathrow "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [crate "0.2.4"]
                 [jayq "2.5.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]]
  :source-paths ["src"
  "comp/clojurescript/src/clj"
              "comp/clojurescript/src/cljs"]
  :cljsbuild { 
    :builds [{:id "deathrow"
              :source-paths ["src"]
              :compiler {
                :output-to "deathrow.js"
                :output-dir "out"
                :optimizations :none
                ;:libs ["public/js/jquery-2.1.1.min.js"]
                :source-map true}}]})

(defproject deathrow "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [crate "0.2.4"]
                 [jayq "2.5.1"]
                 [secretary "1.1.1"]]

  :plugins [[lein-cljsbuild "1.0.2"]
            [com.cemerick/clojurescript.test "0.3.1"]]
  :source-paths ["src"]
  :cljsbuild {
    :builds [{:id "deathrow"
              :source-paths ["src"]
              :compiler {
                :output-to "public/js/deathrow.js"
                :output-dir "out"
                :optimizations :none
                :source-map true
                }}
                {:id "production"
                 :source-paths ["src"]
                 :compiler {
                  :output-to "public/js/deathrow.js"
                  :externs ["target/externs/jquery-1.9.1.extern.js"]
                  :optimizations :advanced
                  :pretty-print false
                }}
                {:id "test"
                :source-paths ["test"]
                :compiler {
                :output-to "target/test/deathrow.test.js"
                :output-dir "target/test"
                :optimizations :none
                }}]
  :test-commands {"unit-tests" ["node" :node-runner
                                  "public/js/jquery-2.1.1.min.js"
                                  "target/test/deathrow.test.js"]}
  :main deathrow.core})


(defproject deathrow "0.1.0-SNAPSHOT"
  :description "Random last statements by executed offenders"
  :url "http://anmonteiro.com/deathrow"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"
                  :exclusion [org.clojure/data.json]]
                 [secretary "1.2.3"]
                 [org.omcljs/om "0.9.0"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [com.cemerick/clojurescript.test "0.3.3"]]
  :source-paths ["src"]
  :clean-targets [[:cljsbuild :builds 0 :compiler :output-dir]
                  [:cljsbuild :builds 0 :compiler :output-to]
                  "target"]
  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :compiler {
                :main deathrow.core
                :output-to "public/js/deathrow.js"
                :output-dir "out"
                :optimizations :none
                :source-map true
                }}
                {:id "prod"
                 :source-paths ["src"]
                 :compiler {
                  :main deathrow.core
                  :output-to "public/js/deathrow.js"
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
                                  "target/test/deathrow.test.js"]}})

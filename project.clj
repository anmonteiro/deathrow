(defproject deathrow "0.1.0-SNAPSHOT"
  :description "Random last statements by executed offenders"
  :url "http://anmonteiro.com/deathrow"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"
                  :exclusion [org.clojure/data.json]]
                 [secretary "1.2.3"]
                 [org.omcljs/om "0.9.0"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.3.8"]
            [com.cemerick/clojurescript.test "0.3.3"]]
  :source-paths ["src"]
  :clean-targets ^{:protect false} [[:cljsbuild :builds 0 :compiler :output-dir]
                  [:cljsbuild :builds 0 :compiler :output-to]
                  "target"]
  :figwheel {:css-dirs ["resources/public/css"]}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel true
                        :compiler {:main deathrow.core
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/deathrow.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :none
                                   :source-map true}}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:main deathrow.core
                                   :output-to "resources/public/js/deathrow.js"
                                   :elide-asserts true
                                   :optimizations :advanced
                                   :pretty-print false}}
                       {:id "test"
                        :source-paths ["test"]
                        :compiler {:output-to "target/test/deathrow.test.js"
                                   :output-dir "target/test"
                                   :optimizations :none}}]
              :test-commands {"unit-tests"
                              ["node" :node-runner "target/test/deathrow.test.js"]}}
  :aliases {"auto-dev" ["do" ["clean"] ["cljsbuild" "auto" "dev"]]
            "publish" ["do" ["clean"] ["cljsbuild" "once" "prod"]]})

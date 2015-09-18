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
                                    [:cljsbuild :builds 1 :compiler :output-to]
                                    "target"]
  :figwheel {:css-dirs ["resources/public/assets/css"]}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel {:on-jsload "deathrow.core/fig-reload-hook"}
                        :compiler {:main deathrow.core
                                   :asset-path "assets/js/out"
                                   :output-to "resources/public/assets/js/deathrow.js"
                                   :output-dir "resources/public/assets/js/out"
                                   :optimizations :none
                                   :source-map true}}
                       {:id "prod"
                        :source-paths ["src"]
                        :compiler {:output-to "resources/public/assets/js/deathrow.js"
                                   :elide-asserts true
                                   :optimizations :advanced
                                   :pretty-print false}}]}
  :aliases {"auto-dev" ["do" ["clean"] ["cljsbuild" "auto" "dev"]]
            "publish" ["do" ["clean"] ["cljsbuild" "once" "prod"]]})

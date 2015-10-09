(defproject deathrow "0.1.0-SNAPSHOT"
  :description "Random last statements by executed offenders"
  :url "http://anmonteiro.com/deathrow"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.122"
                  :exclusion [org.clojure/data.json]]
                 [secretary "1.2.3"]
                 [org.omcljs/om "0.9.0-SNAPSHOT"]]

  :plugins [[lein-cljsbuild "1.1.0"]
            [lein-figwheel "0.3.8"]
            [com.cemerick/clojurescript.test "0.3.3"]]
  :clean-targets ^{:protect false} [[:cljsbuild :builds 0 :compiler :output-dir]
                                    [:cljsbuild :builds 0 :compiler :output-to]
                                    [:cljsbuild :builds 1 :compiler :output-to]
                                    "target"]
  :figwheel {:css-dirs ["resources/public/assets/css"]}
  :aliases {"fig-om" ["do" ["clean"] ["with-profile" "+om" "figwheel" "dev"]]
            "fig-next" ["do" ["clean"] ["with-profile" "+next" "figwheel" "dev"]]})

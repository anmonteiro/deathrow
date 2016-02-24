(defproject deathrow "0.1.0-SNAPSHOT"
  :description "Random last statements by executed offenders"
  :url "http://anmonteiro.com/deathrow"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.7.228"
                  :exclusion [org.clojure/data.json]]
                 [org.clojure/core.async "0.2.374"]
                 [org.omcljs/om "1.0.0-alpha31-SNAPSHOT"]
                 [com.cognitect/transit-cljs "0.8.237"]
                 [secretary "1.2.3"]]

  :plugins [[lein-cljsbuild "1.1.2"]
            [lein-figwheel "0.5.0-6"]]
  :clean-targets ^{:protect false} [[:cljsbuild :builds 0 :compiler :output-dir]
                                    [:cljsbuild :builds 0 :compiler :output-to]
                                    [:cljsbuild :builds 1 :compiler :output-to]
                                    "target"]
  :figwheel {:css-dirs ["resources/public/assets/css"]}
  :aliases {"fig-om" ["do" ["with-profile" "+om" "clean"]
                           ["with-profile" "+om" "figwheel" "dev"]]
            "fig-next" ["do" ["with-profile" "+next" "clean"]
                             ["with-profile" "+next" "figwheel" "dev"]]
            "publish-om" ["do" ["with-profile" "+om" "clean"]
                               ["with-profile" "+om" "cljsbuild" "once" "prod"]]
            "publish-next" ["do" ["with-profile" "+next" "clean"]
                               ["with-profile" "+next" "cljsbuild" "once" "prod"]]})

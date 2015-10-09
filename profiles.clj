{:om {:source-paths ["src/om"]
      :cljsbuild {:builds [{:id "dev"
                           :source-paths ["src/om"]
                           :figwheel {:on-jsload "deathrow.core/fig-reload-hook"}
                           :compiler {:main deathrow.core
                                      :asset-path "assets/js/out"
                                      :output-to "resources/public/assets/js/deathrow.js"
                                      :output-dir "resources/public/assets/js/out"
                                      :optimizations :none
                                      :source-map true}}
                           {:id "prod"
                            :source-paths ["src/om"]
                            :compiler {:output-to "resources/public/assets/js/deathrow.js"
                                       :elide-asserts true
                                       :optimizations :advanced
                                       :pretty-print false}}]}}
 :next {:source-paths ["src/om-next"]
        :cljsbuild {:builds [{:id "dev"
                              :source-paths ["src/om-next"]
                              :figwheel {:on-jsload "deathrow.core/fig-reload-hook"}
                              :compiler {:main deathrow.core
                                         :asset-path "assets/js/out"
                                         :output-to "resources/public/assets/js/deathrow.js"
                                         :output-dir "resources/public/assets/js/out"
                                         :optimizations :none
                                         :source-map true}}
                            {:id "prod"
                             :source-paths ["src/om-next"]
                             :compiler {:output-to "resources/public/assets/js/deathrow.js"
                                        :elide-asserts true
                                        :optimizations :advanced
                                        :pretty-print false}}]}}}

{:om {:source-paths ["src/common" "src/om"]
      :cljsbuild {:builds [{:id "dev"
                           :source-paths ["src/common" "src/om"]
                           :figwheel true
                           :compiler {:main deathrow.core
                                      :asset-path "assets/js/out"
                                      :output-to "resources/public/assets/js/deathrow.js"
                                      :output-dir "resources/public/assets/js/out"
                                      :parallel-build true
                                      :optimizations :none
                                      :source-map true}}
                           {:id "prod"
                            :source-paths ["src/common" "src/om"]
                            :compiler {:output-to "resources/public/assets/js/deathrow.js"
                                       :parallel-build true
                                       :elide-asserts true
                                       :optimizations :advanced
                                       :pretty-print false}}]}}
 :next {:source-paths ["src/common" "src/om-next"]
        :cljsbuild {:builds [{:id "dev"
                              :source-paths ["src/common" "src/om-next"]
                              :figwheel true
                              :compiler {:main deathrow.core
                                         :asset-path "assets/js/out"
                                         :output-to "resources/public/assets/js/deathrow.js"
                                         :output-dir "resources/public/assets/js/out"
                                         :parallel-build true
                                         :optimizations :none
                                         :source-map true}}
                            {:id "prod"
                             :source-paths ["src/common" "src/om-next"]
                             :compiler {:output-to "resources/public/assets/js/deathrow.js"
                                        :elide-asserts true
                                        :optimizations :advanced
                                        :parallel-build true
                                        :pretty-print false}}]}}}

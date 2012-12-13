(defproject cascalog-demo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-cljsbuild "0.2.9"]]
  :hooks [leiningen.cljsbuild]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [cascalog "1.10.0"]
                 [domina "1.0.0"]
                 [aleph "0.3.0-beta8"]]
  :profiles {:dev {:dependencies [[org.apache.hadoop/hadoop-core "0.20.2-dev"
                                   :exclusions [commons-codec/commons-codec]]]}}
  :aliases {"server" ["run" "-m" "cascalog-demo.server"]
            "cljs" ["trampoline" "cljsbuild" "auto"]
            "cljs-repl" ["trampoline" "cljsbuild" "repl-listen"]}
  :cljsbuild {:crossovers []
              :crossover-jar true
              :builds [{:source-path "src-cljs"
                        :compiler {:output-to "resources/public/js/main.js"
                                   :warnings true
                                   :optimizations :whitespace
                                   :print-input-delimiter true
                                   :pretty-print true}}]})

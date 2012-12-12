(ns cascalog-demo.server
  (:use [cascalog.playground]
        [lamina.core]
        [aleph.http :only [start-http-server]]
        [cascalog-demo.cascalog]
        [clojure.pprint :only [pprint]]))

(bootstrap)
(def broadcast (channel* :transactional? true))
(def data (atom []))
(add-watch data
           :d3
           (fn [_ _ _ new-value]
             (enqueue broadcast (pr-str new-value))))

(defn inbound-handler
  [msg]
  (reset! data (run-query-file "src/cascalog_demo/query.clj")))

(def websocket-handler
  (fn [request-channel connection-details]
    (siphon broadcast request-channel)))

; Server startup/shutdown management.
(def stop-http-server-atom (atom nil))

(defn stop-server
  []
  (swap! stop-http-server-atom
         (fn [stop-http-server]
           (if stop-http-server
             (do
               (deref (stop-http-server))
               (println "STOPPED.")))
           nil)))

(defn start-server
  []
  (stop-server)
  (println "STARTING.")
  (reset! stop-http-server-atom
          (start-http-server
            #'websocket-handler
            {:port 8060
             :websocket true})))

(defn -main
  [& args]
  (start-server))
(start-server)
;(stop-server)
(reset! data (run-query-file "src/cascalog_demo/query.clj"))
(reset! data '((["time" 45]
                ["what" 90]
                ["thing" 10])))


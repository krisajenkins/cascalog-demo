; -*- inferior-lisp-program: "lein cljs-repl" -*-

(ns cascalog-demo.demo
  (:require clojure.browser.repl)
  (:use [cljs.reader :only [read-string]]
        [domina :only [append! set-text!]]
        [domina.xpath :only [xpath]]
        [cascalog-demo.d3 :only [linear-scale data append attr text style]]))

; Devel.
(set! *print-fn* #(.log js/console %))
(clojure.browser.repl/connect "http://localhost:9000/repl")

(def a-scale (linear-scale :domain [1 50]
                           :range [200 300]))

(defn show-numbers
  [coll]
  (let [selection (-> js/d3
                      (.select "div#content")
                      (.selectAll "div.thing")
                      (data coll identity))
        enter (-> (.enter selection)
                  (append :div)
                  (attr :class "thing")
                  (text identity)
                  (style {:margin-top "5px"
                          :padding "5px"
                          :margin-left "50px"
                          :width a-scale
                          :background-color "lightgoldenrodyellow"
                          :border-style "solid"
                          :border-color "black"
                          :border-width "1px"
                          :opacity 0}))
        exit (.exit selection)
        enter-transition (-> (.transition enter)
                             (.duration 500)
                             (style {:opacity 1}))
        exit-transition (-> (.transition exit)
                            (.duration 500)
                            (style {:opacity 0
                                    :margin-top 0
                                    :margin-bottom 0
                                    :padding-top 0
                                    :padding-bottom 0
                                    :height 0})
                            .remove)]
    selection))

(defn show-people
  [people]
  (let [selection (-> js/d3
                      (.select "div#content")
                      (.selectAll "div.person")
                      (data people identity))
        enter (-> (.enter selection)
                  (append :div)
                  (attr :class "person")
                  (text (fn [x] (str (first x) " " (second x))))
                  (style {:opacity 0
                          :border-style "solid"
                          :border-width "1px"
                          :border-color "black"
                          :margin "5px"
                          :padding "5px"
                          :width (fn [x] (a-scale (second x)))}))
        exit (.exit selection)
        exit-transition (-> (.transition exit)
                            (.duration 500)
                            (style {:opacity 0})
                            .remove)
        enter-transition (-> (.transition enter)
                             (.duration 500)
                             (style {:opacity 1}))]
    selection))

(defn shift-in-a-number
  [numbers]
  (take 5 (cons (rand-int 50)
                numbers)))

(defn read-websocket-datagram
  [datagram]
  (print datagram)
  (->> datagram
       .-data
       read-string))

(defn show-number-series
  []
  (let [my-series (atom [])]
    (dotimes [_ 5]
      (swap! my-series shift-in-a-number))
    (add-watch my-series :d3 (fn [_ _ _ new-value]
                               (show-numbers new-value)))
    (js/setInterval #(swap! my-series shift-in-a-number) 1000)))

(defn show-websocket-data
  []
  (let [query-data (atom [])
        socket (js/WebSocket. "ws://localhost:8060")]
    (aset socket "onopen" (fn [event]
                            (print "OPEN" event)
                            (.send socket "ANYTHING")))
    (aset socket "onclose" (fn [event]
                             (print "CLOSE" event)))
    (aset socket "onerror" (fn [event]
                             (print "CLOSE" event)))
    (aset socket "onmessage" (fn [datagram]
                               (->> (read-websocket-datagram datagram)
                                    first
                                    (swap! query-data))))
    (add-watch query-data
               :d3
               (fn [_ _ _ new-value]
                 (show-people new-value)))))

(defn main-
  []
  (show-number-series)
  (show-websocket-data))

(set! *lein-clj-fn* main-)

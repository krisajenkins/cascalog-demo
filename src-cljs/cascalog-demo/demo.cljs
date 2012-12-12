; -*- inferior-lisp-program: "lein cljs-repl" -*-

(ns cascalog-demo.demo
  (:require clojure.browser.repl)
  (:use [domina :only [append! set-text!]]
        [domina.xpath :only [xpath]]
        [cascalog-demo.d3 :only [linear-scale data append attr text style]]))

; Devel.
(set! *print-fn* #(.log js/console %))
(clojure.browser.repl/connect "http://localhost:9000/repl")

(def a-scale (linear-scale :domain [0 50]
                           :range [200 300]))

(defn display
  [coll & {:keys [id]}]
  (let [selection (-> js/d3
                      (.select "div#content")
                      (.selectAll "div.thing")
                      (data coll id))
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
                            .remove
                            )]
    selection))

(defn update-series
  [series]
  (swap! series (fn [numbers]
                  (take 5 (cons (rand-int 50)
                                numbers)))))

(def my-series (atom []))
(add-watch my-series :d3 (fn [_ _ _ new-value]
                           (display new-value
                                    :id identity)))

(dotimes [n 5]
  (update-series my-series))
(js/setInterval #(update-series my-series) 1000)


#_(defn display
    [coll]
    (make-d3 ((.select "div#content")
              (.selectAll "div.thing")
              (data coll identity))
             :enter ((append :div)
                     (attr :class "thing")
                     (text identity)
                     (style {:margin "5px"
                             :padding "5px"
                             :width a-scale
                             :background-color "lightgoldenrodyellow"
                             :border-style "solid"
                             :border-color "black"
                             :border-width "1px"
                             :opacity 0})
                     .transition
                     (.duration 500)
                     (style {:opacity 1}))
             :exit (.transition
                    (.duration 500)
                    (style {:opacity 0
                            :margin-top 0
                            :margin-bottom 0
                            :padding-top 0
                            :padding-bottom 0
                            :height 0})
                    .remove)))

; -*- inferior-lisp-program: "lein cljs-repl" -*-

(ns cascalog-demo.d3)

(defn linear-scale
  "Creates a linear scale, with a default domain and range of 0->1."
  [& {:keys [domain range]
      :or {domain [0 1]
           range [0 1]}}]
  (-> (.linear d3/scale)
      (.domain (apply array domain))
      (.range (apply array range))))

(defn data
  [selection coll id-function]
  (.data selection (apply array coll) id-function))

(defn append
  [selection element]
  (.append selection
           (name element)))

(defn attr
  [selection key value]
  (.attr selection
          (name key)
          value))

(defn text
  [selection f]
  (.text selection f))

(defn style
  [selection m]
  (if (empty? m)
    selection
    (let [[[k v] & rest] m]
      (recur
       (.style selection (name k) v)
       rest))))

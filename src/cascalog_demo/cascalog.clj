(ns cascalog-demo.cascalog
  (:use [cascalog.api]
        [cascalog.playground]
        [clojure.pprint :only [pprint]]))

(bootstrap)

(def people [{:name "Kris"
              :age 35
              :gender :male}
             {:name "Alex"
              :age 39
              :gender :male}
             {:name "Paul"
              :age 45
              :gender :male}
             {:name "Fran"
              :age 36
              :gender :female}])

(defn name-of [x] (:name x))
(defn age-of [x] (:age x))
(defn gender-of [x] (:gender x))

(defn run-query-file
  [path]
  (-> path
      slurp
      read-string
      eval
      ??-
      ))

(run-query-file "src/cascalog_demo/query.clj")

;(?<- (stdout)
;     [?sum ?avg ?gender]
;     (people ?person)
;     ((c/juxt #'age-of
;              #'gender-of) ?person :> ?age ?gender)
;     (c/sum ?age :> ?sum)
;     (c/avg ?age :> ?avg))

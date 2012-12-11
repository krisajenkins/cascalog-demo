(ns cascalog-demo.cascalog
  (:use cascalog.playground))

(bootstrap)

(?<- (stdout)
     [?person ?gender ?age ?young]
     (gender ?person ?gender)
     (age ?person ?age)
     (follows "emily" ?person)
     (< 30 ?age :> ?young))

(?<- (stdout)
     [?person1 ?person2 ?age1 ?age2 ?delta]
     (age ?person1 ?age1)
     (age ?person2 ?age2)
     (follows ?person1 ?person2)
     (- ?age1 ?age2 :> ?delta)
     (< ?delta 0)
     )

(?<- (stdout)
     [?count]
     (c/count ?count)
     (age _ ?a)
     (< ?a 30)
     )

(let [weight [[:kris 65]
              [:fran 45]]
      height [[:kris 171]
               [:fran 162]]
      ]
  (?<- (stdout)
       [?name ?weight ?height]
       (weight ?name ?weight)
       (height ?name ?height)
       )
  )

(defmapcatop split [sentence]
  (re-seq #"\S+" sentence))

(?<- (stdout)
     [?word ?count]
     (sentence ?s)
     (split ?s :> ?word1)
     (clojure.string/lower-case ?word1 :> ?word)
     (c/count ?count))


(defn step
  [start step]
  (iterate (partial + step)
           start))

(defn agebucket [age]
  (find-first (partial <= age) (step 0 8)) )

(?<- (stdout)
     [?sum-age ?bucket ?count ?gender]
     (age ?name ?age)
     (gender ?name ?gender)
     (agebucket ?age :> ?bucket)
     (c/sum ?age :> ?sum-age)
     (c/count ?count)
     (:sort ?bucket)
     )

(?<- (stdout)
     [?n]
     (integer ?n)
     (* ?n ?n :> ?n)
     )


(?<- (stdout) [?person ?city] (location ?person _ _ ?city))
(?<- (stdout) [?person !city] (location ?person _ _ !city))

(let [many-follows (<- [?person]
                        (follows ?person _)
                        (c/count ?count)
                        (< 1 ?count))
      follow-count (<- [?person ?follow-count]
                       (follows ?person _)
                       (c/count ?follow-count))]
  (?- (stdout)
       many-follows
      (stdout)
      follow-count))

(defn name-of [x] (:name x))
(defn age-of [x] (:age x))
(defn gender-of [x] (:gender x))

(let [people [{:name "Kris"
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
               :gender :female}
              ]]
  (?<- (stdout)
       [?sum ?avg ?gender]
       (people ?person)
       ((c/juxt #'name-of
                #'age-of
                #'gender-of) ?person :> ?name ?age ?gender)
       (c/sum ?age :> ?sum)
       (c/avg ?age :> ?avg)
       ))

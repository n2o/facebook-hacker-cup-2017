(ns hacker-cup.lazy-loading)

(defn fuckoff [in]
  (loop [q (reverse (sort in))
         stack [(first q)]
         carried 0]
    (println {:q q :stack stack :carried carried})
    (if (empty? q)
      carried
      (if (>= (* (first stack) (count stack)) 50)
        (recur (rest q) [(second q)] (inc carried))
        (recur (drop-last q) (conj stack (last q)) carried)))))
(fuckoff [30 30 1 1])
;; => 2
(fuckoff [20 20 20])
;; => 1
(fuckoff [1 2 3 4 5 6 7 8 9 10 11])
;; => 2
(fuckoff [9 19 29 39 49 59])
;; => 3
(fuckoff [32 56 76 8 44 60 47 85 71 91])
;; => 8

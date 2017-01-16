(ns hacker-cup.lazy-loading
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn count-loads [in]
  (loop [q (reverse (sort in))
         stack [(first q)]
         carried 0]
    (if (empty? q)
      carried
      (if (>= (* (first stack) (count stack)) 50)
        (recur (rest q) [(second q)] (inc carried))
        (recur (drop-last q) (conj stack (last q)) carried)))))
(count-loads [30 30 1 1])
;; => 2
(count-loads [20 20 20])
;; => 1
(count-loads [1 2 3 4 5 6 7 8 9 10 11])
;; => 2
(count-loads [9 19 29 39 49 59])
;; => 3
(count-loads [32 56 76 8 44 60 47 85 71 91])
;; => 8

(defn start []
  (loop [file (str/split (slurp "lazy_loading.txt") #"\n")
         pointer 1
         lines (Integer/parseInt (nth file pointer))
         analyze (subvec file (inc pointer) (+ lines (inc pointer)))
         case 1]
    (let [input (map #(Integer/parseInt %) analyze)
          res (count-loads input)
          npointer (+ lines 1 pointer)]
      (spit "output.txt" (str "Case #" case  ": "  res "\n") :append true)
      (when-not (>= npointer (count file))
        (let [nlines (Integer/parseInt (nth file npointer))]
          (recur file npointer nlines (subvec file (inc npointer) (+ nlines (inc npointer))) (inc case)))))))
(start)

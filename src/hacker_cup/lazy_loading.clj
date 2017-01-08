(ns hacker-cup.lazy-loading
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn count-loads [in]
  (loop [q (reverse (sort in))
         stack [(first q)]
         carried 0]
    #_(println {:q q :stack stack :carried carried})
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

(count-loads '(52 22 16 41 81 84 16 61 13 81 87 51 54 68 51 10 19 90 10 63 26 46 97 57 78 53 42 98 28 33 71 18 72 33 63 7 37 18 58 4 35 69 28 70 64 74 35 56 5 9 40 86 40 23 37 6 63 76 78 4 97 88 38 13 1 12 14 71 11 20 96 84 3 75 14 84 49 27 6 60))

(defn start []
  (loop [file (str/split (slurp "lazy_loading.txt") #"\n")
         pointer 1
         lines (Integer/parseInt (nth file pointer))
         analyze (subvec file (inc pointer) (+ lines (inc pointer)))
         case 1]
    (let [input (map #(Integer/parseInt %) analyze)
          res (count-loads input)
          npointer (+ lines 1 pointer)]
      (when (= 16 case)
        (println {:pointer pointer
                  :lines lines
                  :input input
                  :res res
                  :npointer npointer
                  }))
      (spit "output.txt" (str "Case #" case  ": "  res "\n") :append true)
      (when-not (>= npointer (count file))
        (let [nlines (Integer/parseInt (nth file npointer))]
          (recur file npointer nlines (subvec file (inc npointer) (+ nlines (inc npointer))) (inc case)))))))
(start)

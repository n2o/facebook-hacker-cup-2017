(ns hacker-cup.core
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :as combo])
  (:gen-class))

(defn parse-dice [in]
  (let [[dice sides] (str/split in #"d")
        [sides addition] (str/split sides #"[\+|\-]")
        minus? (.contains in "-")
        parsed-addition (when addition (Integer/parseInt addition))]
    {:dice (Integer/parseInt dice)
     :sides (Integer/parseInt sides)
     :addition (if addition (if minus? (- parsed-addition) parsed-addition) 0)}))
(parse-dice "2d4-1")
(parse-dice "2d4+1")
(parse-dice "2d4")


(defn distribution [{:keys [dice sides addition]} lifepoints]
  (let [vals (range 1 (inc sides))
        ;; nvals (apply concat (repeat dice vals))
        nvals (repeat dice vals)
        combinations (apply combo/cartesian-product nvals)
        ;; combinations (combo/combinations nvals dice)
        deadly (filter #(>= (+ addition (apply + %)) lifepoints) combinations)
        ;; sums (map #(apply + %) combinations)
        ;; mod-sums (map #(+ % addition) sums)
        ]
    {:total (count combinations)
     :deadly (count deadly)}))
;; (time (distribution (parse-dice "4d4-10") 10))

(defn calculate-percentage [lifepoints & spells]
  (let [parsed (map parse-dice spells)
        distributions (map #(distribution % lifepoints) parsed)
        percentages (map #(/ (:deadly %) (:total %)) distributions)]
    {:parsed parsed
     :distributions distributions
     :percentages percentages
     :max (apply max percentages)}
    ))

;; (calculate-percentage 2 "2d4" "1d8")
;; => 1.000000
;; (time (calculate-percentage 10 "10d6-10" "1d6+1"))
;; => 0.998520
;; (calculate-percentage 8 "1d4+4" "2d4" "3d4-4")
;; => 0.250000
;; (calculate-percentage 40 "10d4" "5d8" "2d20")
;; => 0.002500

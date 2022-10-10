(ns propeller.problems
  (:use [propeller util genome pushstate interpreter])
        (:require [libpython-clj.python :refer [py. py.. py.-] :as py]))

(py/initialize! :python-executable "C:\\Users\\user\\anaconda3\\python.exe"
                :library-path "C:\\Users\\user\\anaconda3\\python37.dll"
                :windows-anaconda-activate-bat "C:\\Users\\user\\anaconda3\\Scripts\\activate.bat")

(require '[libpython-clj.require :refer [require-python]])

(require-python 'qiskit)

;java.lang.Error: Invalid memory access

;;;;;;;;;
;; Problem: f(x) = 7x^2 - 20x + 13

(defn target-function-hard
  "Target function: f(x) = 7x^2 - 20x + 13"
  [x]
  (+ (* 7 x x)
     (* -20 x)
     13))

(defn target-function
  "Target function: f(x) = x^3 + x + 3"
  [x]
  (+ (* x x x)
     x
     3))

(defn regression-error-function
  "Finds the behaviors and errors of an individual: Error is the absolute deviation between the target output value and the program's selected behavior, or 1000000 if no behavior is produced. The behavior is here defined as the final top item on the :integer stack."
  [argmap individual]
  (let [program (push-from-plushy (:plushy individual))
        inputs (range -10 11)
        correct-outputs (map target-function inputs)
        outputs (map (fn [input]
                       (peek-stack
                         (interpret-program
                           program
                           (assoc empty-push-state :input {:in1 input})
                           (:step-limit argmap))
                         :integer))
                     inputs)
        errors (map (fn [correct-output output]
                      (if (= output :no-stack-item)
                        1000000
                        (abs (- correct-output output))))
                    correct-outputs
                    outputs)]
    (assoc individual
      :behaviors outputs
      :errors errors
      :total-error (apply +' errors))))

;;;;;;;;;
;; String classification

(defn string-classification-error-function
  "Finds the behaviors and errors of an individual: Error is 0 if the value and the program's selected behavior match, or 1 if they differ, or 1000000 if no behavior is produced. The behavior is here defined as the final top item on the :boolean stack."
  [argmap individual]
  (let [program (push-from-plushy (:plushy individual))
        inputs ["GCG" "GACAG" "AGAAG" "CCCA" "GATTACA" "TAGG" "GACT"]
        correct-outputs [false false false false true true true]
        outputs (map (fn [input]
                       (peek-stack
                         (interpret-program
                           program
                           (assoc empty-push-state :input {:in1 input})
                           (:step-limit argmap))
                         :boolean))
                     inputs)
        errors (map (fn [correct-output output]
                      (if (= output :no-stack-item)
                        1000000
                        (if (= correct-output output)
                          0
                          1)))
                    correct-outputs
                    outputs)]
    (assoc individual
      :behaviors outputs
      :errors errors
      :total-error (apply +' errors))))

(defn quantum-superposition-error-function
  "Finds the behaviors and errors of an individual: Error is the absolute deviation between the target output value and the program's selected behavior, or 1000000 if no behavior is produced. The behavior is here defined as the final top item on the :integer stack."
  [argmap individual]
  (let [program (push-from-plushy (:plushy individual))
        ;correct-outputs ({"00" 500, "11" 500})
        circuit (:circuit
                 (interpret-program
                   program
                   (assoc (conj empty-push-state {:circuit (qiskit/QuantumCircuit 2 2)}) :input {:in1 '(0 1)})
                   (:step-limit argmap)))
        measured-circuit (do (py/py. circuit measure [0 1] [1 0]) circuit)
        simulator (py/py. qiskit/Aer get_backend "qasm_simulator")
        job (qiskit/execute measured-circuit simulator :shots 1000)
        results   (py/py. job result)
        counts (py/py. results get_counts measured-circuit)
        map-counts (read-string (clojure.string/replace (clojure.string/replace counts #"'" "\"") #":" " "))
        errors (+ (abs (- (or (get map-counts "00") 0) 500)) (abs (- (or (get map-counts "11") 0) 500)))
        draw (str (py/py. circuit draw))]
(assoc individual
  :behaviors map-counts
  :errors errors
  :total-error errors
  :circuit_draw draw)))
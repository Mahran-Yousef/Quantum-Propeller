(ns propeller.Test
  (:require [libpython-clj.python :refer [py. py.. py.-] :as py]))
;[libpython-clj.require :refer [require-python]]))

(py/initialize! :python-executable "C:\\Users\\user\\anaconda3\\python.exe"
                :library-path "C:\\Users\\user\\anaconda3\\python37.dll"
                :windows-anaconda-activate-bat "C:\\Users\\user\\anaconda3\\Scripts\\activate.bat")

(require '[libpython-clj.require :refer [require-python]])

(require-python 'qiskit)
(require-python '[numpy :as np])

(defn new-circuit
  []
  (qiskit/QuantumCircuit 2 2))

(defn pop-stack
  "Removes top item of stack."
  [state stack]
  (update state stack rest))

(defn peek-stack
  "Returns top item on a stack."
  [state stack]
  (if (empty? (get state stack))
    :no-stack-item
    (first (get state stack))))

(defn hadamard
  [state]
  (if (= (peek-stack state :integer) :no-stack-item)
    state
    (let [circuit (:circuit state)
          qubit (mod (peek-stack state :integer) 2)]
      (py/py. circuit h qubit)
      (pop-stack state :integer))))

(def state
  {:exec    '()
   :integer '(1 0)
   :string  '("abc")
   :input   {:in1 4}
   :circuit (qiskit/QuantumCircuit 2 2)})

(def x {:circuit (qiskit/QuantumCircuit 2 2)})
;(def x {:circuit (new-circuit)})

(defn new-map
  []
  {:circuit (qiskit/QuantumCircuit 2 2)})

(defn -main
  [& args]
  ;(println (str (np/array [[1 2] [3 4]])))
  (def circuit (qiskit/QuantumCircuit 2 2))
  (def simulator (py/py. qiskit/Aer get_backend "qasm_simulator"))
  (py/py. circuit h 0)
  (py/py. circuit cx 0 1)
  (py/py. circuit measure [0 1] [1 0])
  (def job (qiskit/execute circuit simulator :shots 1000))
  (println (str (py/py. circuit draw)))
  (def results (py/py. job result))
  (def counts (py/py. results get_counts circuit))
  (println counts)
  (println "It worked!!!!")
  (hadamard state)
  (def c (:circuit state))
  (println (str (py/py. c draw))))

(ns propeller.instructions
  (:use [propeller util pushstate])
  (:require [libpython-clj.python :refer [py. py.. py.-] :as py]))

(py/initialize! :python-executable "C:\\Users\\user\\anaconda3\\python.exe"
                :library-path "C:\\Users\\user\\anaconda3\\python37.dll"
                :windows-anaconda-activate-bat "C:\\Users\\user\\anaconda3\\Scripts\\activate.bat")

(require '[libpython-clj.require :refer [require-python]])

(require-python 'qiskit)


; Instructions must all be either functions that take one Push state and return another
; or constant literals.
; TMH: ERCs?
(def default-instructions
  (list
    'in1
    'integer_+
    'integer_-
    'integer_*
    'integer_%
    'integer_=
    'exec_dup
    'exec_if
    'boolean_and
    'boolean_or
    'boolean_not
    'boolean_=
    ;'string_=
    ;'string_take
    ;'string_drop
    ;'string_reverse
    ;'string_concat
    ;'string_length
    ;'string_includes?
    ;'close
    ;'CNot
    ;'hadamard
    ;'Not
    0
    1
    true
    false
    ;""
    ;"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    ;"A"
    ;"C"
    ;"G"
    ;"T"
    ))

(def quantum_list
  (list
    'CNot
    'hadamard
    'Not
    0
    1
    'add-qubit1
    'add-qubit0))

(def opens                                                  ; number of blocks opened by instructions (default = 0)
  {'exec_dup 1
   'exec_if  2})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; actual instructions


(defn make-push-instruction
  "A utility function for making Push instructions. Takes a state, the function
  to apply to the args, the stacks to take the args from, and the stack to return
  the result to. Applies the function to the args (taken from the stacks) and pushes
  the return value onto return-stack."
  [state function arg-stacks return-stack]
  (let [args-pop-result (get-args-from-stacks state arg-stacks)]
    (if (= args-pop-result :not-enough-args)
      state
      (let [result (apply function (:args args-pop-result))
            new-state (:state args-pop-result)]
        (push-to-stack new-state return-stack result)))))

;;;;;;;;;
;; Instructions

(defn in1
  "Pushes the input labeled :in1 on the inputs map onto the :exec stack."
  [state]
  (push-to-stack state :exec (:in1 (:input state))))

(defn integer_+
  [state]
  (make-push-instruction state +' [:integer :integer] :integer))

(defn integer_-
  [state]
  (make-push-instruction state -' [:integer :integer] :integer))

(defn integer_*
  [state]
  (make-push-instruction state *' [:integer :integer] :integer))

(defn integer_%
  [state]
  (make-push-instruction state
                         (fn [int1 int2]
                           (if (zero? int2)
                             int1
                             (quot int1 int2)))
                         [:integer :integer]
                         :integer))

(defn integer_=
  [state]
  (make-push-instruction state = [:integer :integer] :boolean))

(defn exec_dup
  [state]
  (if (empty-stack? state :exec)
    state
    (push-to-stack state :exec (first (:exec state)))))

(defn exec_if
  [state]
  (make-push-instruction state
                         #(if %1 %3 %2)
                         [:boolean :exec :exec]
                         :exec))

(defn boolean_and
  [state]
  (make-push-instruction state #(and %1 %2) [:boolean :boolean] :boolean))

(defn boolean_or
  [state]
  (make-push-instruction state #(or %1 %2) [:boolean :boolean] :boolean))

(defn boolean_not
  [state]
  (make-push-instruction state not [:boolean] :boolean))

(defn boolean_=
  [state]
  (make-push-instruction state = [:boolean :boolean] :boolean))

(defn string_=
  [state]
  (make-push-instruction state = [:string :string] :boolean))

(defn string_take
  [state]
  (make-push-instruction state
                         #(apply str (take %1 %2))
                         [:integer :string]
                         :string))

(defn string_drop
  [state]
  (make-push-instruction state
                         #(apply str (drop %1 %2))
                         [:integer :string]
                         :string))

(defn string_reverse
  [state]
  (make-push-instruction state
                         #(apply str (reverse %))
                         [:string]
                         :string))

(defn string_concat
  [state]
  (make-push-instruction state
                         #(apply str (concat %1 %2))
                         [:string :string]
                         :string))

(defn string_length
  [state]
  (make-push-instruction state count [:string] :integer))

(defn string_includes?
  [state]
  (make-push-instruction state clojure.string/includes? [:string :string] :boolean))

(defn hadamard
  [state]
  (if (= (peek-stack state :integer) :no-stack-item)
    state
    (let [circuit (:circuit state)
          qubit (mod (peek-stack state :integer) 2)]
      (py/py. circuit h qubit)
      (pop-stack state :integer))))

(defn Not
  [state]
  (if (= (peek-stack state :integer) :no-stack-item)
    state
    (let [circuit (:circuit state)
          qubit (mod (peek-stack state :integer) 2)]
      (py/py. circuit x qubit)
      (pop-stack state :integer))))

(defn CNot
  [state]
  (if (or (= (peek-stack state :integer) :no-stack-item)
          (= (peek-stack (pop-stack state :integer) :integer) :no-stack-item))
    state
    (let [circuit (:circuit state)
          qubit1 (mod (peek-stack state :integer) 2)
          qubit2 (mod (peek-stack (pop-stack state :integer) :integer) 2)]
      (if (= qubit1 qubit2)
        state
        (do (py/py. circuit cx qubit1 qubit2)
            (pop-stack (pop-stack state :integer) :integer))))))

(defn add-qubit1
  [state]
  (push-to-stack state :integer 1))

(defn add-qubit0
  [state]
  (push-to-stack state :integer 0))
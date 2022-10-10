(ns propeller.core
  (:gen-class)
  (:use [propeller instructions gp problems]))

(defn -main
  "Runs propeller-gp, giving it a map of arguments."
  [& args]
  (binding [*ns* (the-ns 'propeller.core)]
    (gp (update-in (merge {:instructions            default-instructions
                           :error-function          quantum-superposition-error-function
                           :max-generations         500
                           :population-size         500
                           :max-initial-plushy-size 10
                           :step-limit              100
                           :parent-selection        :tournament
                           :tournament-size         5
                           :UMADRate                0.1
                           :variation               {:UMAD 0.5 :crossover 0.5}
                           :elitism                 true}
                          (apply hash-map
                                 (map read-string args)))
                   [:error-function]
                   #(if (fn? %) % (eval %))))))
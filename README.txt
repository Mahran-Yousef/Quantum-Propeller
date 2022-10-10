Quantum-Propeller
A version of propeller that interacts with IBM Q simulator

Results for a run to evolove a crcuit that entagles two quibts:

-------------------------------------------------------
               Report for Generation 0
-------------------------------------------------------
Best plushy: (hadamard add-qubit1 hadamard add-qubit0 0 1 CNot)
Best program: (hadamard add-qubit1 hadamard add-qubit0 0 1 CNot)
Best total error: 58
Best errors: 58
Best behaviors: {00 471, 11 529}
Genotypic diversity: 0.776
Average genome length: 4.588
Circuit : 
          ┌───┐┌─┐   
q_0: ─────┤ X ├┤M├───
     ┌───┐└─┬─┘└╥┘┌─┐
q_1: ┤ H ├──■───╫─┤M├
     └───┘      ║ └╥┘
c_0: ═══════════╬══╩═
                ║    
c_1: ═══════════╩════
                     

-------------------------------------------------------
               Report for Generation 1
-------------------------------------------------------
Best plushy: (add-qubit0 hadamard Not add-qubit1 CNot CNot add-qubit0 CNot)
Best program: (add-qubit0 hadamard Not add-qubit1 CNot CNot add-qubit0 CNot)
Best total error: 6
Best errors: 6
Best behaviors: {00 497, 11 503}
Genotypic diversity: 0.918
Average genome length: 5.984
Circuit : 
     ┌───┐     ┌─┐   
q_0: ┤ H ├──■──┤M├───
     └───┘┌─┴─┐└╥┘┌─┐
q_1: ─────┤ X ├─╫─┤M├
          └───┘ ║ └╥┘
c_0: ═══════════╬══╩═
                ║    
c_1: ═══════════╩════
                     

-------------------------------------------------------
               Report for Generation 2
-------------------------------------------------------
Best plushy: (0 1 hadamard add-qubit1 add-qubit0 1 CNot)
Best program: (0 1 hadamard add-qubit1 add-qubit0 1 CNot)
Best total error: 6
Best errors: 6
Best behaviors: {00 503, 11 497}
Genotypic diversity: 0.948
Average genome length: 6.562
Circuit : 
          ┌───┐┌─┐   
q_0: ─────┤ X ├┤M├───
     ┌───┐└─┬─┘└╥┘┌─┐
q_1: ┤ H ├──■───╫─┤M├
     └───┘      ║ └╥┘
c_0: ═══════════╬══╩═
                ║    
c_1: ═══════════╩════
                     

-------------------------------------------------------
               Report for Generation 3
-------------------------------------------------------
Best plushy: (hadamard add-qubit1 hadamard hadamard add-qubit0 0 1 CNot)
Best program: (hadamard add-qubit1 hadamard hadamard add-qubit0 0 1 CNot)
Best total error: 0
Best errors: 0
Best behaviors: {00 500, 11 500}
Genotypic diversity: 0.966
Average genome length: 7.028
Circuit : 
          ┌───┐┌─┐   
q_0: ─────┤ X ├┤M├───
     ┌───┐└─┬─┘└╥┘┌─┐
q_1: ┤ H ├──■───╫─┤M├
     └───┘      ║ └╥┘
c_0: ═══════════╬══╩═
                ║    
c_1: ═══════════╩════
                     

SUCCESS

Process finished with exit code 0


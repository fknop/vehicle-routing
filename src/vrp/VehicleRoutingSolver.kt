package vrp

import localsearch.LSSolver
import localsearch.Neighbor
import localsearch.heuristic.Heuristic
import vrp.operators.*

class VehicleRoutingSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
      // return  TwoOptOperator(problem, solution).neighborhood() +
       return  KOptOperator(problem, solution).neighborhood() +
//               SwapOperator(problem, solution).neighborhood() +
               IntraRelocateOperator(problem, solution).neighborhood() +
               InterTwoOptOperator(problem, solution).neighborhood()
    }
}


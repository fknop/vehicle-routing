package vrp

import localsearch.HillClimbingSolver
import localsearch.Neighbor
import vrp.operators.*

class VehicleRoutingSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution): HillClimbingSolver(problem, solution) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy): this(problem, strategy.getInitialSolution(problem))

    override fun neighborhood(): List<Neighbor> {
        return TwoOptOperator(problem, solution).neighborhood() +
               RelocateOperator(problem, solution).neighborhood() +
               SwapOperator(problem, solution).neighborhood() +
               IntraRelocateOperator(problem, solution).neighborhood() +
               InterTwoOptOperator(problem, solution).neighborhood()
    }
}


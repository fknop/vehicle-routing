package vrp

import localsearch.HillClimbingSolver
import localsearch.Successor
import vrp.operators.*

class VehicleRoutingSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution): HillClimbingSolver(problem, solution) {

    val relocate = RelocateSolver(problem, solution)
    val tsp = TspHillClimbingSolver(problem, solution)
    val swap = SwapSolver(problem, solution)

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy): this(problem, strategy.getInitialSolution(problem))

    override fun successors(): List<Successor> {
        return tsp.successors() + relocate.successors() + swap.successors()
    }
}


class TspHillClimbingSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : HillClimbingSolver(problem, solution) {
    override fun successors(): List<Successor> {
        return TspOperator(problem, solution).successors()
    }
}



class RelocateSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : HillClimbingSolver(problem, solution) {
    override fun successors(): List<Successor> {
        return RelocateOperator(problem, solution).successors()
    }
}



class SwapSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : HillClimbingSolver(problem, solution) {
    override fun successors(): List<Successor> {
        return SwapOperator(problem, solution).successors()
    }
}
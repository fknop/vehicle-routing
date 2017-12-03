import localsearch.SimulatedAnnealingSolver
import localsearch.Neighbor
import vrp.*
import vrp.operators.*


class VehicleRoutingSolverSA(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, seed: Long = 0): SimulatedAnnealingSolver(problem, solution, seed) {
    val relocate = RelocateSolverSA(problem, solution)
    val tsp = TSPSolverSA(problem, solution)
    val swap = SwapSolverSA(problem, solution)

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, seed: Long = 0): this(problem, strategy.getInitialSolution(problem), seed)

    override fun successors(): List<Neighbor> {
        return relocate.successors() + tsp.successors() + swap.successors()
    }
}

class SwapSolverSA(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : SimulatedAnnealingSolver(problem, solution) {
    override fun successors(): List<Neighbor> {
        return SwapOperator(problem, solution).neighborhood()
    }
}

class TSPSolverSA(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : SimulatedAnnealingSolver(problem, solution) {
    override fun successors(): List<Neighbor> {
        return TwoOptOperator(problem, solution).neighborhood()
    }
}


class RelocateSolverSA(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : SimulatedAnnealingSolver(problem, solution) {
    override fun successors(): List<Neighbor> {
        return RelocateOperator(problem, solution).neighborhood()
    }
}


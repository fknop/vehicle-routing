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
               SwapOperator(problem, solution).neighborhood() +
               IntraRelocateOperator(problem, solution).neighborhood() +
               InterTwoOptOperator(problem, solution).neighborhood()
    }
}


class SequentialSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    val kopt = KOptSolver(problem, solution, heuristic)
    val relocate = RelocateSolver(problem, solution, heuristic)
    val intra = IntraRelocateSolver(problem, solution, heuristic)
    val inter = InterTwoOptSolver(problem, solution, heuristic)

    override fun neighborhood(): List<Neighbor> {
        throw NotImplementedError()
    }

    override fun optimize(): Boolean {
        var improved: Boolean

        do {
            improved = inter.optimize() || kopt.optimize() || relocate.optimize() || kopt.optimize()
        } while(improved)

        return improved
    }
}



class KOptSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
        return  KOptOperator(problem, solution).neighborhood()
    }
}

class InterTwoOptSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
        return  InterTwoOptOperator(problem, solution).neighborhood()
    }
}

class RelocateSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
        return  RelocateOperator(problem, solution).neighborhood()
    }
}

class SwapSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
        return  SwapOperator(problem, solution).neighborhood()
    }
}

class IntraRelocateSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution, heuristic: Heuristic): LSSolver(problem, solution, heuristic) {

    constructor(problem: VehicleRoutingProblem, strategy: InitialSolutionStrategy, heuristic: Heuristic): this(problem, strategy.getInitialSolution(problem), heuristic)

    override fun neighborhood(): List<Neighbor> {
        return  IntraRelocateOperator(problem, solution).neighborhood()
    }
}
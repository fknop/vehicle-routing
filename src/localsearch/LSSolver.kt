package localsearch

import localsearch.heuristic.Heuristic
import localsearch.heuristic.HillClimbingHeuristic
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

abstract class LSSolver(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution, val heuristic: Heuristic) {

    open fun optimize(): Boolean {
        var improved: Boolean
        var hasImproved: Boolean = false

        do {
            improved = next()
            if (improved) {
                hasImproved = true
            }
        } while(improved)

        return hasImproved
    }

    fun next(): Boolean {
        val neighbors = neighborhood().filter { (it.delta + solution.totalDistance) !in solution.tabu }
        val next = heuristic.next(neighbors)

        if (next != null) {
            next()
            solution.tabu += solution.totalDistance
        }

        return next != null
    }

    abstract fun neighborhood(): List<Neighbor>
}
package localsearch

import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

abstract class HillClimbingSolver(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution) {

    fun optimize(): Boolean {
        var improved: Boolean

        do {
            improved = next()
        } while(improved)

        return improved
    }

    fun next(): Boolean {
        val neighbors = neighborhood()
        if (neighbors.isNotEmpty()) {
            val best = neighborhood().minBy { it.delta }!!

            if (best.delta < 0) {
                best()
            }

            return best.delta < 0
        }

        return false
    }

    abstract fun neighborhood(): List<Neighbor>
}
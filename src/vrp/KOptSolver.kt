package localsearch

import vrp.VehicleRoute
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution
import vrp.operators.deltaTwoOpt
import vrp.operators.twoOpt
import kotlin.math.abs

class KOptSolver(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution) {

    fun optimize(): Boolean {
        var improved: Boolean

        do {
            improved = next()
        } while(improved)

        return improved
    }

    fun next(): Boolean {
        var found = false
        for (route in solution.routes) {
            for (index in 0 until route.size - 1) {
                if (kOpt(route, index)) {
                    found = true
                }
            }
        }

        return found
    }

    fun kOpt(route: VehicleRoute, from: Int, K: Int = 5): Boolean {
        var cDelta = 0
        var bestDelta = Int.MAX_VALUE
        var previous = -1
        var k = 0

        do {
            k += 1
            var delta = 0
            var best = Int.MAX_VALUE
            var bestTo = -1
            for (i in 0 until route.size - 1) {
                if (abs(from - i) > 1 && i != previous) {
                    delta = route.deltaTwoOpt(from, i)
                    if (best > delta) {
                        bestTo = i
                        best = delta
                    }
                }
            }

            previous = bestTo

            cDelta += best
            if (cDelta < bestDelta) {
                bestDelta = cDelta

                if (cDelta < 0) {
                    route.twoOpt(from, bestTo)
                }
            }

        } while(cDelta < 0 && k < K)

        return bestDelta < 0
    }
}
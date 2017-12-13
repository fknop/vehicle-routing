package vrp.operators

import localsearch.Neighbor
import localsearch.Operator
import vrp.VehicleRoute
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution
import vrp.VehicleRoutingSolver
import kotlin.math.abs

class KOptOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun neighborhood(): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()
        for (route in solution) {
            for (index in 0 until route.size - 1) {
                neighbors += kOpt(route, index)
            }
        }

        return neighbors
    }

    fun kOpt(route: VehicleRoute, from: Int): Neighbor {
        var cDelta = 0
        var bestDelta = Int.MAX_VALUE
        var previous = -1
        var k = 0
        val K = 5

        val operations = mutableListOf<() -> Unit>()
        var current = route.copy()

        do {
            k += 1
            var delta = 0
            var best = Int.MAX_VALUE
            var bestTo = -1
            for (i in 0 until route.size - 1) {
                if (abs(from - i) > 1 && i != previous) {
                    delta = current.deltaTwoOpt(from, i)
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
                    current.twoOpt(from, bestTo)
                    operations += {
                        route.twoOpt(from, bestTo)
                    }
                }
            }

        } while(cDelta < 0 && k < K)

        return Neighbor({ bestDelta }, {
            operations.forEach { it() }
        })
    }
}
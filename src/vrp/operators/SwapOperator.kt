package vrp.operators

import localsearch.Operator
import localsearch.Neighbor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class SwapOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun neighborhood(): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()

        for (r in 0 until solution.size) {
            val routeFrom = solution[r]
            for (k in r + 1 until solution.size) {
                val routeTo = solution[k]
                for (i in 1 until routeFrom.size - 1) {
                    for (j in 1 until (routeTo.size - 1)) {

                        if (problem.capacity < routeFrom[i].demand + routeTo.totalDemand - routeTo[j].demand ||
                                problem.capacity < routeTo[j].demand + routeFrom.totalDemand - routeFrom[i].demand) {
                            continue
                        }

                        neighbors += Neighbor({ swapDelta(routeFrom, i, routeTo, j) }, { swap(routeFrom, i, routeTo, j) })
                    }
                }
            }
        }

        return neighbors
    }
}
package vrp.operators

import localsearch.Operator
import localsearch.Successor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class SwapOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun successors(): List<Successor> {
        val successors = mutableListOf<Successor>()
        for (routeFrom in solution) {
            for (routeTo in solution) {
                if (routeFrom != routeTo) {
                    for (i in 1 until routeFrom.size - 1) {
                        for (j in 1 until (routeTo.size - 1)) {

                            if (problem.capacity < routeFrom[i].demand + routeTo.totalDemand - routeTo[j].demand ||
                                    problem.capacity < routeTo[j].demand + routeFrom.totalDemand - routeFrom[i].demand) {
                                continue
                            }

                            successors += Successor({ swapDelta(routeFrom, i, routeTo, j) }, { swap(routeFrom, i, routeTo, j) })
                        }
                    }
                }
            }
        }

        return successors
    }
}
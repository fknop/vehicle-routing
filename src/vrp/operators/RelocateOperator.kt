package vrp.operators

import localsearch.Operator
import localsearch.Successor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class RelocateOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun successors(): List<Successor> {
        val successors = mutableListOf<Successor>()

        for (routeFrom in solution.routes) {
            for (routeTo in solution.routes) {
                if (routeFrom != routeTo) {
                    for (i in 1 until routeFrom.size - 1) {
                        for (j in 1 until routeTo.size) {

                            if (problem.capacity < routeFrom[i].demand + routeTo.totalDemand) {
                                continue
                            }

                            successors += Successor({ relocateDelta(routeFrom, i, routeTo, j) }, { relocate(routeFrom, i, routeTo, j) })
                        }
                    }
                }
            }
        }

        return successors
    }
}
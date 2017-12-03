package vrp.operators

import localsearch.Operator
import localsearch.Neighbor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class InterTwoOptOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun neighborhood(): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()

        for (i in 0 until solution.size) {
            val routeLeft = solution[i]
            for (j in i + 1 until solution.size) {
                val routeRight = solution[j]
                if (routeLeft != routeRight) {
                    for (left in 1 until routeLeft.size - 1) {
                        for (right in 1 until routeRight.size - 1) {

                            // demand left + 1 -> n goes to route2
                            // demand right -1 -> 0 goes to route 1
                            val demandToRight = routeLeft.customers.subList(left + 1, routeLeft.size).sumBy { it.demand }
                            val demandToLeft = routeRight.customers.subList(0, right + 1).sumBy { it.demand }


                            if (problem.capacity < routeLeft.totalDemand + demandToLeft - demandToRight) {
                                continue
                            }

                            if (problem.capacity < routeRight.totalDemand + demandToRight - demandToLeft) {
                                continue
                            }


                            neighbors += Neighbor(
                                    { interDeltaTwoOpt(problem.distances, routeLeft, left, routeRight, right) },
                                    { interTwoOpt(routeLeft, left, routeRight, right) }
                            )
                        }
                    }
                }
            }
        }

        return neighbors
    }
}
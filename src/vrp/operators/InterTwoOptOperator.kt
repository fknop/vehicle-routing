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
                    for (left in 1 until routeLeft.size - 2) {
                        for (right in 1 until routeRight.size - 2) {
                            // left + 1 goes to routeRight
                            // right goes to routeLeft

                            if (problem.capacity < routeLeft.totalDemand + routeRight[right].demand - routeLeft[left + 1].demand) {
                                continue
                            }

                            if (problem.capacity < routeRight.totalDemand + routeLeft[left + 1].demand - routeRight[right].demand) {
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
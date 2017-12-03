package vrp.operators

import localsearch.Operator
import localsearch.Neighbor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class TwoOptOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun neighborhood(): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()

        for (route in solution) {
            for (left in 0 until route.size - 1) {
                for (right in left + 1 until route.size - 1) {
                    neighbors += Neighbor({ route.deltaTwoOpt(left, right) }, { route.twoOpt(left, right) })
                }
            }
        }

        return neighbors
    }
}
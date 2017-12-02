package vrp.operators

import localsearch.Operator
import localsearch.Successor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class TspOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun successors(): List<Successor> {
        val successors = mutableListOf<Successor>()

        for (route in solution) {
            for (left in 0 until route.size - 1) {
                for (right in left + 1 until route.size - 1) {
                    successors += Successor({ route.deltaTwoOpt(left, right) }, { route.twoOpt(left, right) })
                }
            }
        }

        return successors
    }
}
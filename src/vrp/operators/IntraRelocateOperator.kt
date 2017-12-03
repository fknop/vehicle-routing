package vrp.operators

import localsearch.Operator
import localsearch.Neighbor
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution

class IntraRelocateOperator(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution): Operator {
    override fun neighborhood(): List<Neighbor> {
        val neighbors = mutableListOf<Neighbor>()

        for (route in solution) {
            for (i in 1 until route.size - 1) {
                for (j in i + 2 until route.size) {
                    neighbors += Neighbor({ intraRelocateDelta(problem.distances, route, i, j) }, { intraRelocate(route, i, j) })
                }
            }
        }

        return neighbors
    }
}
// Relocate customers until you can't get any better
// two opts on every route
// relocate again
// two opt..
// etc, until improved = false


abstract class LocalSearchSolver(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution) {
    fun optimize(): Boolean {
        var improved: Boolean

        do {
            improved = iteration()
        } while(improved)

        return improved
    }



    abstract fun iteration(): Boolean
}

class VehicleRoutingSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution): LocalSearchSolver(problem, solution) {

    val relocate = RelocateSolver(problem, solution)
    val tsp = TSPSolver(problem, solution)

    override fun iteration(): Boolean {
        return relocate.optimize() || tsp.optimize()
    }

}

class TSPSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : LocalSearchSolver(problem, solution) {
    override fun iteration(): Boolean {
        var bestRoute: VehicleRoute? = null
        var bestDelta = 0
        var bestLeft = 0
        var bestRight = 0
        for (route in solution.routes) {
            for (left in 0 until route.customers.size - 1) {
                for (right in left + 1 until route.customers.size - 1) {
                    val delta = route.deltaTwoOpt(left, right)
                    if (delta < bestDelta) {
                        bestRoute = route
                        bestDelta = delta
                        bestLeft = left
                        bestRight = right
                    }
                }
            }
        }

        if (bestRoute != null) {
            bestRoute.twoOpt(bestLeft, bestRight)
        }

        return bestDelta < 0
    }

}

class RelocateSolver(problem: VehicleRoutingProblem, solution: VehicleRoutingSolution) : LocalSearchSolver(problem, solution) {
    override fun iteration(): Boolean {

        var bestRouteFrom: VehicleRoute? = null
        var bestRouteTo: VehicleRoute? = null
        var bestDelta = 0
        var bestCustomerFrom = 0
        var bestCustomerTo = 0

        for (routeFrom in solution.routes) {
            for (routeTo in solution.routes) {
                if (routeFrom != routeTo) {
                    for (i in 1 until routeFrom.customers.size - 1) {
                        for (j in 1 until routeTo.customers.size) {
                            val delta = relocateDelta(routeFrom, i, routeTo, j)
                            if (delta < bestDelta) {
                                bestDelta = delta
                                bestRouteFrom = routeFrom
                                bestRouteTo = routeTo
                                bestCustomerFrom = i
                                bestCustomerTo = j
                            }
                        }
                    }
                }
            }
        }

        if (bestRouteFrom != null && bestRouteTo != null) {
            relocate(bestRouteFrom, bestCustomerFrom, bestRouteTo, bestCustomerTo)
        }

        return bestDelta < 0
    }


    // Relocate a customer
    fun relocate(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int) {
        val customerToRelocate = route.customers[i]
        route2.addCustomer(customerToRelocate, j)
        route.removeCustomer(i)
    }


    /**
     * A -> B -> C
     * D -> E
     * A -> C
     * D -> B -> E
     */
    fun relocateDelta(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int): Int {

        assert(i < route.customers.size - 1)


        // Check if the demand is not violated first
        if (problem.capacity < route.customers[i].demand + route2.totalDemand) {
            return 0
        }

        val previousDistance = route.totalDistance + route2.totalDistance

        // Apply relocate
        route2.addCustomer(route.customers[i], j)
        route.removeCustomer(i)

        val newDistance = route.totalDistance + route2.totalDistance

        // Undo relocate
        route.addCustomer(route2.customers[j], i)
        route2.removeCustomer(j)

        return newDistance - previousDistance
    }
}
fun sweep(problem: VehicleRoutingProblem): List<List<Customer>> {
    val clusters = mutableListOf<List<Customer>>()

    val furthest = problem.distances[0].max()!!
    var angle = 0

    while (angle <= 360) {

        angle += 1
    }

    return clusters
}

fun simpleInitialSolution(problem: VehicleRoutingProblem): VehicleRoutingSolution {
    val customers = problem.customers
                        .subList(1, problem.customers.size) // remove warehouse
                        .mapIndexed { i, c -> Pair(i, c) }
                        .sortedByDescending { problem.distances[0][it.first] }
                        .map { it.second }


    val routes = MutableList(problem.k) {
        val route = VehicleRoute(problem.capacity, problem.distances)
        route.addCustomer(problem.warehouse)
        route.addCustomer(problem.warehouse)
        route
    }

    var currentCustomer = 0
    var servedCustomers = 0

    while (servedCustomers < customers.size && currentCustomer < customers.size) {
        val customer = customers[currentCustomer]

        routes@ for (route in routes) {
            if (route.addCustomer(customer, route.customers.size - 1)) {
                servedCustomers += 1
                break@routes
            }
        }

        currentCustomer += 1
    }

    if (servedCustomers != customers.size) {
        throw Exception("Could not assign customers with this method")
    }


    return VehicleRoutingSolution(routes)
}


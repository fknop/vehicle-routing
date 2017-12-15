package vrp

import java.util.*
import kotlin.math.*

fun radians(angle: Double): Double {
    return angle / 180.0 * PI
}


interface InitialSolutionStrategy {
    fun getInitialSolution(problem: VehicleRoutingProblem): VehicleRoutingSolution
}

class SweepStrategy(val seed: Long = 0L) : InitialSolutionStrategy {
    override fun getInitialSolution(problem: VehicleRoutingProblem): VehicleRoutingSolution {

        val customers = problem.customers.subList(1, problem.customers.size) // remove warehouse

        val routes = MutableList(problem.k) {
            val route = VehicleRoute(problem.capacity, problem.distances)
            route.addCustomer(problem.warehouse)
            route.addCustomer(problem.warehouse)
            route
        }

        var currentRoute = 0
        var angle = 0.0
        val servedCustomers = mutableSetOf<Int>()

        while (angle < 360.0 && servedCustomers.size != customers.size) {
            val slope = tan(radians(angle))
            var add = false

            customers.filterNot {
                it.index in servedCustomers
            }.forEach {
                val (_, _, x, y) = it
                val above = y > (x * slope)
                val below = y < (x * slope)

                if (angle == .0) {
                    if (x > .0 && y == .0) {
                        add = true
                    }
                }
                else if (angle > .0 && angle < 90.0) {
                    if (x > .0 && y > .0 && below) {
                        add = true
                    }
                }
                else if (angle == 90.0) {
                    if (x == .0 && y >= 0) {
                        add = true
                    }
                }
                else if (angle > 90.0 && angle < 180.0) {
                    if (x <= .0 && y >= .0 && above) {
                        add = true
                    }
                }
                else if (angle == 180.0) {
                    if (x <= .0 && y == .0) {
                        add = true
                    }
                }
                else if (angle > 180.0 && angle < 270.0) {
                    if (x <= .0 && y <= .0 && above) {
                        add = true
                    }
                }
                else if (angle == 270.0) {
                    if (x == .0 && y <= .0) {
                        add = true
                    }
                }
                else if (angle > 270.0 && angle < 360) {
                    if (x >= .0 && y <= .0 && below) {
                        add = true
                    }
                }

                if (add) {
                    val r = routes[currentRoute]
                    if (r.addCustomer(it, r.customers.size - 1)) {
                            servedCustomers += it.index
                    }
                    else {
                        currentRoute++
                        for (i in currentRoute - 1 downTo 0) {
                            val route = routes[i]
                            if (route.addCustomer(it, route.customers.size - 1)) {
                                servedCustomers += it.index
                                break
                            }

                        }
                    }

//                    for (route in routes) {
//                    }
                }
            }


            angle += 1
        }

        assert(customers.size == servedCustomers.size)

        return VehicleRoutingSolution(problem, routes, seed = seed)
    }
}

class SimpleInitialStrategy(val seed: Long = 0) : InitialSolutionStrategy {
    override fun getInitialSolution(problem: VehicleRoutingProblem): VehicleRoutingSolution {
        val customers = problem.customers
                .subList(1, problem.customers.size) // remove warehouse

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


        return VehicleRoutingSolution(problem, routes, seed)
    }
}

class RandomInitialStrategy(val seed: Long = 0) : InitialSolutionStrategy {
    override fun getInitialSolution(problem: VehicleRoutingProblem): VehicleRoutingSolution {
        val customers = problem.customers
                .subList(1, problem.customers.size) // remove warehouse
                .shuffled(Random(seed))

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


        return VehicleRoutingSolution(problem = problem, routes = routes, seed = seed)
    }
}


package vrp

import localsearch.Neighbor
import vrp.operators.*
import java.util.*

class VehicleRoutingSolution(val problem: VehicleRoutingProblem, val routes: MutableList<VehicleRoute>, val seed: Long = 0L) {

    val rand = Random(seed)
    var copies = 0

    val totalDistance: Int
        get() = routes.sumBy { it.totalDistance }

    val size: Int
        get() = routes.size

    inline operator fun get(index: Int): VehicleRoute {
        return routes[index]
    }

    inline operator fun iterator(): Iterator<VehicleRoute> {
        return routes.iterator()
    }

    inline fun forEach(block: (VehicleRoute) -> Unit) {
        var index = 0
        while (index < size) {
            block(get(index++))
        }
    }

    fun copy(): VehicleRoutingSolution {
        val routes = mutableListOf<VehicleRoute>()
        forEach {
            routes += it.copy()
        }

        copies += 1
        return VehicleRoutingSolution(problem, routes, seed + copies)
    }

    fun perturb(hard: Boolean = false, reallyhard: Boolean = false, reallyreallyhard: Boolean = false) {

        val swaps =
                if (reallyreallyhard) 10
                else if (reallyhard) 5
                else if (hard) 2
                else 1

        for (i in 0 until swaps) {
            var neighbors = InterTwoOptOperator(problem, this).neighborhood()

            val r = rand.nextInt(neighbors.size)
            neighbors[r]()
        }
    }

    fun check(): Boolean {
        var valid = true
        this.forEach {
            var checkedDistance = 0
            for (i in 1 until it.customers.size) {
                val previousIndex = it.customers[i - 1].index
                val index = it.customers[i].index
                checkedDistance += (problem.distances[previousIndex][index])
            }

            valid = valid && (it.totalDemand <= problem.capacity)
            valid = valid && (it.totalDistance == checkedDistance)
        }

        return valid
    }

    override fun toString(): String {
        return buildString {
            appendln(totalDistance)
            this@VehicleRoutingSolution.forEach { route ->
                route.forEach {
                    append(it.index).append(" ")
                }
                appendln()
            }
        }
    }
}
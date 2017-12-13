package vrp

import vrp.operators.*
import java.util.*

class VehicleRoutingSolution(val problem: VehicleRoutingProblem, val routes: MutableList<VehicleRoute>, val seed: Long = 0L, val tabu: TabuList = TabuList(keep = 30)) {

    companion object {
        fun bestOf(first: VehicleRoutingSolution, second: VehicleRoutingSolution): VehicleRoutingSolution {
            return if (first.totalDistance < second.totalDistance) first else second
        }
    }

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
        return VehicleRoutingSolution(problem, routes, seed + copies, tabu = tabu)
    }

    fun perturb(swaps: Int = 1) {

        for (i in 0 until swaps) {
            val neighbors = (VehicleRoutingSolver(problem, this).neighborhood())
                            .filter {
                                val new = this.totalDistance + it.delta
                                new !in tabu
                            }


            val r = rand.nextInt(neighbors.size)
            neighbors[r]()

            tabu += this.totalDistance
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

        valid = valid && problem.k == this.size
        valid = valid && problem.customers.size - 1 == this.routes.sumBy { it.size - 2 }

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
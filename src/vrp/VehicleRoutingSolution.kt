package vrp

class VehicleRoutingSolution(val routes: MutableList<VehicleRoute>) {

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

    fun check(distances: Array<Array<Int>>, capacity: Int): Boolean {
        var valid = true
        this.forEach {
            var checkedDistance = 0
            for (i in 1 until it.customers.size) {
                val previousIndex = it.customers[i - 1].index
                val index = it.customers[i].index
                checkedDistance += (distances[previousIndex][index])
            }

            valid = valid && (it.totalDemand <= capacity)
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
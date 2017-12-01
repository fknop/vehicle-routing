class VehicleRoutingSolution(val routes: MutableList<VehicleRoute>) {

    fun totalDistance(): Int {
        return routes.sumBy { it.totalDistance }
    }

    fun check(distances: Array<Array<Int>>, capacity: Int): Boolean {
        var valid = true
        routes.forEach {
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
            appendln(totalDistance())
            routes.forEach { route ->
                route.customers.forEach {
                    append(it.index).append(" ")
                }
                appendln()
            }
        }
    }
}
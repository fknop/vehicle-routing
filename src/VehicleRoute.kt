class VehicleRoute(val capacity: Int, val distances: Array<Array<Int>>) {

    val customers = mutableListOf<Customer>()
    var totalDemand = 0
    var totalDistance = 0

    fun addCustomer(customer: Customer): Boolean {

        if (capacity < totalDemand + customer.demand) {
            return false
        }

        totalDemand += customer.demand
        customers.add(customer)

        if (customers.size > 1) {
            val previousCustomer = customers[customers.size - 2]
            val distance = distances[previousCustomer.index][customer.index]
            totalDistance += distance
        }

        return true
    }

    fun addCustomer(customer: Customer, i: Int): Boolean {

        assert(i > 0)
        assert(i <= customers.size - 1)

        if (capacity < totalDemand + customer.demand) {
            return false
        }

        totalDemand += customer.demand
        customers.add(i, customer)

        if (customers.size > 1) {
            val previousCustomer = customers[i - 1]
            val nextCustomer = customers[i + 1]
            val previousDistance = distances[previousCustomer.index][nextCustomer.index]
            val newDistance = distances[previousCustomer.index][customer.index] + distances[customer.index][nextCustomer.index]

            totalDistance += (newDistance - previousDistance)
        }

        return true
    }

    fun twoOpt(i: Int, j: Int) {
        val left = minOf(i, j)
        val right = maxOf(i, j)

        assert(left >= 0)
        assert(right < customers.size)

        totalDistance += deltaTwoOpt(i, j)

        val n = (right - left + 1) / 2
        var k = 0
        while (k < n) {
            // Swap edges
            val tmp = customers[left + 1 + k]
            customers[left + 1 + k] = customers[right - k]
            customers[right - k] = tmp
            ++k
        }
    }

    fun deltaTwoOpt(i: Int, j: Int): Int {
        val left = minOf(i, j)
        val right = maxOf(i, j)

        assert(left >= 0)
        assert(right < customers.size - 1)

        val distanceLeft = distances[customers[left].index][customers[left + 1].index]
        val distanceRight = distances[customers[right].index][customers[right + 1].index]

        val newDistanceLeft = distances[customers[left].index][customers[right].index]
        val newDistanceRight = distances[customers[left + 1].index][customers[right + 1].index]


        return newDistanceLeft + newDistanceRight - distanceLeft - distanceRight
    }

    fun removeCustomer(i: Int) {

        assert(i > 0)
        assert(i < customers.size - 1)

        val previousDistance = distances[customers[i - 1].index][customers[i].index] + distances[customers[i].index][customers[i + 1].index]
        val newDistance = distances[customers[i - 1].index][customers[i + 1].index]

        totalDemand -= customers[i].demand
        totalDistance += (newDistance - previousDistance)

        customers.removeAt(i)
    }
}

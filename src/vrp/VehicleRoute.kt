package vrp

import vrp.Customer

class VehicleRoute(val capacity: Int, val distances: Array<Array<Int>>) {

    val customers = mutableListOf<Customer>()
    var totalDemand = 0
    var totalDistance = 0

    val size: Int
        get() = customers.size

    inline operator fun get(index: Int): Customer {
        return customers[index]
    }

    inline operator fun iterator(): Iterator<Customer> {
        return customers.iterator()
    }

    inline fun forEach(block: (Customer) -> Unit) {
        var index = 0
        while (index < size) {
            block(get(index++))
        }
    }

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

package vrp.operators

import printMultiple
import vrp.Customer
import vrp.VehicleRoute

/**
 * A -> B -> C
 * D -> E
 * A -> C
 * D -> B -> E
 */
fun relocateDelta(routeFrom: VehicleRoute, i: Int, routeTo: VehicleRoute, j: Int): Int {

    assert(i < routeFrom.customers.size - 1)

    val previousDistance = routeFrom.totalDistance + routeTo.totalDistance

    // Apply relocate
    routeTo.addCustomer(routeFrom.customers[i], j)
    routeFrom.removeCustomer(i)

    val newDistance = routeFrom.totalDistance + routeTo.totalDistance

    // Undo relocate
    routeFrom.addCustomer(routeTo[j], i)
    routeTo.removeCustomer(j)

    return newDistance - previousDistance
}

fun relocate(routeFrom: VehicleRoute, i: Int, routeTo: VehicleRoute, j: Int) {
    val customerToRelocate = routeFrom[i]
    routeTo.addCustomer(customerToRelocate, j)
    routeFrom.removeCustomer(i)
}

// W -> A -> B -> C -> W
// intraRelocateDelta(1, 3)
// W -> B -> A -> C -> W
fun intraRelocateDelta(distances: Array<Array<Int>>, route: VehicleRoute, i: Int, j: Int): Int {
    assert(i < route.size - 1)
    assert(i > 0)
    assert(j > 0)
    assert(j < route.size)
    assert(i < j)


    val lir = distances[route[i - 1].index][route[i].index] + distances[route[i].index][route[i + 1].index]
    val lr = distances[route[i - 1].index][route[i + 1].index]

    val ljr = distances[route[j - 1].index][route[i].index] + distances[route[i].index][route[j].index]
    val lr2 = distances[route[j - 1].index][route[j].index]

    val newDistance = lr + ljr
    val previousDistance = lr2 + lir

    return newDistance - previousDistance
}

fun intraRelocate(route: VehicleRoute, i: Int, j: Int) {

    assert(i < j)

    val customer = route.removeCustomer(i)
    val t = route.addCustomer(customer, j - 1) // Because remove removed one

    assert(t)
}


// Relocate a customer
fun swap(routeFrom: VehicleRoute, i: Int, routeTo: VehicleRoute, j: Int) {

    val sizeA = routeFrom.customers.size
    val sizeB = routeTo.customers.size
    val customerToRelocate = routeFrom.customers[i]
    val customerToRelocate2 = routeTo.customers[j]

    routeFrom.removeCustomer(i)
    routeFrom.addCustomer(customerToRelocate2, i)

    routeTo.removeCustomer(j)
    routeTo.addCustomer(customerToRelocate, j)

    assert(sizeA == routeFrom.customers.size)
    assert(sizeB == routeTo.customers.size)
}


/**
 * A -> B -> C
 * D -> E
 * A -> C
 * D -> B -> E
 */
fun swapDelta(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int): Int {

    assert(i < route.customers.size - 1)
    assert(j < route2.customers.size - 1)



    val previousDistance = route.totalDistance + route2.totalDistance

    val sizeA = route.customers.size
    val sizeB = route2.customers.size

    // Apply vrp.operators.swap
    swap(route, i, route2, j)

    val newDistance = route.totalDistance + route2.totalDistance

    // Undo vrp.operators.swap
    swap(route, i, route2, j)

    assert(sizeA == route.customers.size)
    assert(sizeB == route2.customers.size)


    return newDistance - previousDistance
}

fun VehicleRoute.twoOpt(i: Int, j: Int) {
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

fun VehicleRoute.deltaTwoOpt(i: Int, j: Int): Int {
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

fun interDeltaTwoOpt(distances: Array<Array<Int>>, routeFrom: VehicleRoute, i: Int, routeTo: VehicleRoute, j: Int): Int {
    val left = i
    val right = j

    val distanceLeft = distances[routeFrom[left].index][routeFrom[left + 1].index]
    val distanceRight = distances[routeTo[right].index][routeTo[right + 1].index]

    val newDistanceLeft = distances[routeFrom[left].index][routeTo[right].index]
    val newDistanceRight = distances[routeFrom[left + 1].index][routeTo[right + 1].index]

    return newDistanceLeft + newDistanceRight - distanceLeft - distanceRight
}


fun interTwoOpt(routeLeft: VehicleRoute, i: Int, routeRight: VehicleRoute, j: Int) {


    // Remove left + 1 -> n
    // left + 1 -> n becomes right -> n
    //

    val customersLeft = mutableListOf<Customer>()
    for (left in 0..i) {
        customersLeft += routeLeft[left]
    }

    for (right in j..0) {
        customersLeft += routeRight[right]
    }

    val customersRight = mutableListOf<Customer>()

    for (left in routeLeft.size - 1..i + 1) {
        customersRight += routeLeft[left]
    }

    for (right in j + 1..routeRight.size) {
        customersRight += routeRight[right]
    }


    // TO BE CONTINUED.

}

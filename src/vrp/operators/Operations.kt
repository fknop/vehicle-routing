package vrp.operators

import vrp.VehicleRoute

/**
 * A -> B -> C
 * D -> E
 * A -> C
 * D -> B -> E
 */
fun relocateDelta(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int): Int {

    assert(i < route.customers.size - 1)

    val previousDistance = route.totalDistance + route2.totalDistance

    // Apply vrp.operators.relocate
    route2.addCustomer(route.customers[i], j)
    route.removeCustomer(i)

    val newDistance = route.totalDistance + route2.totalDistance

    // Undo vrp.operators.relocate
    route.addCustomer(route2.customers[j], i)
    route2.removeCustomer(j)

    return newDistance - previousDistance
}

fun relocate(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int) {
    val customerToRelocate = route.customers[i]
    route2.addCustomer(customerToRelocate, j)
    route.removeCustomer(i)
}


// Relocate a customer
fun swap(route: VehicleRoute, i: Int, route2: VehicleRoute, j: Int) {

    val sizeA = route.customers.size
    val sizeB = route2.customers.size
    val customerToRelocate = route.customers[i]
    val customerToRelocate2 = route2.customers[j]

    route.removeCustomer(i)
    route.addCustomer(customerToRelocate2, i)

    route2.removeCustomer(j)
    route2.addCustomer(customerToRelocate, j)

    assert(sizeA == route.customers.size)
    assert(sizeB == route2.customers.size)
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
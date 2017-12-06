package vrp.operators

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

    val routeToCopy = routeTo.copy()
    val routeFromCopy = routeFrom.copy()

    // Apply relocate
    routeToCopy.addCustomer(routeFromCopy.customers[i], j)
    routeFromCopy.removeCustomer(i)

    val newDistance = routeFromCopy.totalDistance + routeToCopy.totalDistance

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
fun swapDelta(routeFrom: VehicleRoute, i: Int, routeTo: VehicleRoute, j: Int): Int {

    assert(i < routeFrom.customers.size - 1)
    assert(j < routeTo.customers.size - 1)


    val previousDistance = routeFrom.totalDistance + routeTo.totalDistance


    val sizeA = routeFrom.customers.size
    val sizeB = routeTo.customers.size

    val routeFromCopy = routeFrom.copy()
    val routeToCopy = routeTo.copy()

    // Apply vrp.operators.swap
    swap(routeFromCopy, i, routeToCopy, j)

    val newDistance = routeFromCopy.totalDistance + routeToCopy.totalDistance

    assert(sizeA == routeFrom.customers.size)
    assert(sizeB == routeTo.customers.size)


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

fun interDeltaTwoOpt(routeLeft: VehicleRoute, i: Int, routeRight: VehicleRoute, j: Int): Int {

    val previousDistance = routeLeft.totalDistance + routeRight.totalDistance
    val routeLeftCopy = routeLeft.copy()
    val routeRightCopy = routeRight.copy()

    // Apply
    interTwoOpt(routeLeftCopy, i, routeRightCopy, j)

    val newDistance = routeLeftCopy.totalDistance + routeRightCopy.totalDistance

    return newDistance - previousDistance
}


fun interTwoOpt(routeLeft: VehicleRoute, i: Int, routeRight: VehicleRoute, j: Int) {

    // Remove left + 1 -> n
    // left + 1 -> n becomes right -> n
    val copyLeft = routeLeft.copy()
    val copyRight = routeRight.copy()

    for (left in i+1 until copyLeft.size - 1) {
        routeLeft.removeCustomer(i + 1)
    }

    for (right in j downTo 1) {
        routeRight.removeCustomer(1)
        routeLeft.addCustomer(copyRight[right], routeLeft.size - 1)
    }


    for (left in i+1 until copyLeft.size - 1) {
        routeRight.addCustomer(copyLeft[left], 1)
    }
}

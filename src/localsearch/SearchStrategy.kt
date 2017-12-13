package localsearch

import vrp.VehicleRoutingSolution

interface SearchStrategy {
    fun search(): Pair<Long, VehicleRoutingSolution>
}
package vrp

import java.util.*

class ILSSearch(
        val problem: VehicleRoutingProblem,
        val restarts: Int = 0,
        val maxstuck: Int = 50,
        val maxtime: Long = 20000, // 20 seconds
        val randomStart: Boolean = false
    ) {

    private fun ils(restart: Int = 0): VehicleRoutingSolution {
        var solver = VehicleRoutingSolver(problem, if (randomStart) RandomInitialStrategy(restarts.toLong()) else SweepStrategy())
        var best: VehicleRoutingSolution? = null
        var stuck = 0
        val rand = Random(restarts.toLong())

        val startTime = System.currentTimeMillis()
        var elapsed = 0L

        while (stuck < maxstuck && elapsed <= maxtime) {

            if (best != null) {
                val solution = best.copy()
                solution.perturb(swaps = 2 + rand.nextInt(5))

                solver = VehicleRoutingSolver(problem, solution)
            }

            solver.optimize()

            if (best == null || best.totalDistance > solver.solution.totalDistance) {
                best = solver.solution.copy()
                stuck = 0
            }
            else {
                stuck += 1
            }

            elapsed = System.currentTimeMillis() - startTime
        }


        if (restart < restarts) {
            return VehicleRoutingSolution.bestOf(best!!, ils(restart = restart + 1))
        }

        return best!!
    }

    fun search(): VehicleRoutingSolution {
        return ils()
    }
}
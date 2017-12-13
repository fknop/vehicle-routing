package vrp.search

import localsearch.KOptSolver
import localsearch.SearchStrategy
import vrp.*
import java.util.*
import kotlin.system.measureTimeMillis

class ILSSearch(
        val problem: VehicleRoutingProblem,
        val restarts: Int = 0,
        val maxstuck: Int = 50,
        val maxtime: Long = 20000, // 20 seconds
        val randomStart: Boolean = false
    ): SearchStrategy {

    private fun ils(restart: Int = 0, random: Boolean = randomStart): VehicleRoutingSolution {
        var solver = VehicleRoutingSolver(problem, if (random) RandomInitialStrategy(restart.toLong()) else SweepStrategy(restart.toLong()))
        var best: VehicleRoutingSolution? = null
        var stuck = 0
        val rand = Random(restart.toLong())

        val startTime = System.currentTimeMillis()
        var elapsed = 0L

        while (stuck < maxstuck && elapsed <= maxtime) {

            if (best != null) {
                val solution = best.copy()
                solution.perturb(swaps = 2 + rand.nextInt(4))
                solver = VehicleRoutingSolver(problem, solution)
            }

            solver.optimize()

            val koptSolver = KOptSolver(problem, solver.solution)
            koptSolver.optimize()

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
            val other = ils(restart = restart + 1, random = true)
            return VehicleRoutingSolution.bestOf(best!!, other)
        }

        return best!!
    }

    override fun search(): Pair<Long, VehicleRoutingSolution> {
        lateinit var solution: VehicleRoutingSolution
        val time = measureTimeMillis {
            solution = ils()
        }

        return Pair(time, solution)
    }
}
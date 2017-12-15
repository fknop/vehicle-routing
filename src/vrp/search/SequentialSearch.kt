package vrp.search

import localsearch.SearchStrategy
import localsearch.heuristic.Heuristic
import vrp.*
import java.util.*
import kotlin.system.measureTimeMillis



class SequentialSearch(
        val problem: VehicleRoutingProblem,
        val restarts: Int = 0,
        val maxstuck: Int = 50,
        val maxtime: Long = 20000, // 20 seconds
        val randomStart: Boolean = false,
        val heuristic: Heuristic
): SearchStrategy {

    private fun ils(restart: Int = 0, random: Boolean = randomStart): VehicleRoutingSolution {

        val seed = restart.toLong()
        var solver = SequentialSolver(problem, if (random) RandomInitialStrategy(seed) else SweepStrategy(seed), heuristic)
        var best: VehicleRoutingSolution? = null
        var stuck = 0
        val rand = Random(seed)

        val startTime = System.currentTimeMillis()
        var elapsed = 0L

        while (stuck < maxstuck && elapsed <= maxtime) {

            if (best != null) {
                val solution = best.copy()
                solution.perturb(swaps = 1 + rand.nextInt(4) + when {
                    stuck > maxstuck - (maxstuck / 4) -> rand.nextInt(4)
                    stuck > maxstuck - (maxstuck / 3) -> rand.nextInt(3)
                    stuck > maxstuck / 2 -> rand.nextInt(2)
                    else -> 0
                })
                solver = SequentialSolver(problem, solution, heuristic)
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
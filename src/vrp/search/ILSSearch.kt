package vrp.search

import localsearch.KOptSolver
import localsearch.SearchStrategy
import localsearch.heuristic.FirstBestHeuristic
import localsearch.heuristic.Heuristic
import localsearch.heuristic.HillClimbingHeuristic
import vrp.*
import java.util.*
import kotlin.system.measureTimeMillis

class ILSSearch(
        val problem: VehicleRoutingProblem,
        val restarts: Int = 0,
        val maxstuck: Int = 50,
        val maxtime: Long = 20000, // 20 seconds
        val randomStart: Boolean = false,
        val heuristic: Heuristic
    ): SearchStrategy {

    private fun ils(restart: Int = 0, random: Boolean = randomStart): VehicleRoutingSolution {

        val seed = 1234567L * restart
        var solver = VehicleRoutingSolver(problem, if (random) RandomInitialStrategy(seed) else SweepStrategy(seed), heuristic)
        var best: VehicleRoutingSolution? = null
        var stuck = 0
        val rand = Random(seed)

        val startTime = System.currentTimeMillis()
        var elapsed = 0L

        while (stuck < maxstuck && elapsed <= maxtime) {

            if (best != null) {
                val solution = best.copy()
                solution.perturb(swaps = 1 + rand.nextInt(2))
                solver = VehicleRoutingSolver(problem, solution, heuristic)
            }

            solver.optimize()

//            val koptSolver = KOptSolver(problem, solver.solution)
//            koptSolver.optimize()

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
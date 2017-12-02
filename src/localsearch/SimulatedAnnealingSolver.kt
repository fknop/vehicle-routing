package localsearch

import printMultiple
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution
import java.util.*
import kotlin.math.exp


abstract class SimulatedAnnealingSolver(val problem: VehicleRoutingProblem, val solution: VehicleRoutingSolution, seed: Long = 0) {

    val rand = Random(seed)
    val limit = 100000

    private fun expSchedule(k: Int = 20, lam: Double = 0.05, limit: Int = this.limit): (Int) -> Double {
        return { t ->
            if (t < limit)
                k * exp(-lam * t)
            else
                .0
        }
    }

    fun cool(T: Double, rate: Double = 0.003): Double {
        return T * (1.0 - rate)
    }

    fun optimize() {
        var T = 100000000000.0
        var i = 0
        while (T > 0.0000000000000000001) {
            T = cool(T)
            ++i
            if (i % 50000 == 0) {
                // 10 random perturbations
                val successor = next()
                successor()
            }

            val successor = next()
            val delta = successor.delta

            val probability = if (delta < 0) 1.0 else exp(-delta / T)

            if (probability > rand.nextDouble()) {
                successor()
            }
        }
    }

    fun next(): Successor {
        val successors = successors()
        return successors[rand.nextInt(successors.size)]
    }

    abstract fun successors(): List<Successor>
}

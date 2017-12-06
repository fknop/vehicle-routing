import vrp.*
import java.util.*
import kotlin.math.*

fun ils(problem: VehicleRoutingProblem): VehicleRoutingSolution {
    var solver = VehicleRoutingSolver(problem, SweepStrategy())
    var best: VehicleRoutingSolution? = null
    var stuck = 0
    val rand = Random(0)
    while (stuck < 200) {

        if (best != null) {
            val solution = best.copy()
            solution.perturb(swaps = 2 + rand.nextInt(10))
            solver = VehicleRoutingSolver(problem, solution)
        }

        solver.optimize()

        if (!solver.solution.check()) {
            throw Exception("Invalid solution")
        }


        if (best == null || best.totalDistance > solver.solution.totalDistance) {
            best = solver.solution.copy()
            stuck = 0
        }
        else {
            stuck += 1
        }
    }

    return best!!
}

fun ils_random(problem: VehicleRoutingProblem): VehicleRoutingSolution {
    var best: VehicleRoutingSolution? = null
    for (i in 0L..100L) {
        val solver = VehicleRoutingSolver(problem, RandomInitialStrategy(seed = i))
        solver.optimize()

        if (best == null || best.totalDistance > solver.solution.totalDistance) {
            best = solver.solution.copy()
        }
    }

    return best!!
}


// A: 725
// B: 1185
// C: 1082
fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])
    print(ils(problem))
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
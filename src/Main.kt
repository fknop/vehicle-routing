import localsearch.heuristic.FirstBestHeuristic
import localsearch.heuristic.HillClimbingHeuristic
import vrp.*
import vrp.search.ILSSearch
import vrp.search.SequentialSearch


// A: 724
// B: 1164
// C: 1079


fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])
    val maxtime = 290000L
    val restarts = 0
    val ils = SequentialSearch(
            problem = problem,
            restarts = restarts, maxstuck = 600, maxtime = maxtime / (restarts + 1),
            randomStart = false,
            heuristic = HillClimbingHeuristic()
        )

    val (time, solution) = ils.search()
    print(solution)
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
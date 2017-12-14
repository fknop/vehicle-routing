import localsearch.heuristic.FirstBestHeuristic
import localsearch.heuristic.HillClimbingHeuristic
import vrp.*
import vrp.search.ILSSearch


// A: 724
// B: 1168
// C: 1080
fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])
    val maxtime = 290000L
    val restarts = 6
    val ils = ILSSearch(
            problem = problem,
            restarts = restarts, maxstuck = 200, maxtime = maxtime / (restarts + 1),
            randomStart = false,
            heuristic = FirstBestHeuristic()
        )

    val (time, solution) = ils.search()
    println(time)
    print(solution)
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
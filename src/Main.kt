import vrp.*

fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])

    var solver = VehicleRoutingSolver(problem, SweepStrategy())
    var best: VehicleRoutingSolution? = null
    var stuck = 0
    for (i in 1..100) {
        solver.optimize()
        val solution = solver.solution
        if (best == null || best.totalDistance > solution.totalDistance) {
            best = solution.copy()
            stuck = 0
        }
        else {
            stuck += 1
        }

        solution.perturb(hard = stuck >= 5, reallyhard = stuck >= 10)
    }

    print(best)
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
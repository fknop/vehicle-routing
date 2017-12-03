import vrp.*

fun ils(problem: VehicleRoutingProblem): VehicleRoutingSolution {
    var solver = VehicleRoutingSolver(problem, RandomInitialStrategy())
    var best: VehicleRoutingSolution? = null
    var stuck = 0
    while (stuck < 100) {

        if (best != null) {
            val solution = best.copy()
            solution.perturb(hard = stuck >= 5, reallyhard = stuck >= 15, reallyreallyhard = stuck >= 30)
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
    }

    return best!!
}

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
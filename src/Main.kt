import vrp.*

fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])

    var solver = VehicleRoutingSolver(problem, SimpleInitialStrategy())
    var best: VehicleRoutingSolution? = null
    for (i in 1..10) {
        solver.optimize()
        val solution = solver.solution
        if (best == null || best.totalDistance > solution.totalDistance) {
            best = solution.copy()
        }

        solution.perturb()
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
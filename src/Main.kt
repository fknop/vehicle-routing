fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])

    val solution = simpleInitialSolution(problem)
    val solver = VehicleRoutingSolver(problem, solution)
    solver.optimize()

    print(solution)
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
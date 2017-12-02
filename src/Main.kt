import vrp.SweepStrategy
import vrp.VehicleRoutingProblem
import vrp.VehicleRoutingSolution
import vrp.VehicleRoutingSolver

fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])

    val solverSA = VehicleRoutingSolverSA(problem, SweepStrategy())
    solverSA.optimize()
    println(solverSA.solution)
    val solver = VehicleRoutingSolver(problem, solverSA.solution)
    solver.optimize()
    val solution = solver.solution

    print(solution)




}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
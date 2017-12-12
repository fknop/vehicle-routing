import vrp.*


// A: 724
// B: 1168
// C: 1082
fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])
    val maxtime = 290000L
    val restarts = 100
    val ils = ILSSearch(problem, restarts = restarts, maxstuck = 10, maxtime = maxtime / (restarts + 1), randomStart = true)
    val (time, solution) = ils.search()
    println(time)
    println(solution)
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
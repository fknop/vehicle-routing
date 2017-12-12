import vrp.*


// A: 724
// B: 1168
// C: 1082
fun main(args: Array<String>) {

    val problem = VehicleRoutingProblem.fromFile(args[0])
    val ils = ILSSearch(problem, restarts = 1, maxstuck = 200, maxtime = Long.MAX_VALUE, randomStart = true)
    print(ils.search())
}

fun printMultiple(vararg values: Any) {
    values.forEach {
        print(it)
        print(" ")
    }

    print("\n")
}
import java.io.File
import java.util.*

data class Customer(val demand: Int, val x: Double, val y: Double)

data class VehicleRoutingProblem (val customers: List<Customer>, val k: Int, val capacity: Int) {

    val distances = computeDistances(customers.map { it.x }, customers.map { it.y })
    val warehouse = customers[0]

    companion object {
        fun fromFile (path: String): VehicleRoutingProblem {
            val file = File(path)
            val scanner = Scanner(file)

            val n = scanner.nextInt()
            val k = scanner.nextInt()
            val w = scanner.nextInt()

            val customers = (0 until n).map {
                val demand = scanner.nextInt()
                val x = scanner.nextDouble()
                val y = scanner.nextDouble()
                Customer(demand, x, y)
            }

            return VehicleRoutingProblem(customers, k, w)
        }
    }
}
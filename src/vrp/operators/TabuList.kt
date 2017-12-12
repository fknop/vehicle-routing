package vrp.operators

class TabuList(val keep: Int = 20, val clean: Int = 1000) {

    private val tabu = mutableMapOf<Int, Int>()
    private var iterations = 0

    operator fun plusAssign(value: Int) {
        tabu[value] = iterations
        iterations++
    }

    operator fun contains(value: Int): Boolean {
        return value in tabu && tabu[value]!! + keep > iterations
    }
}
package localsearch

interface Operator {
    fun neighborhood(): List<Neighbor>
}
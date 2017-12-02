package localsearch

interface Operator {
    fun successors(): List<Successor>
}
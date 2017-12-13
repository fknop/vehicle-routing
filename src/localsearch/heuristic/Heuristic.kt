package localsearch.heuristic

import localsearch.Neighbor

interface Heuristic {
    fun next(neighbors: List<Neighbor>): Neighbor?
}
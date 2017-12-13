package localsearch.heuristic

import localsearch.Neighbor

class FirstBestHeuristic : Heuristic {
    override fun next(neighbors: List<Neighbor>): Neighbor? {
        return neighbors.firstOrNull { it.delta < 0 }
    }
}
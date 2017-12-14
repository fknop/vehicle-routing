package localsearch.heuristic

import localsearch.Neighbor

class HillClimbingHeuristic: Heuristic {
    override fun next(neighbors: List<Neighbor>): Neighbor? {
        val best = neighbors.minBy { it.delta }

        if (best != null && best.delta < 0) {
            return best
        }

        return null
    }
}
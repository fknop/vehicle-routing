package localsearch.heuristic

import localsearch.Neighbor
import java.util.*

class FirstBestHeuristic : Heuristic {

    val rand = Random(12345)

    override fun next(neighbors: List<Neighbor>): Neighbor? {
        return neighbors.shuffled(rand).firstOrNull { it.delta < 0 }
    }
}
fun computeDistances(x: List<Double>, y: List<Double>): Array<Array<Int>> {
    assert(x.size == y.size)

    val n = x.size

    // n x n array filled with 0
    val distances = Array(n, { Array(n, { 0 }) })
    for (i in 0 until n) {
        for (j in 0 until n) {
            val dx = x[i] - x[j]
            val dy = y[i] - y[j]
            val distance = Math.sqrt((dx * dx) + (dy * dy))
            val normalized = Math.round(distance).toInt()
            distances[i][j] = normalized
            distances[j][i] = normalized
        }
    }

    return distances
}


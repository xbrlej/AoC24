package solutions.year24

import utils.CombinatoricsUtils

class Solver25(val locksAndKeys: List<Array<Array<Char>>>) {
    val lockHeights: List<IntArray>
    val keyHeights: List<IntArray>
    init {
        lockHeights = locksAndKeys.filter { it[0][0] == '#' }
            .map { row -> row.map { it.indexOf('.') - 1 } .toIntArray() }
        keyHeights = locksAndKeys.filter { it[0][0] == '.'}
            .map { row -> row.map { 6 - (it.indexOf('#') ) } .toIntArray() }
    }

    fun solveFirstPart(): Long {
        var result = 0L
        val lockKeyHeightPairs = CombinatoricsUtils.cartesianProduct(lockHeights, keyHeights)
        for (lockKeyPair in lockKeyHeightPairs) {
            var overlap = false
            for (i in 0..<5) {
                if (lockKeyPair.first[i] + lockKeyPair.second[i] > 5) {
                    overlap = true
                    break
                }
            }
            if (!overlap) {
                result += 1
            }
        }
        return result
    }
}
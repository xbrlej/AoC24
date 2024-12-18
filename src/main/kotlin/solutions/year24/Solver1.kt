package solutions.year24

import kotlin.math.abs

class Solver1 {
    private val tableOfColumns: Array<IntArray>

    constructor(table: Array<Array<String>>) {
        this.tableOfColumns = table.map { it.map { it.toInt() }.toIntArray() }.toTypedArray()
    }

    fun solveFirstPart(): Int {
        var sorted = tableOfColumns.map { it.sorted().toIntArray() }.toTypedArray()
        var sum = 0
        for (i in sorted[0].indices) {
            sum += abs(sorted[0][i] - sorted[1][i])
        }
        return sum
    }

    fun solveSecondPart(): Int {
        val similarityMap = HashMap<Int, Int>()
        val leftCol = tableOfColumns[0]
        val rightCol = tableOfColumns[1]
        var similarityScore = 0
        for (i in leftCol.indices) {
            if (leftCol[i] in similarityMap) {
                similarityScore += similarityMap.getOrDefault(leftCol[i], 0)
            } else {
                val countInRightCol = rightCol.count { it == leftCol[i] }
                similarityMap[leftCol[i]] = leftCol[i] * countInRightCol
                similarityScore += leftCol[i] * countInRightCol
            }
        }
        return similarityScore
    }
}
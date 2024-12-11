package solutions.year24

import kotlin.math.abs

class Solver2(rows: Array<Array<String>>) {
    private var rows: Array<IntArray> = rows.map { row -> row.map { it.toInt() }.toIntArray() }.toTypedArray()

    fun solveFirstPart(): Int {
        return rows.map { isSafe(it) }.count { it }
    }

    fun solveSecondPart(): Int {
        return rows.map { isSafeRemovalPermutations(it) }.count { it }
    }

    private fun isSafe(row: IntArray): Boolean {
        // Sorted asc
        return pairsSortedAscWithCondition(row).all { it } || pairsSortedDescWithCondition(row).all { it }
    }

    // Dummy method with permutations
    private fun isSafeRemovalPermutations(row: IntArray): Boolean {
        if (isSafe(row)) {
            return true
        }
        // There are cases where 2 instances of unsatisfied condition can be solved by removing one element
        // For instance 1 2 5 1 6, (5, 1) is incorrect and (1, 6) is incorrect, can be solved by removing at index 3
        if (pairsSortedAscWithCondition(row).count { !it } >= 3 && pairsSortedDescWithCondition(row).count { !it } >= 3) {
            return false
        }
        for (i in row.indices) {
            val copied = row.copyOf().toMutableList()
            copied.removeAt(i)
            if (isSafe(copied.toIntArray())) {
                return true
            }
        }
        return false
    }

    // Attempts at logic and O(n) solution, unsuccessful
    fun isSafeAfterOneRemoved(row: IntArray): Boolean {
        if (isSafe(row)) {
            return true
        }
        val pairsAsc = pairsSortedAscWithCondition(row)
        val pairsDesc = pairsSortedDescWithCondition(row)

        // If all rows are sorted, the difference condition is not satisfied, and that can't be improved by removing anything
        if (pairsAsc.all { it } || pairsDesc.all { it }) {
            return false
        }

        // If there is only one unsorted element
        if (pairsAsc.count { !it } == 1) {
            val idx = pairsAsc.indexOf(false)
            if (idx == 0) {
                return true
            }
            return pairSortedAscWithCondition(row[idx - 1], row[idx + 1])
        }
        if (pairsDesc.count { !it } == 1) {
            val idx = pairsDesc.indexOf(false)
            if (idx == 0) {
                return true
            }
            return pairSortedDescWithCondition(row[idx - 1], row[idx + 1])
        }
        return false
    }

    private fun pairsSortedAscWithCondition(row: IntArray): List<Boolean> {
        return row.asList().zipWithNext { a, b -> pairSortedAscWithCondition(a, b) }
    }

    private fun pairsSortedDescWithCondition(row: IntArray): List<Boolean> {
        return row.asList().zipWithNext { a, b -> pairSortedDescWithCondition(a, b) }
    }

    private fun pairSortedAscWithCondition(a: Int, b: Int): Boolean {
        return a < b && abs(a - b) <= 3
    }

    private fun pairSortedDescWithCondition(a: Int, b: Int): Boolean {
        return a > b && abs(a - b) <= 3
    }
}
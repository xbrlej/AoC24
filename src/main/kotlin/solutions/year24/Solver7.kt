package solutions.year24

import utils.CombinatoricsUtils.getXNaryVariations

class Solver7(val rows: Array<LongArray>) {
    fun solveFirstPart(): Long {
        return rows.filter { isRowSolvable(it, false) }.sumOf { it[0] }
    }

    fun solveSecondPart(): Long {
        return rows.filter { isRowSolvable(it, true) }.sumOf { it[0] }
    }

    fun isRowSolvable(row: LongArray, concat: Boolean): Boolean {
        val rowResult = row[0]
        val radix = if (concat) {
            3
        } else {
            2
        }
        val operatorVariations = getXNaryVariations(radix, row.size - 2)
        for (operators in operatorVariations) {
            var tmpResult = row[1]
            for ((index, operator) in operators.withIndex()) {
                if (operator == '0') {
                    tmpResult *= row[index + 2]
                } else if (operator == '1') {
                    tmpResult += row[index + 2]
                } else {
                    tmpResult = tmpResult.cat(row[index + 2])
                }
                if (tmpResult > rowResult) {
                    continue
                }
            }
            if (tmpResult == rowResult) {
                return true
            }
        }
        return false
    }

    fun Long.cat(other: Long): Long {
        return this.toString().plus(other.toString()).toLong()
    }
}
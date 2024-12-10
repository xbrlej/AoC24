package solutions.year24

class Solver5(val rules: Array<Pair<Int, Int>>, val updates: Array<IntArray>) {

    fun updateValidation(update: IntArray): Int {
        for (i in update.copyOfRange(0, update.size - 1).indices) {
            for (j in IntRange(i+1, update.size - 1)) {
                if (rules.contains(Pair(update[j], update[i]))) {
                    return 0
                }
            }
        }
        return update[update.size / 2]
    }

    fun solveFirstPart(): Int {
        return updates.sumOf { updateValidation(it) }
    }

    fun solveSecondPart(): Int {
        assert(solveFirstPart() == updates.filter {updateValidation(it) != 0}.sumOf { update -> middleElem((sortByRules(update).toTypedArray())) })
        return updates.filter {updateValidation(it) == 0}.sumOf { update -> middleElem((sortByRules(update).toTypedArray())) }
    }

    fun sortByRules(input: IntArray): IntArray {
        val result = input.copyOf()
        for (i in result.indices) {
            for (j in result.indices) {
                if (rules.contains(Pair(result[i], result[j])) && i > j) {
                    // Swap
                    var tmp = result[i]
                    result[i] = result[j]
                    result[j] = tmp
                }
            }
        }
        return result
    }

    fun <T>middleElem(input: Array<T>): T {
        return input[input.size / 2]
    }
}
package solutions.year24

class Solver19(val subwords: List<String>, val words: List<String>) {

    val buildableMap = mutableMapOf<String, Long>().withDefault { 0L }

    fun solveFirstPart(): Long {
        var result = 0L
        for (word in words) {
            if (isBuildable(word) != 0L) {
                result += 1
            }
        }
        return result
    }

    fun solveSecondPart(): Long {
        var result = 0L
        for (word in words) {
            result += isBuildable(word)
        }
        return result
    }

    fun isBuildable(word: String): Long {
        if (word in buildableMap) {
            return buildableMap.getValue(word)
        }
        for (subword in subwords.sortedBy { it.length }) {
            if (word.startsWith(subword)) {
                if (word == subword) {
                    buildableMap[word] = buildableMap.getValue(word) + 1
                }
                val isTailBuildable = isBuildable(word.substring(subword.length))
                if (isTailBuildable != 0L) {
                    buildableMap[word] = buildableMap.getValue(word) + isTailBuildable
                }
            }
        }
        return buildableMap.getValue(word)
    }
}
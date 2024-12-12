package solutions.year24

class Solver11(val numberStrings: Array<String>) {
    fun solveFirstPart(): Int {
        val copy = numberStrings.copyOf().map { it.toCharArray() }.toMutableList()
        for (i in 0..<25) {
            for (j in copy.size - 1 downTo 0) {
                val processed = processNumber(copy.removeAt(j))
                copy.addAll(j, processed)
            }
        }
        return copy.size
    }

    // Just a more efficient solution
    fun solveSecondPart(): Long {
        val mapOfResults = mutableMapOf<Pair<String, Int>, Long>()
        var result = 0L
        for (string in numberStrings) {
            result += recursiveCountWithStoredResults(string, 150, mapOfResults)
        }
        return result
    }

    private fun recursiveCountWithStoredResults(string: String, i: Int, mapOfResults: MutableMap<Pair<String, Int>, Long>): Long {
        val keyPair = Pair(string, i)
        var returnValue: Long
        if (mapOfResults.contains(keyPair)) {
            return mapOfResults.getOrDefault(keyPair, 0)
        }
        if (i == 0) {
            returnValue = 1
        } else {
            val processedNumbers = processNumber(string.toCharArray())
            var result = 0L
            for (processed in processedNumbers) {
                result += recursiveCountWithStoredResults(String(processed), i - 1, mapOfResults)
            }
            returnValue = result
        }
        mapOfResults[keyPair] = returnValue
        return returnValue
    }

    private fun processNumber(numberCharArray: CharArray): List<CharArray> {
        val number = String(numberCharArray).toLong()
        if (number == 0L) {
            return listOf("1".toCharArray())
        }
        val len = numberCharArray.size
        if (len % 2 == 0) {
            val firstPart = numberCharArray.copyOfRange(0, len / 2)
            val secondPart = String(numberCharArray.copyOfRange(len / 2, len)).toLong().toString().toCharArray()
            return listOf(firstPart, secondPart)
        }
        return listOf((number * 2024).toString().toCharArray())
    }
}
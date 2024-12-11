package solutions.year24

class Solver4(val matrix: Array<CharArray>, val word: CharArray) {

    //    Crossword solver
    fun solveFirstPart(): Int {
        var occurrences = 0
        for ((i, row) in matrix.withIndex()) {
            for (j in row.indices) {
                if (matrix[i][j] == this.word[0]) {
                    val directions = getAvailDirections(i, j, word[1])
                    for (direction in directions) {
                        if (checkWordInDirection(i, j, direction, this.word)) {
                            occurrences++
                        }
                    }
                }
            }
        }
        return occurrences
    }

    private fun getAvailDirections(x: Int, y: Int, chr: Char): Array<Pair<Int, Int>> {
        val availableIndices = mutableListOf<Pair<Int, Int>>()
        for (i in IntRange(-1, 1)) {
            for (j in IntRange(-1, 1)) {
                if (x + i in matrix.indices && y + j in matrix[0].indices && matrix[x + i][y + j] == chr) {
                    availableIndices.add(Pair(i, j))
                }
            }
        }
        return availableIndices.toTypedArray()
    }

    private fun checkWordInDirection(x: Int, y: Int, direction: Pair<Int, Int>, word: CharArray): Boolean {
        for ((i, chr) in word.withIndex()) {
            val tmpX = x + i * direction.first
            val tmpY = y + i * direction.second
            if (!(tmpX in matrix.indices && tmpY in matrix[0].indices && matrix[tmpX][tmpY] == chr)) {
                return false
            }
        }
        return true
    }

    fun solveSecondPart(): Int {
        var crossOccurrences = 0
        val amWord = "AM".toCharArray()
        val asWord = "AS".toCharArray()
        val directions = arrayOf(Pair(-1, -1), Pair(1, -1), Pair(-1, 1), Pair(1, 1))
        for ((i, row) in matrix.withIndex()) {
            for (j in row.indices) {
                if (matrix[i][j] == 'A') {
                    val amOccurrences = directions.map { checkWordInDirection(i, j, it, amWord) }.count { it }
                    val asOccurrences = directions.map { checkWordInDirection(i, j, it, asWord) }.count { it }
                    if (amOccurrences == 2 &&
                        asOccurrences == 2 &&
                        checkWordInDirection(i, j, directions[0], amWord) !=
                        checkWordInDirection(i, j, directions[3], amWord) // MAM
                    ) {
                        crossOccurrences++
                    }
                }
            }
        }
        return crossOccurrences
    }
}
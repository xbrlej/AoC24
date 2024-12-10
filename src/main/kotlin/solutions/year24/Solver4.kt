package solutions.year24

class Solver4(val matrix: Array<CharArray>, val word: CharArray) {

    //    Crossword solver
    fun solveFirstPart(): Int {
        var occurences = 0
        for ((i, row) in matrix.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (matrix[i][j] == this.word[0]) {
                    var directions = getAvailDirections(i, j, word[1])
                    for (direction in directions) {
                        if (checkWordInDirection(i, j, direction, this.word)) {
                            occurences++
                        }
                    }
                }
            }
        }
        return occurences
    }

    fun getAvailDirections(x: Int, y: Int, chr: Char): Array<Pair<Int, Int>> {
        var availableIndices = mutableListOf<Pair<Int, Int>>()
        for (i in IntRange(-1, 1)) {
            for (j in IntRange(-1, 1)) {
                if (x + i in matrix.indices && y + j in matrix[0].indices && matrix[x + i][y + j] == chr) {
                    availableIndices.add(Pair(i, j))
                }
            }
        }
        return availableIndices.toTypedArray()
    }

    fun checkWordInDirection(x: Int, y: Int, direction: Pair<Int, Int>, word: CharArray): Boolean {
        for ((i, chr) in word.withIndex()) {
            var tmpX = x + i * direction.first
            var tmpY = y + i * direction.second
            if (!(tmpX in matrix.indices && tmpY in matrix[0].indices && matrix[tmpX][tmpY] == chr)) {
                return false
            }
        }
        return true
    }

    fun solveSecondPart(): Int {
        var cross_occurences = 0
        val am_word = "AM".toCharArray()
        val as_word = "AS".toCharArray()
        val directions = arrayOf(Pair(-1, -1), Pair(1, -1), Pair(-1, 1), Pair(1, 1))
        for ((i, row) in matrix.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (matrix[i][j] == 'A') {
                    var am_occurences = directions.map { checkWordInDirection(i, j, it, am_word) }.count { it }
                    var as_occurences = directions.map { checkWordInDirection(i, j, it, as_word) }.count { it }
                    if (am_occurences == 2 &&
                        as_occurences == 2 &&
                        checkWordInDirection(i, j, directions[0], am_word) !=
                        checkWordInDirection(i, j, directions[3], am_word) // MAM
                    ) {
                        cross_occurences++
                    }
                }
            }
        }
        return cross_occurences
    }
}
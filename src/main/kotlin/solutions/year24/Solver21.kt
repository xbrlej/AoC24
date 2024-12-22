package solutions.year24

import utils.CombinatoricsUtils.cartesianProduct
import utils.GraphUtils
import utils.GraphUtils.getGraphPaths

class Solver21(val input: List<Int>) {
    val RIGHT = '>'
    val LEFT = '<'
    val DOWN = 'v'
    val UP = '^'
    val ACT = 'A'

//    +---+---+---+
//    | 7 | 8 | 9 |
//    +---+---+---+
//    | 4 | 5 | 6 |
//    +---+---+---+
//    | 1 | 2 | 3 |
//    +---+---+---+
//        | 0 | A |
//        +---+---+
    val numericGraph = mapOf<Int, List<Int>>(
            Pair(7, listOf(8, 4)),
            Pair(8, listOf(7, 9, 5)),
            Pair(9, listOf(8, 6)),
            Pair(4, listOf(7, 5, 1)),
            Pair(5, listOf(8, 4, 6, 2)),
            Pair(6, listOf(9, 5, 3)),
            Pair(1, listOf(2, 4)),
            Pair(2, listOf(5, 1, 3, 0)),
            Pair(3, listOf(6, 2, -1)),
            Pair(0, listOf(2, -1)),
            Pair(-1, listOf(0, 3)) // A
        )

    val numericNeighbourToDirectionMap = mapOf<Pair<Int, Int>, Char> (
        Pair(Pair(7, 8), RIGHT),
        Pair(Pair(7, 4), DOWN),
        Pair(Pair(8, 9), RIGHT),
        Pair(Pair(8, 7), LEFT),
        Pair(Pair(8, 5), DOWN),
        Pair(Pair(9, 8), LEFT),
        Pair(Pair(9, 6), DOWN),
        Pair(Pair(4, 7), UP),
        Pair(Pair(4, 5), RIGHT),
        Pair(Pair(4, 1), DOWN),
        Pair(Pair(5, 8), UP),
        Pair(Pair(5, 4), LEFT),
        Pair(Pair(5, 2), DOWN),
        Pair(Pair(5, 6), RIGHT),
        Pair(Pair(6, 9), UP),
        Pair(Pair(6, 5), LEFT),
        Pair(Pair(6, 3), DOWN),
        Pair(Pair(1, 4), UP),
        Pair(Pair(1, 2), RIGHT),
        Pair(Pair(2, 5), UP),
        Pair(Pair(2, 1), LEFT),
        Pair(Pair(2, 3), RIGHT),
        Pair(Pair(2, 0), DOWN),
        Pair(Pair(3, 6), UP),
        Pair(Pair(3, 2), LEFT),
        Pair(Pair(3, -1), DOWN),
        Pair(Pair(0, 2), UP),
        Pair(Pair(0, -1), RIGHT),
        Pair(Pair(-1, 0), LEFT),
        Pair(Pair(-1, 3), UP)
    )

//        +---+---+
//        | ^ | A |
//    +---+---+---+
//    | < | v | > |
//    +---+---+---+
    val directionalGraph = mapOf<Char, List<Char>> (
        Pair(UP, listOf(ACT, DOWN)),
        Pair(LEFT, listOf(DOWN)),
        Pair(DOWN, listOf(LEFT, UP, RIGHT)),
        Pair(RIGHT, listOf(ACT, DOWN)),
        Pair(ACT, listOf(UP, RIGHT))
    )

    val directionalNeighbourToDirectionsMap = mapOf<Pair<Char, Char>, Char>(
        Pair(Pair(UP, ACT), RIGHT),
        Pair(Pair(UP, DOWN), DOWN),
        Pair(Pair(LEFT, DOWN), RIGHT),
        Pair(Pair(DOWN, LEFT), LEFT),
        Pair(Pair(DOWN, RIGHT), RIGHT),
        Pair(Pair(DOWN, UP), UP),
        Pair(Pair(RIGHT, DOWN), LEFT),
        Pair(Pair(RIGHT, ACT), UP),
        Pair(Pair(ACT, UP), LEFT),
        Pair(Pair(ACT, RIGHT), DOWN)
    )

    fun solveFirstPart(): Long {
        return solveSecondPart(2)
    }

    fun solveSecondPart(layers: Int): Long {
        var sum = 0L
        val precomputedMovesInGeneration = mutableMapOf<Triple<Char, Char, Int>, Long>()
        for (number in input) {
            val firstLayerDirections = numberToDirections(number)
            val costs = firstLayerDirections.map { computeDirectionInGeneration(precomputedMovesInGeneration, direction = it,  generation = layers) }
            sum += costs.min() * number
        }
        return sum
    }

    fun computeDirectionInGeneration(precomputedMovesInGeneration: MutableMap<Triple<Char, Char, Int>, Long>, direction: String, generation: Int): Long {
        var result = 0L
        result += computeMoveInGeneration(precomputedMovesInGeneration, ACT, direction[0], generation)
        for (i in 0..<direction.length-1) {
            val startChar = direction[i]
            val endChar = direction[i + 1]
            result += computeMoveInGeneration(precomputedMovesInGeneration, startChar, endChar, generation)
        }
        return result
    }

    fun computeMoveInGeneration(precomputedMovesInGeneration: MutableMap<Triple<Char, Char, Int>, Long>, start: Char, end: Char, generation: Int): Long {
        var result: Long
        if (Triple(start, end, generation) in precomputedMovesInGeneration) {
            return precomputedMovesInGeneration[Triple(start, end, generation)]!!
        } else if (generation == 1) {
            val startToEndBfsPaths = GraphUtils.breadthFirstSearchDistancesAndPrevMap(directionalGraph, start)
            val paths = getGraphPaths(startToEndBfsPaths.second, start, end, startToEndBfsPaths.first[end]!!)
            val directions = paths.map { directionPathToMetaDirections(it) }
            result = directions.map { it.length }.min().toLong()
        } else {
            val startToEndBfsPaths = GraphUtils.breadthFirstSearchDistancesAndPrevMap(directionalGraph, start)
            val paths = getGraphPaths(startToEndBfsPaths.second, start, end, startToEndBfsPaths.first[end]!!)
            val values = paths.map { computeDirectionInGeneration(precomputedMovesInGeneration, directionPathToMetaDirections(it), generation - 1) }
            result = values.min()
        }
        precomputedMovesInGeneration[Triple(start, end, generation)] = result
        return result
    }




    fun directionToMetaDirections(direction: String): List<String> {
        val start = ACT
        val end = ACT
        val directionPathsMetaDirections = getMetaDirectionsFromDirection(start, direction[0])
        var result = directionPathsMetaDirections
        for (i in 0..<direction.length - 1) {
            result = combinePathSets(result, getMetaDirectionsFromDirection(direction[i], direction[i + 1]))
        }
        return result
    }

    fun getMetaDirectionsFromDirection(start: Char, end: Char): List<String> {
        val startToEndBfsPaths = GraphUtils.breadthFirstSearchDistancesAndPrevMap(directionalGraph, start)
        val graphPaths = getGraphPaths(startToEndBfsPaths.second, start, end, startToEndBfsPaths.first[end]!!)
        return graphPaths.map { directionPathToMetaDirections(it, true) }
    }

    fun numberToDirections(number: Int): List<String> {
        val start = -1
        val firstDigit = number / 100
        val secondDigit = (number % 100) / 10
        val thirdDigit = number % 10
        val end = start

        // Move to first digit
        val firstDigitPathsDirections = getDirectionPathsFromNumeric(start, firstDigit)

        // Move to second digit
        val secondDigitPathsDirections = getDirectionPathsFromNumeric(firstDigit, secondDigit)

        // Move to third digit
        val thirdDigitPathsDirections = getDirectionPathsFromNumeric(secondDigit, thirdDigit)

        // Return to start
        val endPathsDirections = getDirectionPathsFromNumeric(thirdDigit, end)

        return combinePathSets(combinePathSets(combinePathSets(firstDigitPathsDirections, secondDigitPathsDirections), thirdDigitPathsDirections), endPathsDirections)
    }

    fun getDirectionPathsFromNumeric(start: Int, end: Int): List<String> {
        val firstDigitBfsPaths = GraphUtils.breadthFirstSearchDistancesAndPrevMap(numericGraph, start)
        val graphPaths = getGraphPaths(firstDigitBfsPaths.second, start, end, firstDigitBfsPaths.first[end]!!)
        return graphPaths.map { numericPathToDirections(it, true) }
    }

    fun combinePathSets(pathSet1: List<String>, pathSet2: List<String>): List<String> {
        return cartesianProduct(pathSet1, pathSet2).map { it.first + it.second }
    }

    fun numericPathToDirections(numericPath: List<Int>, addA: Boolean = true): String {
        var result = ""
        for (i in 0..<numericPath.size - 1) {
            result += numericNeighbourToDirectionMap[Pair(numericPath[i], numericPath[i + 1])]
        }
        if (addA) {
            result += ACT
        }
        return result
    }

    fun directionPathToMetaDirections(directionPath: List<Char>, addA: Boolean = true): String {
        var result = ""
        for (i in 0..<directionPath.size - 1) {
            result += directionalNeighbourToDirectionsMap[Pair(directionPath[i], directionPath[i + 1])]
        }
        if (addA) {
            result += ACT
        }
        return result
    }
}
package solutions.year24

import utils.CombinatoricsUtils.getCombinations
import utils.GridUtils
import utils.GridUtils.gridDistance
import utils.GridUtils.isInGrid
import utils.GridUtils.move

class Solver8 {
    val grid: Array<CharArray>
    val charsToProcess: CharArray

    constructor(grid: Array<CharArray>) {
        this.grid = grid
        val charList = mutableListOf<Char>()
        grid.forEach { row -> charList.addAll(row.filter { it != '.' }) }
        this.charsToProcess = charList.toCharArray()
    }

    fun solveFirstPart(): Int {
        return markLocations(false)
    }

    fun solveSecondPart(): Int {
        return markLocations(true)
    }

    private fun markLocations(multipleSteps: Boolean): Int {
        var markedLocations = mutableSetOf<Pair<Int, Int>>()
        for (char in this.charsToProcess) {
            val typedCharGrid = grid.map { it.toTypedArray() }.toTypedArray()
            val charLocations = GridUtils.locationsOf(typedCharGrid, char)
            if (charLocations.size <= 1) {
                continue
            }
            val locationPairs = getCombinations(charLocations, 2)
            for (locationPair in locationPairs) {
                val first = locationPair[0]
                val second = locationPair[1]
                if (multipleSteps) {
                    markedLocations.add(first)
                    markedLocations.add(second)
                }
                val distance = gridDistance(first, second)
                var forwardSteps = 1
                var backwardSteps = 1
                while (multipleSteps || forwardSteps == 1) {
                    val forwardExtendLoc = move(second, distance, forwardSteps)
                    if (isInGrid(typedCharGrid, forwardExtendLoc)) {
                        markedLocations.add(forwardExtendLoc)
                        forwardSteps++
                    } else {
                        break
                    }
                }
                while (multipleSteps || backwardSteps == 1) {
                    val backwardExtendLoc = move(first, distance, -backwardSteps)
                    if (isInGrid(typedCharGrid, backwardExtendLoc)) {
                        markedLocations.add(backwardExtendLoc)
                        backwardSteps++
                    } else {
                        break
                    }
                }
            }
        }
        return markedLocations.count()
    }
}
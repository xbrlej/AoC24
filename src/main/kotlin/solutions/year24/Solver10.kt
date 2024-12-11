package solutions.year24

import utils.GridUtils
import utils.GridUtils.isInGrid

class Solver10(private val grid: Array<Array<Int>>) {
    fun solveFirstPart(): Int {
        return processNodesToCollection(mutableSetOf())
    }

    fun solveSecondPart(): Int {
        return processNodesToCollection(mutableListOf())
    }

    private fun processNodesToCollection(collection: MutableCollection<Pair<Int, Int>>): Int {
        var result = 0
        for ((rowIndex, row) in grid.withIndex()) {
            for (colIndex in row.indices) {
                if (grid[rowIndex][colIndex] == 0) {
                    processNode(0, rowIndex, colIndex, collection)
                    result += collection.size
                    collection.clear()
                }
            }
        }
        return result
    }

    private fun processNode(value: Int, x: Int, y: Int, peaks: MutableCollection<Pair<Int, Int>>) {
        if (value == 9) {
            peaks.add(Pair(x, y))
            return
        }
        val directions = GridUtils.basicDirections
        val locations = directions.map {GridUtils.move(Pair(x, y), it, steps=1)}.toTypedArray()
        for (location in locations) {
            if (isInGrid(grid, location) && grid[location.first][location.second] == value+1) {
                processNode(value+1, location.first, location.second, peaks)
            }
        }
    }
}
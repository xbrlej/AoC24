package solutions.year24

import utils.GridUtils
import view.GridView
import java.io.File
import java.lang.Math.floorMod

class Solver14(private val locationsAndDirections: Array<Pair<Pair<Int, Int>, Pair<Int, Int>>>, private val gridSize: Pair<Int, Int>, private val steps: Int) {
    fun solveFirstPart(): Long {
        val finalLocations = mutableListOf<Pair<Int, Int>>()
        for ((loc, dir) in locationsAndDirections) {
            val finalLoc = GridUtils.move(loc, dir, steps)
            val finalLocModuloGrid = Pair(floorMod(finalLoc.first, gridSize.first), floorMod(finalLoc.second, gridSize.second))
            finalLocations.add(finalLocModuloGrid)
        }
        val rowHalf = gridSize.first / 2
        val colHalf = gridSize.second / 2
        val filteredMiddle = finalLocations.filter { it.first != rowHalf && it.second != colHalf }
        val grouped = filteredMiddle.groupBy { loc -> Pair(loc.first > rowHalf, loc.second > colHalf) }
        return grouped.values.map { it.size.toLong() }.reduce(Long::times)
    }

    fun solveSecondPart() {
        val textGrid = Array(gridSize.first) {Array(gridSize.second) {'.'} }

        // Modular arithmetic - X coordinates repeat themselves each 101 steps, Y coordinates repeat themselves each 103 steps
        // XY coordinates repeat themselves each LCM(101, 103) steps and 103 is prime, therefore we only need to check 101 * 103 states
        for (i in 0..< gridSize.first * gridSize.second) {
            val copy = textGrid.map { it.copyOf() }.toTypedArray()
            val locationSet = mutableSetOf<Pair<Int, Int>>()
            for ((loc, dir) in locationsAndDirections) {
                val finalLoc = GridUtils.move(loc, dir, i)
                val finalLocModuloGrid = Pair(floorMod(finalLoc.first, gridSize.first), floorMod(finalLoc.second, gridSize.second))
                copy[finalLocModuloGrid.first][finalLocModuloGrid.second] = '#'
                locationSet.add(finalLocModuloGrid)
            }
            val newLocations = GridUtils.locationsOf(copy, '#')
            val regions = newLocations.map { GridUtils.getRegion(copy, '#', it.first, it.second) }
            if (regions.any { it.size > locationsAndDirections.size / 5 }) {
                drawGrid(copy, i)
                // View with title of i showing the tree should pop up
                val view = GridView(gridSize.first, gridSize.second, locationSet, i)
            }
        }
    }

    private fun drawGrid(grid: Array<Array<Char>>, iteration: Int) {
        val sb = StringBuilder()
        for (row in grid) {
            for (col in row) {
                sb.append(col)
            }
            sb.append('\n')
        }

        val file = File("src/main/kotlin/solutions/year24/output/$iteration.txt")
        file.createNewFile()
        file.writeText(sb.toString())
    }
}
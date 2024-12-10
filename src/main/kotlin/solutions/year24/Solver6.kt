package solutions.year24

import utils.GridUtils.locationsOf
import utils.GridUtils.isInGrid
import utils.GridUtils.move
import utils.GridUtils.rotateRight

class Solver6 {
    var grid: Array<CharArray>
    val unmodifiableGrid: Array<CharArray>
    val initDirection: Pair<Int, Int> = Pair(-1, 0)
    var direction = initDirection
    val initLocation: Pair<Int, Int>
    var location: Pair<Int, Int>
    var history: MutableSet<Triple<Int, Int, Pair<Int, Int>>> = HashSet()

    constructor(grid: Array<CharArray>) {
        this.grid = grid
        this.unmodifiableGrid = grid.map { it.copyOf() }.toTypedArray()
        val startLocations = locationsOf(grid.map{it.toTypedArray()}.toTypedArray(), '^')
        assert(startLocations.size == 1)
        initLocation = startLocations[0]
        location = initLocation
    }

    fun solveFirstPart(): Int {
        resetGrid()
        while (step(rotateHist = false)) {}
        return grid.sumOf { it.count { it == 'X' } }
    }

    fun solveSecondPart(): Int {

        // An extremely slow brute force, i am ashamed
        solveFirstPart()
        var count = 0
        val originalHist = history.map{ Pair(it.first, it.second) }.toTypedArray()
        for ((x, y) in originalHist) {
            resetGrid()
            if (Pair(x, y) == initLocation) {
                continue
            }
            if (grid[x][y] == '#') {
                continue
            }
            grid[x][y] = '#'
            var size = history.size
            while(step(rotateHist = true)) {
                if (size == history.size) {
                    count++
                    break
                }
                size = history.size
            }
        }
        return count
    }

    fun step(rotateHist: Boolean): Boolean {
        val newLoc = move(location, direction, 1)
        var fin: Boolean
        if (!isInGrid(grid.map { it.toTypedArray() }.toTypedArray(), newLoc)) {
            fin = true
//            grid[location.first][location.second] = 'X'
            history.add(Triple(location.first, location.second, direction))
            location = newLoc
        } else {
            fin = false
            if (grid[newLoc.first][newLoc.second] == '#') {
                if (rotateHist) {
                    history.add(Triple(location.first, location.second, direction))
                }
                direction = rotateRight(direction)
            } else {
//                grid[location.first][location.second] = 'X'
                history.add(Triple(location.first, location.second, direction))
                location = newLoc
            }
        }
        return !fin
    }

    fun resetGrid() {
        grid = unmodifiableGrid.map { it.copyOf() }.toTypedArray()
        location = initLocation
        direction = initDirection
        history = HashSet()
    }
}
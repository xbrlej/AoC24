package utils

import kotlin.math.abs

/**
 * Helper class for operations in a 2D grid
 * This class is **stateless**, it only calculates coordinates and vectors
 */
object GridUtils {

    val UP = Pair(-1, 0)
    val RIGHT = Pair(0, 1)
    val DOWN = Pair(1, 0)
    val LEFT = Pair(0, -1)

    val basicDirections: Array<Pair<Int, Int>> = arrayOf(UP, RIGHT, DOWN, LEFT)
    val diagonalDirections: Array<Pair<Int, Int>> = arrayOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))
    val allDirections = basicDirections + diagonalDirections

    fun move(initLocation: Pair<Int, Int>, distance: Pair<Int, Int>, steps: Int): Pair<Int, Int> {
        return Pair(initLocation.first + steps * distance.first, initLocation.second + steps * distance.second)
    }

    fun rotateRight(direction: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(direction.second, -direction.first)
    }

    fun rotateLeft(direction: Pair<Int, Int>): Pair<Int, Int> {
        return -rotateRight(direction)
    }

    fun<T> isInGrid(grid: Array<Array<T>>, loc: Pair<Int, Int>): Boolean {
        return isInGrid(Pair(grid.size, grid[0].size), loc)
    }

    fun isInGrid(gridSize: Pair<Int, Int>, loc: Pair<Int, Int>): Boolean {
        return loc.first >= 0 && loc.second >= 0 && loc.first < gridSize.first && loc.second < gridSize.second
    }

    fun<T> locationsOf(grid: Array<Array<T>>, elem: T): Array<Pair<Int, Int>> {
        var result: MutableList<Pair<Int, Int>> = mutableListOf()
        val rowsIndicesToCheck = grid.indices.filter{grid[it].contains(elem)}
        for (index in rowsIndicesToCheck) {
            var locations = grid[index].withIndex().filter{it.value == elem}.map { Pair(index, it.index) }
            result.addAll(locations)
        }
        return result.toTypedArray()
    }

    fun gridDistance(locationA: Pair<Int, Int>, locationB: Pair<Int, Int>): Pair<Int, Int> {
        return locationB - locationA
    }

    fun gridAbsDistance(locationA: Pair<Int, Int>, locationB: Pair<Int, Int>): Int {
        val gridDist = gridDistance(locationA, locationB)
        return abs(gridDist.first) + abs(gridDist.second)
    }

    fun<T> getRegion(input: Array<Array<T>>, flower: T, x: Int ,y: Int, includeDiagonalDirections: Boolean = false): Set<Pair<Int, Int>> {
        val directions: Array<Pair<Int, Int>>
        if (includeDiagonalDirections) {
            directions = allDirections
        } else {
            directions = basicDirections
        }
        val queue = mutableListOf(Pair(x, y))
        val result = mutableSetOf(Pair(x, y))
        while (queue.isNotEmpty()) {
            val elem = queue.removeFirst()
            for (dir in directions) {
                val neighbour = move(elem, dir, 1)
                val size = result.size
                if (isInGrid(input, neighbour) && input[neighbour.first][neighbour.second] == flower) {
                    result.add(neighbour)
                }
                // Add to processing if not tracked yet
                if (result.size != size) {
                    queue.add(neighbour)
                }
            }
        }
        return result
    }

    fun getLocationsInDirection(gridSize: Pair<Int, Int>, location: Pair<Int, Int>, direction: Pair<Int, Int>): List<Pair<Int, Int>> {
        var counter = 1
        var newLoc = move(location, direction, counter)
        val possibleLocations = mutableListOf<Pair<Int, Int>>()
        while (isInGrid(gridSize, newLoc)) {
            possibleLocations.add(newLoc)
            counter += 1
            newLoc = move(location, direction, counter)
        }
        return possibleLocations
    }

    fun getAllLocationsInDistance(gridSize: Pair<Int, Int>, location: Pair<Int, Int>, distance: Int): Set<Pair<Int, Int>> {
        var locations = mutableSetOf<Pair<Int, Int>>()
        for (i in 0..distance) {
            for (j in 0..distance - i) {
                locations.add(location + Pair(i, j))
                locations.add(location + Pair(i, -j))
                locations.add(location + Pair(-i, j))
                locations.add(location + Pair(-i, -j))
            }
        }
        locations.remove(location)
        return locations.filter { isInGrid(gridSize, it) }.toSet()
    }



    fun getNeighbours(location: Pair<Int, Int>): Array<Pair<Int, Int>> {
        var result = Array(4) {Pair(0, 0)}
        for ((idx, dir) in basicDirections.withIndex()) {
            result[idx] = location + dir
        }
        return result
    }

    fun oneDimensionalIndex(rowSize: Int, location: Pair<Int, Int>): Int {
        return location.first * rowSize + location.second
    }

    fun twoDimensionalIndex(rowSize: Int, index: Int): Pair<Int, Int> {
        return Pair(index / rowSize, index % rowSize)
    }

    operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(this.first - other.first, this.second - other.second)
    }

    operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
        return Pair(this.first + other.first, this.second + other.second)
    }

    operator fun Pair<Int, Int>.unaryMinus(): Pair<Int, Int> {
        return Pair(-this.first, -this.second)
    }
}
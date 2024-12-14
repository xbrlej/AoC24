package utils

object GridUtils {
    val basicDirections: Array<Pair<Int, Int>> = arrayOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
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

    fun<T> getRegion(input: Array<Array<T>>, flower: T, x: Int ,y: Int, includeDiagonalDirections: Boolean = false): Set<Pair<Int, Int>> {
        val directions: Array<Pair<Int, Int>>
        if (includeDiagonalDirections) {
            directions = allDirections
        } else {
            directions = basicDirections
        }
        val queue = mutableListOf(Pair(x, y))
        val result = mutableSetOf(Pair(x, y))
        while (queue.size != 0) {
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
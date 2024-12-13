package utils

object GridUtils {
    val basicDirections: Array<Pair<Int, Int>> = arrayOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))

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
        return loc.first >= 0 && loc.second >= 0 &&
                loc.first < grid.size && loc.second < grid[loc.first].size
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
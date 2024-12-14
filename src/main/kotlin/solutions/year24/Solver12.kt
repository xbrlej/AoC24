package solutions.year24

import utils.GridUtils
import utils.GridUtils.minus

class Solver12(val input: Array<CharArray>) {

    private val typed = input.map { it.toTypedArray() }.toTypedArray()

    fun solveFirstPart(): Long {
        val locationBoundaryMap = mutableMapOf<Set<Pair<Int, Int>>, Int>()
        for ((rowIndex, row) in input.withIndex()) {
            for ((colIndex, col) in row.withIndex()) {
                if (!locationBoundaryMap.any { region -> region.key.contains(Pair(rowIndex, colIndex))}) {
                    val region = GridUtils.getRegion(typed, col, rowIndex, colIndex)
                    val boundary = getBoundaryCount(region.toTypedArray(), col)
                    locationBoundaryMap[region] = boundary
                }
            }
        }
        var result = 0L
        for ((key, value) in locationBoundaryMap.entries) {
            result += key.size * value
        }
        return result
    }

    fun solveSecondPart(): Long {
        val locationBoundaryMap = mutableMapOf<Set<Pair<Int, Int>>, Int>()
        for ((rowIndex, row) in input.withIndex()) {
            for ((colIndex, col) in row.withIndex()) {
                if (!locationBoundaryMap.any { region -> region.key.contains(Pair(rowIndex, colIndex))}) {
                    val region = GridUtils.getRegion(typed, col, rowIndex, colIndex)
                    val boundary = getBoundaryCount(region.toTypedArray(), col)
                    locationBoundaryMap[region] = boundary
                }
            }
        }
        var result = 0L
        for ((key, _) in locationBoundaryMap.entries) {
            val boundariesWithView = getBoundaryWithView(key.toTypedArray()).toTypedArray()
            val sideCount = getSideCount(boundariesWithView)
            result += key.size * sideCount
        }
        return result
    }

    private fun getBoundaryCount(region: Array<Pair<Int, Int>>, flower: Char): Int {
        val directions = GridUtils.basicDirections
        var result = 0
        for (loc in region) {
            for (dir in directions) {
                val neighbourLoc = GridUtils.move(loc, dir, 1)
                if (!GridUtils.isInGrid(typed, neighbourLoc)) {
                    result++
                    continue
                }
                if (input[neighbourLoc.first][neighbourLoc.second] != flower) {
                    result++
                }
            }
        }
        return result
    }

    private fun getBoundaryWithView(region: Array<Pair<Int, Int>>): Set<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val flower = input[region[0].first][region[0].second]
        val directions = GridUtils.basicDirections
        var result = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (loc in region) {
            for (dir in directions) {
                val neighbourLoc = GridUtils.move(loc, dir, 1)
                if (!GridUtils.isInGrid(typed, neighbourLoc)) {
                    result.add(Pair(loc, neighbourLoc))
                    continue
                }
                if (input[neighbourLoc.first][neighbourLoc.second] != flower) {
                    result.add(Pair(loc, neighbourLoc))
                }
            }
        }
        return result
    }

    private fun getSideCount(boundariesWithView: Array<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        var sideCount = 0
        val boundariesWithDirection = boundariesWithView.map { Pair(it.first, it.second - it.first) }
        val groupedByDir = boundariesWithDirection.groupBy { it.second }
        assert(groupedByDir.size == 4)
        for ((direction, group) in groupedByDir) {
            var locs = group.map { it.first }
            val sortedLocs: List<Pair<Int, Int>>
            if (direction.first == 0) {
                sortedLocs = locs.sortedWith(compareBy({it.second}, {it.first}))
            } else {
                sortedLocs = locs.sortedWith(compareBy({it.first}, {it.second}))
            }
            var prevLoc = sortedLocs[0]
            sideCount += 1
            for (loc in sortedLocs.subList(1, sortedLocs.size)) {
                if (direction.first == 0 && (Math.abs(loc.first - prevLoc.first) != 1 || loc.second != prevLoc.second)) {
                    sideCount += 1
                } else if (direction.second == 0 && (Math.abs(loc.second - prevLoc.second) != 1 || loc.first != prevLoc.first)) {
                    sideCount += 1
                }
                prevLoc = loc
            }
        }
        return sideCount
    }
}
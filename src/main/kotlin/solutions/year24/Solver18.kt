package solutions.year24

import utils.GraphUtils
import utils.GridUtils.basicDirections
import utils.GridUtils.isInGrid
import utils.GridUtils.plus

class Solver18 {

    val gridSize: Pair<Int, Int>
    val fallingByteLocations: Array<Pair<Int, Int>>

    constructor(gridSize: Pair<Int, Int>, fallingByteLocations: Array<Pair<Int, Int>>) {
        this.gridSize = gridSize
        this.fallingByteLocations = fallingByteLocations
    }

    fun solveFirstPart(range: Int): Long {
        val locations = mutableListOf<Pair<Int, Int>>()

        for (i in 0..<gridSize.first) {
            for (j in 0..<gridSize.second) {
                val loc = Pair(i, j)
                val firstRange = fallingByteLocations.copyOfRange(0, range)
                if (loc !in firstRange) {
                    locations.add(loc)
                }
            }
        }
        val paths = mutableMapOf<Pair<Int, Int>, List<Pair<Int, Int>>>()
        for (loc in locations) {
            val neighbours = basicDirections.map { loc + it }
            val locPaths = mutableListOf<Pair<Int, Int>>()
            for (neighbour in neighbours) {
                if (isInGrid(gridSize, neighbour) && neighbour in locations) {
                    locPaths.add(neighbour)
                }
            }
            paths[loc] = locPaths
        }
        val (bfsDistances, _) = GraphUtils.breadthFirstSearchDistancesAndPrevMap(paths, Pair(0, 0))
        if (bfsDistances.contains(Pair(70, 70))) {
            return bfsDistances[Pair(70, 70)]!!.toLong()
        }
        return -1
    }

    fun solveSecondPart(): String {
        var index = fallingByteLocations.size / 2
        var incrementDecrement = index / 2
        while (true) {
            if (incrementDecrement == 0) {
                val fallingByte = fallingByteLocations[index - 1]
                return "${fallingByte.first},${fallingByte.second}"
            }
            if (solveFirstPart(index) != -1L) {
                index += incrementDecrement
            }
            else {
                index -= incrementDecrement
            }
            incrementDecrement /= 2
        }
    }
}
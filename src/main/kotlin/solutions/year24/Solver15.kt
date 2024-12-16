package solutions.year24;

import utils.GridUtils
import utils.GridUtils.LEFT
import utils.GridUtils.UP
import utils.GridUtils.DOWN
import utils.GridUtils.RIGHT
import utils.GridUtils.locationsOf

class Solver15(val grid: Array<Array<Char>>, val moves: Array<Char>) {
    val agentCh = '@'
    val boxCh = 'O'
    val bigBoxChL = '['
    val bigBoxChR = ']'
    val wallCh = '#'
    val spaceCh = '.'
    val gridSize = Pair(grid.size, grid[0].size)

    val directionsMap = mapOf(Pair('<', LEFT), Pair('^', UP), Pair('>', RIGHT), Pair('v', DOWN))
    var agentLoc = GridUtils.locationsOf(grid, agentCh)[0]
    val initAgentLoc = agentLoc

    fun solveFirstPart(): Long {
        val copy = grid.map { it.copyOf() }.toTypedArray()
        for (move in moves) {
            val dir = directionsMap[move]
            requireNotNull(dir)
            val lineInDirection = GridUtils.getLocationsInDirection(gridSize, agentLoc, dir)
            processLine(copy, lineInDirection, dir)
        }
        return calculateBoxDistances(copy)
    }

    fun solveSecondPart(): Long {
        agentLoc = Pair(initAgentLoc.first, initAgentLoc.second * 2)
        val doubledCopy = grid.map { Array(gridSize.second * 2) { '.' } }.toTypedArray()
        val doubledGridSize = Pair(doubledCopy.size, doubledCopy[0].size)
        val transformMap = mapOf(Pair('#', "##"), Pair('O', "[]"), Pair('.', ".."), Pair('@', "@."))
        for ((rowIdx, row) in grid.withIndex()) {
            for ((colIdx, elem) in row.withIndex()) {
                val transformed = transformMap[elem]
                doubledCopy[rowIdx][colIdx * 2] = transformed?.get(0)!!
                doubledCopy[rowIdx][colIdx * 2 + 1] = transformed[1]
            }
        }
        for (move in moves) {
            val dir = directionsMap[move]
            requireNotNull(dir)
            if (dir == LEFT || dir == RIGHT) {
                val lineInDirection = GridUtils.getLocationsInDirection(doubledGridSize, agentLoc, dir)
                processLine(doubledCopy, lineInDirection, dir)
            } else {
                processBiggerBoxes(doubledCopy, dir)
            }

        }
        return calculateBoxDistances(doubledCopy)
    }

    // Move all boxes, and agent accordingly
    private fun processLine(input: Array<Array<Char>>, line: List<Pair<Int, Int>>, direction: Pair<Int, Int>) {
        val lineChars = line.map { input[it.first][it.second] }

        val firstSpace = lineChars.indexOf(spaceCh)
        if (firstSpace == -1) {
            return
        }
        val firstWall = lineChars.indexOf(wallCh)
        if (firstWall < firstSpace) {
            return
        }
        // Move boxes
        for (i in firstSpace-1 downTo 0) {
            val loc = line[i]
            val locNext = line[i + 1]
            input[locNext.first][locNext.second] = input[loc.first][loc.second]
        }
        // Move agent
        val firstLoc = line[0]
        input[firstLoc.first][firstLoc.second] = agentCh
        input[agentLoc.first][agentLoc.second] = spaceCh
        agentLoc = firstLoc
    }

    private fun calculateBoxDistances(input: Array<Array<Char>>): Long {
        val locations = locationsOf(input, boxCh)
        val locationsOfBigBox = locationsOf(input, bigBoxChL)
        return locations.sumOf { it.first.toLong() * 100L + it.second.toLong() } + locationsOfBigBox.sumOf { it.first.toLong() * 100L + it.second.toLong()}
    }

    private fun processBiggerBoxes(input: Array<Array<Char>>, direction: Pair<Int, Int>) {
        val toBeMovedRegion = mutableSetOf(agentLoc)

        // Search iteratively all locations ahead of the "front" starting with only the agent's location
        // If there is any wall, the agent can't move,
        // If there are only spaces, the whole region can move
        // If there are spaces and boxes, add only boxes to the region, and change the "front" to the boxes

        var front = mutableSetOf(agentLoc)
        var depth = 0
        while (depth < 10000) {
            val frontAheadLocations = front.map { GridUtils.move(it, direction, 1) }
            val frontAhead = frontAheadLocations.associateWith { input[it.first][it.second] }
            if (frontAhead.values.contains(wallCh)) {
                return
            }
            if (frontAhead.values.all { it == spaceCh }) {
                // Perform the move - modify the grid
                val valuesMap = toBeMovedRegion.associateWith { input[it.first][it.second] }
                val movedLocationsMap = toBeMovedRegion.associateWith { GridUtils.move(it, direction, 1) }
                toBeMovedRegion.forEach { input[it.first][it.second] = spaceCh }
                for (toBeMovedLocation in toBeMovedRegion) {
                    val movedLocation = movedLocationsMap[toBeMovedLocation]
                    val value = valuesMap[toBeMovedLocation]
                    requireNotNull(movedLocation)
                    requireNotNull(value)
                    input[movedLocation.first][movedLocation.second] = value
                }
                agentLoc = GridUtils.move(agentLoc, direction, 1)
                return
            }
            front = mutableSetOf()
            val leftBoxesLocations = frontAhead.filter { it.value == bigBoxChL }.keys
            val rightBoxesLocations = frontAhead.filter { it.value == bigBoxChR }.keys
            val leftBoxesRightSides = leftBoxesLocations.map { GridUtils.move(it, RIGHT, 1) }
            val rightBoxesLeftSides = rightBoxesLocations.map { GridUtils.move(it, LEFT, 1) }
            front.addAll(leftBoxesLocations)
            front.addAll(leftBoxesLocations.map { GridUtils.move(it, RIGHT, 1) })
            front.addAll(rightBoxesLocations)
            front.addAll(rightBoxesLocations.map { GridUtils.move(it, LEFT, 1) })
            toBeMovedRegion.addAll(leftBoxesLocations)
            toBeMovedRegion.addAll(rightBoxesLocations)
            toBeMovedRegion.addAll(leftBoxesRightSides)
            toBeMovedRegion.addAll(rightBoxesLeftSides)

            depth += 1
        }
    }
}

package solutions.year24

import utils.GraphUtils
import utils.GridUtils
import utils.GridUtils.RIGHT
import utils.GridUtils.basicDirections
import utils.GridUtils.locationsOf

class Solver16(val grid: Array<Array<Char>>) {
    val wallCh = '#'
    val spaceCh = '.'

    fun solveFirstPart(): Long {
        val endLocs = locationsOf(grid, 'E')
        val startLocs = locationsOf(grid, 'S')
        require(endLocs.size == 1)
        require(startLocs.size == 1)
        val startDirection = RIGHT
        val graph = createGraph(grid, endLocs[0])
        val startPoint = toGraphPoint(startLocs[0], startDirection, grid[0].size)
        val endPoints = basicDirections.map{ toGraphPoint(endLocs[0], it, grid[0].size) }
        val dijkstra = GraphUtils.dijkstraWithLoops(graph, startPoint)
        val dijkstraFilterEndLocation = (dijkstra.filter { it.key in endPoints }.values)
        return dijkstraFilterEndLocation.first().toLong()
    }

    fun solveSecondPart(): Long {
        val endLocs = locationsOf(grid, 'E')
        val startLocs = locationsOf(grid, 'S')
        require(endLocs.size == 1)
        require(startLocs.size == 1)
        val startDirection = RIGHT
        val graph = createGraph(grid, endLocs[0])
        val startPoint = toGraphPoint(startLocs[0], startDirection, grid[0].size)
        val endPoints = basicDirections.map{ toGraphPoint(endLocs[0], it, grid[0].size) }
        val dijkstraDistanceAndPaths = GraphUtils.dijkstraWithLoopsAndPaths(graph, startPoint, endPoints[0])
        val prevs = dijkstraDistanceAndPaths.second.map { it.second.toSet() }.reduce { s1, s2 -> s1.union(s2) }
        val countOfPoints = prevs.map { it / 4 }.toSet().size
        return countOfPoints.toLong()
    }

    /**
     * Creates a graph by creating a unique integer (a fingerprint) for each location pair and direction pair.
     * There are 4 basic directions taken into account, @see [GridUtils] for more. The directions are mapped one-to-one
     * to an int range { (UP, 0), (RIGHT, 1), (DOWN, 2), (LEFT, 3) }.
     *
     * The fingerprints, which are used as vertices of the graph, are then calculated as
     *
     * $`fingerprint = {row size} * X + Y) * 4 + {direction map value}`$
     */
    private fun createGraph(grid: Array<Array<Char>>, endLoc: Pair<Int, Int>): Map<Int, List<Pair<Int, Int>>> {
        val graph = mutableMapOf<Int, List<Pair<Int, Int>>>()
        val gridRowSize = grid[0].size
        val endPointLoc = toGraphPoint(endLoc, RIGHT, gridRowSize) / 4
        val directionIndices = IntRange(0, 3)
        val points = mutableListOf<Int>()

        // Add points
        for ((rowIndex, row) in grid.withIndex()) {
            for ((colIndex, chr) in row.withIndex()) {
                if (chr != wallCh) {
                    val loc = Pair(rowIndex, colIndex)
                    val graphLocIndex = GridUtils.oneDimensionalIndex(gridRowSize, loc) * 4
                    for (directionIdx in directionIndices) {
                        points.add(graphLocIndex + directionIdx)
                    }
                }
            }
        }
        // Add edges
        for (point in points) {
            val pointLocIndex = point / 4
            val dir = point % 4

            val turnLeftDir = Math.floorMod(dir - 1, 4)
            val turnRightDir = Math.floorMod(dir + 1, 4)
            val turnedLeftPoint = pointLocIndex * 4 + turnLeftDir
            val turnedRightPoint = pointLocIndex * 4 + turnRightDir

            val edges: MutableList<Pair<Int, Int>> = if (pointLocIndex == endPointLoc) {
                mutableListOf(Pair(turnedRightPoint, 0), Pair(turnedLeftPoint, 0))
            } else {
                mutableListOf(Pair(turnedRightPoint, 1000), Pair(turnedLeftPoint, 1000))
            }

            val (location, direction) = toLocationDirectionPair(point, gridRowSize)
            val neighbourLoc = GridUtils.move(location, direction, 1)
            if (grid[neighbourLoc.first][neighbourLoc.second] != wallCh) {
                val neighbourPoint = toGraphPoint(neighbourLoc, direction, gridRowSize)
                edges.add(Pair(neighbourPoint, 1))
            }

            graph[point] = edges
        }
        return graph
    }

    private fun toGraphPoint(location: Pair<Int, Int>, direction: Pair<Int, Int>, gridRowSize: Int): Int {
        val indicesRange = IntRange(0, 3)
        val directionIndicesMap = basicDirections.zip(indicesRange).toMap()
        return (GridUtils.oneDimensionalIndex(gridRowSize, location) * 4 + directionIndicesMap[direction]!!)
    }

    private fun toLocationDirectionPair(graphPoint: Int, gridRowSize: Int): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val indicesRange = IntRange(0, 3)
        val indicesDirectionMap = indicesRange.zip(basicDirections).toMap()
        val pointLocIndex = graphPoint / 4
        val dirIncrement = graphPoint % 4
        val location = GridUtils.twoDimensionalIndex(grid[0].size, pointLocIndex)
        val direction = indicesDirectionMap[dirIncrement]!!
        return Pair(location, direction)
    }
}
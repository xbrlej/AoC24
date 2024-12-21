package solutions.year24

import utils.GraphUtils
import utils.GridUtils.basicDirections
import utils.GridUtils.getAllLocationsInDistance
import utils.GridUtils.gridAbsDistance
import utils.GridUtils.isInGrid
import utils.GridUtils.locationsOf
import utils.GridUtils.oneDimensionalIndex
import utils.GridUtils.plus
import utils.GridUtils.twoDimensionalIndex

class Solver20(val grid: Array<Array<Char>>) {
    val wallCh = '#'
    val spaceCh = '.'
    val gridSize = Pair(grid.size, grid[0].size)

    fun solveFast(savedDiff: Int, cheatDistances: Int): Int {
        val endLocs = locationsOf(grid, 'E')
        val startLocs = locationsOf(grid, 'S')
        require(endLocs.size == 1)
        require(startLocs.size == 1)
        val startVertex = startLocs[0].first * gridSize.first + startLocs[0].second
        val endVertex = endLocs[0].first * gridSize.first + endLocs[0].second
        val graph = createGraph()
        val forwardDijkstra = GraphUtils.dijkstraWithLoops(graph, startVertex)
        val backwardDijkstra = GraphUtils.dijkstraWithLoops(graph, endVertex)
        val pathLen = forwardDijkstra[endVertex]!!
        var counter = 0
        for (point in graph.keys) {
            val pointLoc = twoDimensionalIndex(gridSize.first, point)
            val cheatNeighbourPoints = getAllLocationsInDistance(gridSize, pointLoc, cheatDistances)
                .filter { grid[it.first][it.second] != wallCh }
                .map { oneDimensionalIndex(gridSize.first, it) }
            for (cheatNeighbour in cheatNeighbourPoints) {
                val cheatPathCost =
                    forwardDijkstra[point]!! +
                    gridAbsDistance(twoDimensionalIndex(gridSize.first, point), twoDimensionalIndex(gridSize.first, cheatNeighbour)) +
                    backwardDijkstra[cheatNeighbour]!!
                if (cheatPathCost <= pathLen - savedDiff) {
                    counter += 1
                }
            }
        }
        return counter
    }

    // Original solution, slow
    fun solveFirstPart(): Int {
        val endLocs = locationsOf(grid, 'E')
        val startLocs = locationsOf(grid, 'S')
        require(endLocs.size == 1)
        require(startLocs.size == 1)
        val startVertex = startLocs[0].first * gridSize.first + startLocs[0].second
        val endVertices = listOf(
            endLocs[0].first * gridSize.first + endLocs[0].second,
            endLocs[0].first * gridSize.first + endLocs[0].second + gridSize.first * gridSize.second)

        // Calculate non cheated path length
        val graph = createGraph()
        val dijkstraUncheated = GraphUtils.dijkstraWithLoops(graph, startVertex)
        val uncheatedDistance = dijkstraUncheated[endVertices[0]]!!

        val graphWithCheats = createGraphWithCheats(2, dijkstraUncheated)
        var dijkstra = GraphUtils.dijkstraWithLoopsAndPaths(graphWithCheats, startVertex, endVertices[1])
        var cheatedDistance = dijkstra.first
        var cheatCount = 0
        val knownCheats = mutableListOf<Pair<Int, Int>>()
        while (uncheatedDistance - cheatedDistance >= 64) {
            val wholePath = dijkstra.second
            val cheats = findUsedCheat(wholePath)
            cheatCount += cheats.size
            for (cheat in cheats) {
                graphWithCheats[cheat.first]?.remove(Pair(cheat.second, 2))
                knownCheats.add(cheat)
            }
            dijkstra = GraphUtils.dijkstraWithLoopsAndPaths(graphWithCheats, startVertex, endVertices[1])
            cheatedDistance = dijkstra.first
        }

        return cheatCount
    }

    // Original solution, very slow, takes about 2-5 minutes
    fun solveSecondPart(): Int {
        val secondGraphOffset = gridSize.first * gridSize.second
        val endLocs = locationsOf(grid, 'E')
        val startLocs = locationsOf(grid, 'S')
        require(endLocs.size == 1)
        require(startLocs.size == 1)
        val startVertex = startLocs[0].first * gridSize.first + startLocs[0].second
        val endVertices = listOf(
            endLocs[0].first * gridSize.first + endLocs[0].second,
            endLocs[0].first * gridSize.first + endLocs[0].second + gridSize.first * gridSize.second)

        // Calculate non cheated path length
        val graph = createGraph()
        val dijkstraUncheated = GraphUtils.dijkstraWithLoops(graph, startVertex)
        val uncheatedDistance = dijkstraUncheated[endVertices[0]]!!

        val graphWithCheats = createGraphWithCheats(20, dijkstraUncheated)
        var dijkstra = GraphUtils.dijkstraWithLoopsAndPaths(graphWithCheats, startVertex, endVertices[1])
        var cheatedDistance = dijkstra.first
        var cheatCount = 0
        val knownCheats = mutableListOf<Pair<Int, Int>>()
        while (uncheatedDistance - cheatedDistance >= 100) {
            val wholePath = dijkstra.second
            val cheats = findUsedCheat(wholePath)
            cheatCount += cheats.size
            for (cheat in cheats) {
                val locA = twoDimensionalIndex(gridSize.first, cheat.first)
                val locB = twoDimensionalIndex(gridSize.first, cheat.second - secondGraphOffset)
                val dist = gridAbsDistance(locA, locB)
                graphWithCheats[cheat.first]?.remove(Pair(cheat.second, dist))
                knownCheats.add(cheat)
            }
            dijkstra = GraphUtils.dijkstraWithLoopsAndPaths(graphWithCheats, startVertex, endVertices[1])
            cheatedDistance = dijkstra.first
        }

        return cheatCount
    }

    fun createGraph(): Map<Int, List<Pair<Int, Int>>> {
        val vertices = mutableSetOf<Int>()
        val edges = mutableMapOf<Int, List<Pair<Int, Int>>>()
        for ((rowIdx, row) in grid.withIndex()) {
            for ((colIdx, element) in row.withIndex()) {
                if (element != wallCh) {
                    val vertex = rowIdx * gridSize.first + colIdx
                    vertices.add(vertex)
                }
            }
        }
        for (vertex in vertices) {
            val location = twoDimensionalIndex(gridSize.first, vertex)
            val neighbours = basicDirections
                .map { location + it }
                .filter{ isInGrid(gridSize, it) && grid[it.first][it.second] != wallCh }
            val edgesForLocation = neighbours.map { Pair(oneDimensionalIndex(gridSize.first, it), 1) }
            edges[vertex] = edgesForLocation
        }
        return edges
    }
    
    fun createGraphWithCheats(maxMoves: Int, basicGraph: Map<Int, Int>): MutableMap<Int, MutableList<Pair<Int, Int>>> {
        val vertices = mutableSetOf<Int>()
        val edges = mutableMapOf<Int, MutableList<Pair<Int, Int>>>()
        val secondGraphOffset = gridSize.first * gridSize.second
        for ((rowIdx, row) in grid.withIndex()) {
            for ((colIdx, element) in row.withIndex()) {
                if (element != wallCh) {
                    val vertex = rowIdx * gridSize.first + colIdx
                    vertices.add(vertex)
                    vertices.add(vertex + secondGraphOffset)
                }
            }
        }
        for (vertex in vertices) {
            if (vertex < secondGraphOffset) {
                val location = twoDimensionalIndex(gridSize.first, vertex)
                val neighboursInFirstGraph = basicDirections
                    .map { location + it }
                    .filter{ isInGrid(gridSize, it) && grid[it.first][it.second] != wallCh }
                val neighboursInSecondGraph = getAllLocationsInDistance(gridSize, location, maxMoves)
                    .filter { grid[it.first][it.second] != wallCh }
                    .associateWith { gridAbsDistance(location, it) }
                    .filter { it.value > 1 }
                val edgesForLocation = neighboursInFirstGraph.map { Pair(oneDimensionalIndex(gridSize.first, it), 1) }.toMutableList()
                edgesForLocation.addAll(neighboursInSecondGraph.map { Pair(oneDimensionalIndex(gridSize.first, it.key) + secondGraphOffset, it.value) })
                edges[vertex] = edgesForLocation

            } else {
                val vertexInFirstGraph = vertex - secondGraphOffset
                val location = twoDimensionalIndex(gridSize.first, vertexInFirstGraph)
                val neighbours = basicDirections
                    .map { location + it }
                    .filter{ isInGrid(gridSize, it) && grid[it.first][it.second] != wallCh }
                val edgesForLocation = neighbours.map { Pair(oneDimensionalIndex(gridSize.first, it) + secondGraphOffset, 1) }.toMutableList()
                edges[vertex] = edgesForLocation
            }
        }
        return edges
    }
    
    fun findUsedCheat(prevList: List<Pair<Int, List<Int>>>): Set<Pair<Int, Int>> {
        val secondGraphOffset = gridSize.first * gridSize.second
        val filtered = prevList.filter { it.first > secondGraphOffset }
            .filter { pair -> pair.second.any { it < secondGraphOffset } }
            .map { pair -> Pair(pair.first, pair.second.filter { it < secondGraphOffset })  }
        return filtered.map { pair -> pair.second.map { Pair(it, pair.first) }.toSet() }.reduce {s1, s2 -> s1.union(s2)}
    }
}
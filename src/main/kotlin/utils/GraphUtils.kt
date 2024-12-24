package utils

import java.util.*
import kotlin.collections.HashSet

object GraphUtils {

    fun<T> breadthFirstSearchDistancesAndPrevMap(graph: Map<T, List<T>>, start: T): Pair<Map<T, Int>, Map<T, List<T>>> {
        val queue = LinkedList<T>()
        val visitedFromPrevious = mutableMapOf<T, MutableList<T>>()
        val distances = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
        queue.add(start)
        visitedFromPrevious[start] = mutableListOf()
        distances[start] = 0

        while (queue.isNotEmpty()) {
            val node = queue.poll()
            graph[node]?.forEach {
                if (!visitedFromPrevious.contains(it)) {
                    queue.add(it)
                    visitedFromPrevious[it] = mutableListOf(node)
                    distances[it] = distances[node]?.plus(1)!!
                } else if (distances[it] == distances[node]?.plus(1)!!) {
                    visitedFromPrevious[it]?.add(node)
                }
            }
        }
        return Pair(distances, visitedFromPrevious)
    }

    fun<T> getGraphPaths(prevMap: Map<T, List<T>>, start: T, end: T, length: Int): Set<List<T>> {
        val tmpPath = MutableList<T>(length + 1) { null as T }
        tmpPath[0] = start
        tmpPath[length] = end
        val paths = mutableSetOf<List<T>>()
        getPathsRec(prevMap, end, length, 1, tmpPath, paths)
        return paths
    }

    private fun<T> getPathsRec(prevMap: Map<T, List<T>>, tail: T, length: Int, index: Int, tmpPath: MutableList<T>, paths: MutableSet<List<T>>) {
        if (length <= index) {
            paths.add(tmpPath.toList())
            return
        }
        for (prev in prevMap[tail]!!) {
            tmpPath[length - index] = prev
            getPathsRec(prevMap, prev, length, index + 1, tmpPath, paths)
        }
    }

    fun<T> dijkstraWithLoops(graph: Map<T, List<Pair<T, Int>>>, start: T): Map<T, Int> {
        val distances = mutableMapOf<T, Int>().withDefault { Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<T, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<T, Int>>()

        priorityQueue.add(start to 0)
        distances[start] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist) = priorityQueue.poll()
            if (visited.add(node to currentDist)) {
                graph[node]?.forEach { (adjacent, weight) ->
                    val totalDist = currentDist + weight
                    if (totalDist < distances.getValue(adjacent)) {
                        distances[adjacent] = totalDist
                        priorityQueue.add(adjacent to totalDist)
                    }
                }
            }
        }
        return distances
    }

    // Modification of the method above
    fun dijkstraWithLoopsAndPaths(graph: Map<Int, List<Pair<Int, Int>>>, start: Int, end: Int): Pair<Int, MutableList<Pair<Int, MutableList<Int>>>> {
        val distances = mutableMapOf<Int, Pair<Int, MutableList<Int>>>().withDefault { Pair(Int.MAX_VALUE, mutableListOf()) }
        val priorityQueue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<Int, Int>>()

        priorityQueue.add(start to 0)

        // Distances now store all the best previous nodes found during relaxation
        distances[start] = Pair(0, mutableListOf())

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist) = priorityQueue.poll()
            if (visited.add(node to currentDist)) {
                graph[node]?.forEach { (adjacent, weight) ->
                    val totalDist = currentDist + weight
                    if (totalDist < distances.getValue(adjacent).first) {
                        distances[adjacent] = Pair(totalDist, mutableListOf(node))
                        priorityQueue.add(adjacent to totalDist)
                    } else if (totalDist == distances.getValue(adjacent).first) {
                        distances[adjacent]?.second?.add(node)
                    }
                }
            }
        }
        val endDistAndPrev = distances[end]
        requireNotNull(endDistAndPrev)
        var prevs = endDistAndPrev.second
        val visitedPrevs = mutableSetOf(end)
        val prevList = mutableListOf(Pair(end, prevs))
        while (prevs.isNotEmpty()) {
            val newPrevs = mutableListOf<Int>()
            for (prev in prevs) {
                if (prev !in visitedPrevs) {
                    visitedPrevs.add(prev)
                    newPrevs.addAll(distances[prev]!!.second)
                }
                prevList.add(Pair(prev, distances[prev]!!.second))
            }
            prevs = newPrevs
        }
        return Pair(endDistAndPrev.first, prevList)
    }

    // Requires (x, x) connection in graph to exist
    inline fun<reified T> cliquesOfFixedSize(graph: Map<T, List<T>>, size: Int): Set<Set<T>> {
        val allCombinations = CombinatoricsUtils.getCombinations(graph.keys.toTypedArray(), size)
        val cliques = mutableSetOf<Set<T>>()
        for (combination in allCombinations) {
            var isClique = true
            for (element in combination) {
                if (!graph[element]!!.containsAll(combination)) {
                    isClique = false
                    break
                }
            }
            if (isClique) {
                cliques.add(combination.toSet())
            }
        }
        return cliques
    }

    fun<T> maximumClique(graph: Map<T, List<T>>): Set<T> {
        val allCliques = allCliquesBronKerboschMethod(mutableSetOf(), graph.keys.toMutableSet(), mutableSetOf(), graph)
        return allCliques.sortedByDescending { it.size }[0]
    }


    // Bron Kerbosch Algorithm (currentClique = R, potentialVertical = P, notIncluded = X)
    // Pseudocode https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
    private fun<T> allCliquesBronKerboschMethod(currentClique: Set<T>, potentialVertices: MutableSet<T>, notIncluded: MutableSet<T>, graph: Map<T, List<T>>): Set<Set<T>> {
        val cliques = mutableSetOf<Set<T>>()
        if (potentialVertices.isEmpty() && notIncluded.isEmpty()) {
            cliques.add(currentClique)
        }
        while (potentialVertices.isNotEmpty()) {
            val processedVertex = potentialVertices.first()
            val newCurrent = HashSet(currentClique)
            newCurrent.add(processedVertex)
            val processedEdges = graph[processedVertex]!!.toMutableSet()
            // Remove self edge if exists
            processedEdges.remove(processedVertex)
            val newPotential = HashSet(potentialVertices)
            newPotential.retainAll(processedEdges)
            val newNotIncluded = HashSet(notIncluded)
            newNotIncluded.retainAll(processedEdges)
            cliques.addAll(allCliquesBronKerboschMethod(newCurrent, newPotential, newNotIncluded, graph))
            potentialVertices.remove(processedVertex)
            notIncluded.add(processedVertex)
        }
        return cliques
    }
}
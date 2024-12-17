package utils

import java.util.*

object GraphUtils {

    // NOT MY CODE, this is the simplest dijkstra in kotlin, reference https://www.baeldung.com/kotlin/dijkstra-algorithm-graphs
    fun dijkstraWithLoops(graph: Map<Int, List<Pair<Int, Int>>>, start: Int): Map<Int, Int> {
        val distances = mutableMapOf<Int, Int>().withDefault { Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<Int, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<Int, Int>>()

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
    fun dijkstraWithLoopsAndPathCount(graph: Map<Int, List<Pair<Int, Int>>>, start: Int, end: Int): Pair<Int, Set<Int>> {
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
        val result = mutableSetOf<Int>()
        var prevs = endDistAndPrev.second
        while (prevs.isNotEmpty()) {
            val newPrevs = mutableSetOf<Int>()
            for (prev in prevs) {
                if (prev in result) {
                    continue
                }
                result.add(prev)
                distances[prev]?.let { newPrevs.addAll(it.second) }
            }
            prevs = newPrevs.toMutableList()
        }
        return Pair(endDistAndPrev.first, result)
    }
}
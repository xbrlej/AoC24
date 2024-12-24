package solutions.year24

import utils.GraphUtils

class Solver23(val links: List<Pair<String, String>>) {
    fun solveFirstPart(): Int {
        val graph = createGraph()
        val cliques = GraphUtils.cliquesOfFixedSize(graph, 3)
        return cliques.filter { it.any { it.startsWith("t") } }.size
    }

    fun solveSecondPart(): String {
        val graph = createGraph()
        val maxClique = GraphUtils.maximumClique(graph)
        return maxClique.sorted().joinToString(",")
    }

    fun createGraph(): Map<String, List<String>> {
        val edges = mutableMapOf<String, MutableList<String>>()
        for (link in links) {
            if (link.first in edges) {
                edges[link.first]!!.add(link.second)
            } else {
                edges[link.first] = mutableListOf(link.first, link.second)
            }
            if (link.second in edges) {
                edges[link.second]!!.add(link.first)
            } else {
                edges[link.second] = mutableListOf(link.second, link.first)
            }
        }
        return edges
    }
}
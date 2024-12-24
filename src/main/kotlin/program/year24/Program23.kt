package program.year24

import solutions.year24.Solver23
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = ResourceUtils.getResourceAsFile("day23-input.txt").readLines().map { it.split("-") }.map { Pair(it[0], it[1]) }
    val solver = Solver23(input)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}

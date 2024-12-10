package program.year24

import solutions.year24.Solver6
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val grid = InputUtils.parseInputFileByRow(
        ResourceUtils.getResourceAsFile("day6-input.txt"), false)
        .map { it.map{ str -> str[0] }.toCharArray() }.toTypedArray()

    val solver = Solver6(grid)

    var tripleA = Triple(0, 0, Pair(0, 0))
    var set = mutableSetOf(tripleA)
    var tripleB = Triple(0, 0, Pair(0, 0))
    set.add(tripleB)
    println(set.size)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
package program.year24

import solutions.year24.Solver18
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val rows = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day18-input.txt"), spaces = true)
    val intPairs = rows.map { it[0] }.map { Pair(it.split(",")[0].toInt(), it.split(",")[1].toInt()) }.toTypedArray()
    val gridSize = Pair(71, 71)

    val solver = Solver18(gridSize, intPairs)
    println(solver.solveFirstPart(1024))
    println(solver.solveSecondPart())
}
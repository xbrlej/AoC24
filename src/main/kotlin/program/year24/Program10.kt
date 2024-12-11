package program.year24

import solutions.year24.Solver10
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val grid = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day10-input.txt"), false).map { row ->
        row.map { it.toInt() }.toTypedArray()
    }.toTypedArray()

    val solver = Solver10(grid)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}

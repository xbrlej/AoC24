package program.year24

import solutions.year24.Solver16
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val grid = InputUtils.stringGridToCharGrid(InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day16-input.txt"), spaces = false))

    val solver = Solver16(grid)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
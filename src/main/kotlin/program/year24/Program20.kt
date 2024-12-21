package program.year24

import solutions.year24.Solver20
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {

    val grid = InputUtils.stringGridToCharGrid(InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day20-input.txt"), spaces = false))

    val solver = Solver20(grid)

    println(solver.solveFast(100, 2))
    println(solver.solveFast(100, 20))
}
package program.year24

import solutions.year24.Solver15
import utils.GridUtils
import utils.InputUtils
import utils.ResourceUtils
import view.GridView

fun main(args: Array<String>) {

    // Input split into two files beforehand
    val grid = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day15-grid-input.txt"), spaces = false)
        .map { it.map { it[0] }.toTypedArray() }.toTypedArray()
    val moves = ResourceUtils.getResourceAsFile("day15-moves-input.txt").readText().filter { it != '\n' }.toCharArray()

    val solver = Solver15(grid, moves.toTypedArray())
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
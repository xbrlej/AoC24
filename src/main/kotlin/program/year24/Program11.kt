package program.year24

import solutions.year24.Solver11
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day11-input.txt"), true)
    assert(input.size == 1)

    val solver = Solver11(input[0])
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
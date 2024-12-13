package program.year24

import solutions.year24.Solver12
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day12-input.txt"), false)

    val solver = Solver12(input.map { it.map { it[0] }.toCharArray() }.toTypedArray())
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
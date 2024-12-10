package program.year24

import solutions.year24.Solver8
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val inputStrings = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day8-input.txt"), false)
    val charGrid = inputStrings.map { it.map { it[0] }.toCharArray() }.toTypedArray()

    val solver = Solver8(charGrid)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
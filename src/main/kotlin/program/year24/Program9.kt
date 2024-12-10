package program.year24

import solutions.year24.Solver9
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = InputUtils.readFile(ResourceUtils.getResourceAsFile("day9-input.txt")).map { it.toString().toInt() }.toIntArray()

    val solver = Solver9(input)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
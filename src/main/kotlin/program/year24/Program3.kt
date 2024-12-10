package program.year24

import solutions.year24.Solver3
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = InputUtils.readFile(ResourceUtils.getResourceAsFile("day3-input.txt"))

    val solver = Solver3(input)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
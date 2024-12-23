package program.year24

import solutions.year24.Solver22
import utils.RegexConstants.INTEGERS
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = ResourceUtils.getResourceAsFile("day22-input.txt").readLines().map { INTEGERS.find(it)!!.value.toInt() }

    val solver = Solver22(input)
    println(solver.solveFirstPart())
    println(solver.solveSecondPartFaster())
}
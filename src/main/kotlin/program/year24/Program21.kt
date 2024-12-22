package program.year24

import solutions.year24.Solver21
import utils.RegexConstants.INTEGERS
import utils.ResourceUtils

fun main(args: Array<String>) {
    val input = ResourceUtils.getResourceAsFile("day21-input.txt").readLines().map { INTEGERS.find(it)!!.value.toInt() }
    val solver = Solver21(input)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart(25))
}
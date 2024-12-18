package program.year24

import solutions.year24.Solver17
import utils.RegexConstants
import utils.ResourceUtils

fun main(args: Array<String>) {
    val lines = ResourceUtils.getResourceAsFile("day17-input.txt").readLines()
    val registerA = RegexConstants.INTEGERS.find(lines[0])?.value?.toLong()!!
    val registerB = RegexConstants.INTEGERS.find(lines[1])?.value?.toLong()!!
    val registerC = RegexConstants.INTEGERS.find(lines[2])?.value?.toLong()!!
    val instructions = RegexConstants.INTEGERS.findAll(lines[4]).map { it.value.toLong() }.toList()

    val solver = Solver17(registerA, registerB, registerC, instructions)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
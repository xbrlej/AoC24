package program.year24

import solutions.year24.Solver19
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val rawInput = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day19-input.txt"), true)
    val subwords = rawInput[0].map { it.split(",")[0] }
    val words = rawInput.asList().subList(2, rawInput.size).map { it[0] }

    val solver = Solver19(subwords, words)

    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
package program.year24

import solutions.year24.Solver13
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val rawInput = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day13-input.txt"), spaces = true)
    val input = mutableListOf<Array<Int>>()
    for (i in IntRange(0, (rawInput.size) / 4)) {
        val idxPad = i * 4
        val aX = rawInput[0 + idxPad][2].split("+")[1].split(",")[0]
        val aY = rawInput[0 + idxPad][3].split("+")[1]
        val bX = rawInput[1 + idxPad][2].split("+")[1].split(",")[0]
        val bY = rawInput[1 + idxPad][3].split("+")[1]
        val prizeX = rawInput[2 + idxPad][1].split("=")[1].split(",")[0]
        val prizeY = rawInput[2 + idxPad][2].split("=")[1]

        input.add(arrayOf(aX, aY, bX, bY, prizeX, prizeY).map { it.toInt() }.toTypedArray())
    }

    val solver = Solver13(input)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
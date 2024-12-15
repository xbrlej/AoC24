package program.year24

import solutions.year24.Solver14
import utils.InputUtils
import utils.ResourceUtils
import utils.RegexConstants

fun main(args: Array<String>) {
    val rawInput = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day14-input.txt"), spaces = true)
    val input = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    for (row in rawInput) {
        val location = parseHelper(row[0])
        val direction = parseHelper(row[1])
        input.add(Pair(location, direction))
    }

    val gridSize = Pair(101, 103)
    val steps = 100

    val solver = Solver14(input.toTypedArray(), gridSize, steps)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}

private fun parseHelper(str: String): Pair<Int, Int> {
    val values = RegexConstants.INTEGERS.findAll(str).map { it.value.toInt() }.toList()
    require(values.size == 2)
    return Pair(values[0], values[1])
}
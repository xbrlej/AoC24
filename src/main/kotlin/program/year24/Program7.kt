package program.year24

import solutions.year24.Solver7
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    var rows = InputUtils.parseInputFileByRow(ResourceUtils.getResourceAsFile("day7-input.txt"), spaces=true)
    rows.forEach { row -> row[0] = row[0].split(":")[0] }
    var longs = rows.map { strings -> strings.map{ it.toLong() }.toLongArray() }.toTypedArray()

    var solver = Solver7(longs)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
package program.year24

import solutions.year24.Solver2
import utils.InputUtils
import utils.ResourceUtils.getResourceAsFile

fun main(args: Array<String>) {
    var inputFile = getResourceAsFile("day2-input.txt");
    var rows = InputUtils.parseInputFileByRow(inputFile, true)

    var solver = Solver2(rows)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
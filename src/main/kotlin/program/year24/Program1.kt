package program.year24

import solutions.year24.Solver1
import utils.InputUtils
import utils.ResourceUtils.getResourceAsFile

fun main(args: Array<String>) {
    var inputFile = getResourceAsFile("day1-input.txt");
    var columns = InputUtils.parseInputFileByColumn(inputFile, true)

    var solver = Solver1(columns)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
package program.year24

import solutions.year24.Solver4
import utils.ResourceUtils

fun main(args: Array<String>) {
    val file = ResourceUtils.getResourceAsFile("day4-input.txt")
    val lines = file.readLines()
    val matrix = lines.map { line -> line.toCharArray()}.toTypedArray()

    val word = "XMAS"

    var solver4 = Solver4(matrix, word.toCharArray())


    println(solver4.solveFirstPart())
    println(solver4.solveSecondPart())
}
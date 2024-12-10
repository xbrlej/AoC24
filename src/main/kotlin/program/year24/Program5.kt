package program.year24

import solutions.year24.Solver5
import utils.ResourceUtils

fun main(args: Array<String>) {
    val inputFile = ResourceUtils.getResourceAsFile("day5-input.txt")
    val lines = inputFile.readLines();
    val emptyLineIdx = lines.indexOf("")
    val ruleLines = lines.subList(0, emptyLineIdx)
    val updateLines = lines.subList(emptyLineIdx + 1, lines.size)

    val rules = ruleLines.map { it.split("|") }.map { Pair(it[0].toInt(), it[1].toInt()) }.toTypedArray()
    val updates = updateLines.map { it.split(",") }.map { it.map(String::toInt).toIntArray() }.toTypedArray()

    val solver = Solver5(rules, updates)
    println(solver.solveFirstPart())
    println(solver.solveSecondPart())
}
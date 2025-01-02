package program.year24

import solutions.year24.Solver25
import utils.InputUtils
import utils.ResourceUtils

fun main(args: Array<String>) {
    val lines = ResourceUtils.getResourceAsFile("day25-input.txt").readLines()
    val columnGrids = mutableListOf<Array<Array<Char>>>()

    for (i in 0..<lines.size / 8 + 1) {
        columnGrids.add(
            InputUtils.parseLinesByColumn(lines.subList(i * 8, (i+1) * 8 - 1), false)
                .map { row -> row.map { it[0] }.toTypedArray() }.toTypedArray()
        )
    }

    val solver = Solver25(columnGrids)
    println(solver.solveFirstPart())

}
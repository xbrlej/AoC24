package program.year24

import solutions.year24.Solver24
import utils.ResourceUtils

fun main(args: Array<String>) {
    val lines = ResourceUtils.getResourceAsFile("day24-input.txt").readLines()
    val splitLines = lines.map { it.split("\\s+".toRegex()).toTypedArray() }

    val values = mutableMapOf<String, Boolean>()
    val rules = mutableListOf<Pair<Triple<String, String, String>, String>>()

    for (splitLine in splitLines) {
        if (splitLine.size == 2) {
            values[splitLine[0].split(":")[0]] = splitLine[1].toInt() == 1
        }
        else if (splitLine.size == 5) {
            rules.add(Pair(Triple(splitLine[0], splitLine[1], splitLine[2]), splitLine[4]))
        }
    }

    val solver = Solver24(values, rules)
    println(solver.solveFirstPart())

    /**
     * Rare, solver does not return result, I did not have enough energy to finish this properly (it's christmas eve), part 2 solution
     * is the first three rules print out by this function and one of the last 4 rules (might differ slightly for different input),
     * filtering done manually by logic described in solver
     * @see [Solver24.findIncorrectRules] for more thoughts on how this "plus machine" works.
     */
    println(solver.solveSecondPart())
}
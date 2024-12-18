package solutions.year24

import kotlin.math.pow
import kotlin.math.truncate

class Solver17(val initRegA: Long, val initRegB: Long, val initRegC: Long, val instructions: List<Long>) {

    var regA = initRegA
    var regB = initRegB
    var regC = initRegC
    var output = mutableListOf<Long>()
    var outputSize = 0
    var instructionPointer = 0
    var remainderIndex = 0
    var remainderJump = 0

    val functionMap = (0L..7L).zip(listOf(
        ::adv,
        ::bxl,
        ::bst,
        ::jnz,
        ::bxc,
        ::out,
        ::bdv,
        ::cdv)
    ).toMap()

    fun solveFirstPart(cutEarly: Boolean = false): String {

        while (instructionPointer < instructions.size) {
            val instruction = instructions[instructionPointer]
            val operand = instructions[instructionPointer + 1]
            functionMap[instruction]?.invoke(operand)
            instructionPointer += 2
            if (cutEarly && outputSize > 0 && (outputSize > instructions.size || output[outputSize-1] != instructions[outputSize-1])) {
                return ""
            }
//            println("Action $instruction, $operand")
//            println("Reg A $regA")
//            println("Reg B $regB")
//            println("Reg C $regC")
//            println()
        }

        return output.joinToString(",")
    }

    fun solveSecondPart(): Long {
        return rebuildBackwardsRecursive(0, 0).second
    }

    private fun rebuildBackwardsRecursive(prevChecked: Long, index: Int): Pair<Boolean, Long> {
        if (index == instructions.size) {
            return Pair(true, prevChecked)
        }
        val size = instructions.size
        val expected = instructions.subList(size - 1 - index, size).joinToString(",")
        var rangeToBeChecked = LongRange(8 * prevChecked, 8 * (prevChecked + 1) - 1)
        for (possible in rangeToBeChecked) {
            reset(possible)
            // This number is possible for this tail, need to check all other variants in case the recursive call returns false
            if (solveFirstPart() == expected) {
                val tryRebuildOneLonger = rebuildBackwardsRecursive(possible, index + 1)
                if (tryRebuildOneLonger.first == true) {
                    return tryRebuildOneLonger
                }
            }
        }
        return Pair(false, prevChecked)
    }

    fun reset(newRegA: Long) {
        regA = newRegA
        regB = 0
        regC = 0
        output.clear()
        outputSize = 0
        instructionPointer = 0
        remainderIndex = 0
        remainderJump = 0
    }

    fun adv(operand: Long) {
        regA = dv(operand)
    }

    fun bxl(operand: Long) {
        regB = regB xor operand
    }

    fun bst(operand: Long) {
        regB = combo(operand) % 8
    }

    fun jnz(operand: Long) {
        if (regA == 0L) {
            return
        }
        instructionPointer = operand.toInt() - 2
    }

    fun bxc(operand: Long) {
        regB = (regB xor regC)
    }

    fun out(operand: Long) {
        output.add(combo(operand) % 8)
        outputSize += 1
    }

    fun bdv(operand: Long) {
        regB = dv(operand)
    }

    fun cdv(operand: Long) {
        regC = dv(operand)
        val futureRemainder = if (remainderIndex - remainderJump >= 0) instructions[remainderIndex - remainderJump] else 0

    }

    private fun dv(operand: Long): Long {
        val numerator = regA
        val denominator = 2.0.pow(combo(operand).toInt())
        return truncate((numerator / denominator)).toLong()
    }

    private fun combo(operand: Long): Long {
        if (operand in 0..3) {
            return operand
        }

        return when (operand) {
            4L -> regA
            5L -> regB
            6L -> regC
            7L -> throw IllegalArgumentException("Valid programs do not contain 7s.")
            else -> {
                throw UnknownError("Solver failed.")
            }
        }
    }
}
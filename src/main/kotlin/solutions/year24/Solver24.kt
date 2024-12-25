package solutions.year24

import utils.CombinatoricsUtils
import utils.RegexConstants.INTEGERS
import utils.RegexConstants.POSITIVE_INTEGERS
import kotlin.math.pow

class Solver24(val initValues: Map<String, Boolean>, val stringRules: List<Pair<Triple<String, String, String>, String>>) {

    var rules: MutableList<Pair<Triple<String, (Boolean, Boolean) -> Boolean, String>, String>>
    val stringRulesReverseIndexMap: Map<Pair<Triple<String, String, String>, String>, Int>
    var values = initValues.toMutableMap()

    val functionMap = mapOf<String, Function2<Boolean, Boolean, Boolean>>(
        "AND" to { a, b -> a && b },
        "OR" to { a, b -> a || b},
        "XOR" to { a, b -> a != b }
    )

    init {
        rules = stringRules.map { Triple(it.first.first, functionMap[it.first.second]!!, it.first.third) to it.second }.toMutableList()
        stringRulesReverseIndexMap = stringRules.withIndex().map { it.value to it.index }.toMap()
    }

    fun solveFirstPart(): Long {
        var counter = 0
        while (rules.isNotEmpty() && counter < stringRules.size * 2) {
            val applicableRules = rules.filter { it.first.first in values.keys && it.first.third in values.keys }
            for (rule in applicableRules) {
                val leftValue = values[rule.first.first]!!
                val rightValue = values[rule.first.third]!!
                val resultValue = rule.first.second.invoke(leftValue, rightValue)
                values[rule.second] = resultValue
                rules.remove(rule)
            }
            counter += 1
        }
        return getPrefixedNumber("z")
    }
    
    private fun reset() {
        values = initValues.toMutableMap()
        rules = stringRules.map { Triple(it.first.first, functionMap[it.first.second]!!, it.first.third) to it.second }.toMutableList()
    }

    private fun reset(stringRules: List<Pair<Triple<String, String, String>, String>>) {
        values = initValues.toMutableMap()
        rules = stringRules.map { Triple(it.first.first, functionMap[it.first.second]!!, it.first.third) to it.second }.toMutableList()
    }

    fun findIncorrectRules() {
        reset()
        solveFirstPart()
        val x = getPrefixedNumber("x")
        val y = getPrefixedNumber("y")
        val z = getPrefixedNumber("z")
        val expected = x + y

        // For my input, all the x<AB> inputs are always in a gate with y<AB> in XOR and AND gate -- sum
        // Z gates are all XOR except for z45 in my input, they should all be XOR because 1 + 1 = 0 and 1 carries over higher
        val modifiableStringRules = stringRules.toMutableList()
        val zIncorrectRules = mutableListOf<Pair<Triple<String, String, String>, String>>()
        for (rule in stringRules) {
            if (rule.second.contains("z") && !rule.first.second.contains("XOR")) {
                zIncorrectRules.add(rule)
            }
        }

        // Since all the x and y inputs are always paired in a gate, all the other rules should contain only carry-over
        // values on the left side. For these it should hold that they are either AND or OR if they don't have z on the
        // right side because there are only two possible operations for carry over values:
        // -- case one - AND operation between result of XOR on x y inputs (carry over of the current x y values) with carry-over
        //               value from the previous value
        // -- case two - OR operation between carry over from x AND y inputs and case one - results in a carry-over value for
        //               the current x y value
        val carryOverIncorrectRules = mutableListOf<Pair<Triple<String, String, String>, String>>()
        for (rule in stringRules) {
            if (!rule.second.contains("z") &&
                rule.first.second == "XOR" &&
                !(rule.first.first.contains(POSITIVE_INTEGERS) || rule.first.third.contains(POSITIVE_INTEGERS))
                ) {
                carryOverIncorrectRules.add(rule)
            }
        }

        // Found 7 rules not matching the conditions
        // Filter z45, because it is the highest value of z and it is constructed only from carry-over values
        zIncorrectRules.filter { rule -> rule.second == "z45" }

        // 6 incorrect rules remain
        // The rules found by z values should be changed by swapping with one of the carry-over incorrect rules
        val pairs = mutableListOf<Pair<Pair<Triple<String, String, String>, String>, Pair<Triple<String, String, String>, String>>>()
        for (carryRule in carryOverIncorrectRules) {
            val valueIndex: Int
            val filteredLeftOpRules = stringRules.filter { it.second == carryRule.first.first }
            require(filteredLeftOpRules.size == 1)
            val filteredRightOpRules = stringRules.filter { it.second == carryRule.first.third }
            require(filteredRightOpRules.size == 1)
            if (filteredLeftOpRules[0].first.first.contains(POSITIVE_INTEGERS)) {
                valueIndex = POSITIVE_INTEGERS.find(filteredLeftOpRules[0].first.first)!!.value.toInt()
            } else if (filteredRightOpRules[0].first.first.contains(POSITIVE_INTEGERS)) {
                valueIndex = POSITIVE_INTEGERS.find(filteredRightOpRules[0].first.first)!!.value.toInt()
            } else {
                throw UnknownError("Something happened.")
            }
            for (zRule in zIncorrectRules) {
                if (zRule.second.contains(valueIndex.toString())) {
                    pairs.add(Pair(carryRule, zRule))
                }
            }
        }
        println()

        // Found 3 pairs to be swapped
        // The last pair is found by brute force
        for (rulePair in pairs) {
            val swappedFirst = Pair(rulePair.first.first, rulePair.second.second)
            val swappedSecond = Pair(rulePair.second.first, rulePair.first.second)
            modifiableStringRules.remove(rulePair.first)
            modifiableStringRules.remove(rulePair.second)
            modifiableStringRules.add(swappedFirst)
            modifiableStringRules.add(swappedSecond)
            println("${rulePair.first} --- ${rulePair.second}")
        }

        reset(modifiableStringRules)
        val almostSolved = solveFirstPart()
        println(almostSolved xor expected)
        println((almostSolved xor expected).toString(2))
        // 111100000000000 11th value is bugged
        // brute force confirms this -- options
//        ((qff, OR, stw), wsv) --- ((ncw, XOR, qnw), z11)
//        ((x11, XOR, y11), qff) --- ((x11, AND, y11), qnw)
//        ((x11, XOR, y11), qff) --- ((ncw, XOR, qnw), z11)
//        ((ncw, XOR, qnw), z11) --- ((ncw, AND, qnw), stw)

        val possibleRulesLeft = modifiableStringRules.filter { rule ->
            !(rule.second.contains("z") && !rule.first.second.contains("XOR")) ||
                    !(!rule.second.contains("z") &&
                            rule.first.second == "XOR" &&
                            !(rule.first.first.contains(POSITIVE_INTEGERS) || rule.first.third.contains(POSITIVE_INTEGERS))
                            )
        }
        val ruleCombinations = CombinatoricsUtils.getCombinations(possibleRulesLeft.toTypedArray(), 2).map { Pair(it[0], it[1]) }.toMutableSet()
        for (rule in possibleRulesLeft) {
            val backtrackedSetOfrules = mutableSetOf<Pair<Triple<String, String, String>, String>>()
            backTrackRules(rule.second, backtrackedSetOfrules)
            for (backtrackedRule in backtrackedSetOfrules) {
                ruleCombinations.remove(Pair(rule, backtrackedRule))
                ruleCombinations.remove(Pair(backtrackedRule, rule))
            }
        }
        var i = 0
        for (ruleCombination in ruleCombinations) {
            i += 1
            val tmpModifiableStringRules = modifiableStringRules.toMutableList()
            val swappedFirst = Pair(ruleCombination.first.first, ruleCombination.second.second)
            val swappedSecond = Pair(ruleCombination.second.first, ruleCombination.first.second)
            tmpModifiableStringRules.remove(ruleCombination.first)
            tmpModifiableStringRules.remove(ruleCombination.second)
            tmpModifiableStringRules.add(swappedFirst)
            tmpModifiableStringRules.add(swappedSecond)
            reset(tmpModifiableStringRules)
            if (i % 100 == 0) {
                print("")
            }
            val result = solveFirstPart()
            if (result == expected) {
                println(result)
                println("${ruleCombination.first} --- ${ruleCombination.second}")
            }
        }

    }

    fun solveSecondPart(): String {
        findIncorrectRules()
        return ""
    }

    private fun getPrefixedNumber(prefix: String): Long {
        var result = 0L
        for ((key, value) in values.filter { it.key.startsWith(prefix) }) {
            if (value) {
                result += 2.0.pow(INTEGERS.find(key)!!.value.toInt()).toLong()
            }
        }
        return result
    }

    private fun backTrackRules(outputWire: String, suspectedRules: MutableSet<Pair<Triple<String, String, String>, String>>) {
        for (stringRule in stringRules) {
            if (stringRule.second == outputWire) {
                suspectedRules.add(stringRule)
                backTrackRules(stringRule.first.first, suspectedRules)
                backTrackRules(stringRule.first.third, suspectedRules)
            }
        }
    }
}
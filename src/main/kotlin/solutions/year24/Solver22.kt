package solutions.year24

import utils.CombinatoricsUtils
import java.time.Instant.now
import kotlin.math.abs
import kotlin.math.max

class Solver22(val input: List<Int>) {

    fun solveFirstPart(): Long {
        var result = 0L
        val distanceHistoryMap = mutableMapOf<Quadruple<Byte, Byte, Byte, Byte>, Byte>()
        for (number in input) {
            result += getNextNthSecretWithHistory(number, 2000, distanceHistoryMap)
        }
        return result
    }

    fun solveSecondPartFaster(): Int {
        println(now())
        val allUnsignedVariants = CombinatoricsUtils.getXNaryVariations(9, 4).map { it.toCharArray().map { it.toString().toInt() }.toIntArray() }
        val signVariations = CombinatoricsUtils.getXNaryVariations(2, 4).map {
            it.toCharArray().map { it.toString().toInt() }.map { if (it == 0) -1 else 1 }.toIntArray()
        }
        val signedVariants = mutableSetOf<Quadruple<Byte, Byte, Byte, Byte>>()
        for (unsignedVariant in allUnsignedVariants) {
            for (signVariation in signVariations) {
                val signedVariant = (unsignedVariant.zip(signVariation, Int::times).toTypedArray().map { it.toByte() }.toTypedArray())
                if (abs(signedVariant.sumOf{ it.toInt() }) < 10) {
                    signedVariants.add(quadrupleOf(signedVariant))
                }
            }
        }
        val mapOfHistories = mutableMapOf<Int, Map<Quadruple<Byte, Byte, Byte, Byte>, Byte>>()
        for (number in input) {
            val map = mutableMapOf<Quadruple<Byte, Byte, Byte, Byte>, Byte>()
            getNextNthSecretWithHistory(number, 2000, map)
            mapOfHistories[number] = map
        }
        var result = 0
        for (signedVariant in signedVariants) {
            var tmpResult = 0
            for (number in input) {
                val map = mapOfHistories[number]!!
                if (signedVariant in map.keys) {
                    tmpResult += map[signedVariant]!!.toInt()
                }
            }
            result = max(tmpResult, result)
        }
        println(now())
        return result
    }
    fun getNextSecret(number: Int): Int {
        val trimmer = 0xFFFFFF
        val applyFirst = ((number shl 6) and trimmer) xor number
        val applySecond = (applyFirst shr 5) xor applyFirst
        val applyThird = (applySecond shl 11 and trimmer) xor applySecond
        return applyThird
    }

    fun getNextNthSecret(number: Int, repeat: Int): Int {
        var result = number
        for (i in 0..<repeat) {
            result = getNextSecret(result)
        }
        return result
    }

    fun getNextNthSecretWithHistory(number: Int, repeat: Int, distanceHistoryMap: MutableMap<Quadruple<Byte, Byte, Byte, Byte>, Byte>): Int {
        if (repeat <= 3) {
            return getNextNthSecret(number, repeat)
        }
        var first = number
        var second = getNextSecret(number)
        var third = getNextSecret(second)
        var fourth = getNextSecret(third)
        var current = fourth
        for (i in 3..<repeat) {
            current = getNextSecret(current)
            val firstDist = (second % 10 - first % 10).toByte()
            val secondDist = (third % 10 - second % 10).toByte()
            val thirdDist = (fourth % 10 - third % 10).toByte()
            val fourthDist = (current % 10 - fourth % 10).toByte()
            val distanceHistory = Quadruple(
                firstDist,
                secondDist,
                thirdDist,
                fourthDist
            )
            if (distanceHistory !in distanceHistoryMap.keys) {
                distanceHistoryMap[distanceHistory] = (current % 10).toByte()
            }
            first = second
            second = third
            third = fourth
            fourth = current
        }
        return current
    }

    // Original solution, DO NOT USE, Extremely slow, I went to make breakfast
    fun solveSecondPart(): Long {
        println(now())
        val allUnsignedVariants = CombinatoricsUtils.getXNaryVariations(9, 4).map { it.toCharArray().map { it.toString().toInt() }.toIntArray() }
        val signVariations = CombinatoricsUtils.getXNaryVariations(2, 4).map {
            it.toCharArray().map { it.toString().toInt() }.map { if (it == 0) -1 else 1 }.toIntArray()
        }
        val signedVariants = mutableSetOf<Quadruple<Byte, Byte, Byte, Byte>>()
        for (unsignedVariant in allUnsignedVariants) {
            for (signVariation in signVariations) {
                val signedVariant = (unsignedVariant.zip(signVariation, Int::times).toTypedArray().map { it.toByte() }.toTypedArray())
                if (abs(signedVariant.sumOf{ it.toInt() }) < 10) {
                    signedVariants.add(quadrupleOf(signedVariant))
                }
            }
        }

        println(now())
        var result = 0L
        for (signedVariant in signedVariants) {
            var tmpResult = 0L
            for (number in input) {
                tmpResult += getFirstOccurence(signedVariant, number)
            }
            result = max(result, tmpResult)
        }
        println(now())
        return result
    }

    fun getFirstOccurence(sequenceOfPriceChanges: Quadruple<Byte, Byte, Byte, Byte>, number: Int): Byte {
        val changes = Array (2001) {Pair((number % 10).toByte(), 0.toByte())}
        var prev = number
        for (i in 1..<2001) {
            val next = getNextSecret(prev)
            changes[i] = Pair((next % 10).toByte(), ((next % 10.toByte()) - changes[i-1].first).toByte())
            prev = next
        }
        for (i in 0..<1998) {
            if (changes[i].second == sequenceOfPriceChanges.first &&
                changes[i + 1].second == sequenceOfPriceChanges.second &&
                changes[i + 2].second == sequenceOfPriceChanges.third &&
                changes[i + 3].second == sequenceOfPriceChanges.fourth) {
                return changes[i + 3].first
            }
        }
        return 0
    }

    fun<S> quadrupleOf(array: Array<S>): Quadruple<S, S, S, S> {
        return Quadruple(array[0], array[1], array[2], array[3])
    }

    data class Quadruple<T, U, V, W>(val first: T, val second: U, val third: V, val fourth: W)
}
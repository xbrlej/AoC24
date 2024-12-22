package utils

import kotlin.math.pow

object CombinatoricsUtils {
    fun getXNaryVariations(radix: Int, size: Int): List<String> {
        val maxNumber = radix.toDouble().pow(size).toInt() - 1
        return IntRange(0, maxNumber).map { it.toUInt().toString(radix).padStart(size, '0') }
    }

    fun <T, U> cartesianProduct(c1: Collection<T>, c2: Collection<U>): List<Pair<T, U>> {
        return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
    }

    fun<T> getCombinations(array: Array<T>, combSize: Int): MutableList<List<T>> {
        val empty: T = null as T
        val currentComb = mutableListOf<T>()
        for (i in IntRange(0, combSize-1)) {
            currentComb.add(empty)
        }
        var result = mutableListOf<List<T>>()
        combinationRecursiveUtil(array, array.size, combSize, currentComb, 0, 0, result)
        return result
    }

    /**
     * Creates combination of k (combSize) elements from array of (length) n, carries the result by reference in combData
     * The combination in the process of creation is stored in currentComb
     */
    private fun<T> combinationRecursiveUtil(input: Array<T>, length: Int, combSize: Int, currentComb: MutableList<T>, combIndex: Int, arrIndex: Int, combData: MutableList<List<T>>) {
        if (combIndex == combSize) {
            combData.add(ArrayList(currentComb))
            return
        }

        if (arrIndex >= length) {
            return
        }

        currentComb[combIndex] = (input[arrIndex]);
        combinationRecursiveUtil(input, length, combSize, currentComb, combIndex+1,  arrIndex+1, combData);

        combinationRecursiveUtil(input, length, combSize, currentComb, combIndex,  arrIndex+1, combData);
    }
}
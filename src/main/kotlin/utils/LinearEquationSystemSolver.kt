package utils

import equations.Rational

object LinearEquationSystemSolver {
    // TODO EXTRACT COEFFICIENTS OUTSIDE THE MATRIX DURING ELIMINATION, currently GCDs for bigger system are outside the range of Int, could be more efficient (also ordering)
    // TODO detect linear combinations and MxN matrices
    // Yes i did implement rational numbers for this
    fun solveXEquationsOfXVariablesGaussianElimination(
        matrixOfEquations: Array<Array<Long>>,
        results: Array<Long>
    ): Array<Rational> {
        require(matrixOfEquations.all { matrixOfEquations[0].size == it.size }) { "Matrix rows are not the same size." }
        require(matrixOfEquations[0].size == matrixOfEquations.size) { "Matrix should be a square" }
        var coefficients = mutableListOf<Rational>()
        val rationalsEquations =
            matrixOfEquations.map { row -> row.map { Rational.of(it) }.toTypedArray() }.toTypedArray()
        val rationalsResults = results.map { Rational.of(it) }.toMutableList()

        // Elimination to triangular matrix
        for (i in 0..<matrixOfEquations.size - 1) {
            for (j in i + 1..<matrixOfEquations.size) {
                val (newRow, newResult) = columnElimination(
                    rationalsEquations[i],
                    rationalsResults[i],
                    rationalsEquations[j],
                    rationalsResults[j],
                    i
                )
                rationalsEquations[j] = newRow
                rationalsResults[j] = newResult
            }
        }

        // Elimination to diagonal matrix
        for (i in 1..<matrixOfEquations.size) {
            for (j in 0..<i) {
                val (newRow, newResult) = columnElimination(
                    rationalsEquations[i],
                    rationalsResults[i],
                    rationalsEquations[j],
                    rationalsResults[j],
                    i
                )
                rationalsEquations[j] = newRow
                rationalsResults[j] = newResult
            }
        }

        for (i in matrixOfEquations.indices) {
            coefficients.add(rationalsResults[i] / rationalsEquations[i][i])
        }

        return coefficients.toTypedArray()
    }

    private fun columnElimination(
        row1: Array<Rational>,
        row1Result: Rational,
        row2: Array<Rational>,
        row2Result: Rational,
        columnIdx: Int
    ): Pair<Array<Rational>, Rational> {
        val rate = row1[columnIdx] / row2[columnIdx]
        if (row2[columnIdx] == Rational.ZERO) {
            return Pair(row2, row2Result)
        }
        val newRow2 = row2.copyOf()
        row2.indices.forEach { newRow2[it] -= (row1[it] / rate) }
        return Pair(newRow2, row2Result - row1Result / rate)
    }
}


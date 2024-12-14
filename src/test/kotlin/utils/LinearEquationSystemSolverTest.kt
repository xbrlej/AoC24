package utils

import equations.Rational
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LinearEquationSystemSolverTest {

    @Test
    fun solveTwoEquationsOfTwoVariables() {
        val equationsLeftSide = arrayOf(
            arrayOf(1L, 2L),
            arrayOf(1L, 1L)
        )
        val equationsRightSide = arrayOf(3L, 2L)

        val result = LinearEquationSystemSolver.solveXEquationsOfXVariablesGaussianElimination(
            equationsLeftSide,
            equationsRightSide
        )
        assertEquals(Rational(1, 1), result[0])
        assertEquals(Rational(1, 1), result[0])
    }

    @Test
    fun solveSixEquationsOfSixVariables() {
        var equationsLeftSide = arrayOf(
            arrayOf(1, 3, 7, 0, 2, 4),
            arrayOf(-5, 3, 18, 9, 11, 2),
            arrayOf(4, 1, 1, 1, 1, 1),
            arrayOf(8, 9, -1, -2, -3, -4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(6, 5, 4, 3, 2, 1)
        ).map { it.map { it.toLong() }.toTypedArray() }.toTypedArray()

        // variable values are 1, 2, 3, 4, 5, 6
        var equationsRightSide = arrayOf(62, 158, 24, -24, 91, 56).map { it.toLong() }.toTypedArray()

        var result = LinearEquationSystemSolver.solveXEquationsOfXVariablesGaussianElimination(
            equationsLeftSide,
            equationsRightSide
        )
        assertEquals(Rational(1, 1), result[0])
        assertEquals(Rational(2, 1), result[1])
        assertEquals(Rational(3, 1), result[2])
        assertEquals(Rational(4, 1), result[3])
        assertEquals(Rational(5, 1), result[4])
        assertEquals(Rational(6, 1), result[5])
    }
}
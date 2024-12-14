package equations

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RationalTest {
    companion object {
        val RATIONAL1 = Rational(5, 1)
        val RATIONAL2 = Rational(2, 3)
        val RATIONAL3_NEG = Rational(-5, 2)
        val RATIONAL4_DENOM_NEG = Rational(5, -2)
        val RATIONAL5_DOUBLE_NEG = Rational(-7, -3)
        val RATIONAL_INF = Rational.POSITIVE_INFINITY
        val RATIONAL_ZERO = Rational.ZERO
    }

    @Test
    fun normalizeOnConstruction() {
        val tested = Rational(2, 4)
        assertEquals(1, tested.numerator)
        assertEquals(2, tested.denominator)
    }

    @Test
    fun getSign() {
        assertEquals(-1, RATIONAL3_NEG.sign)
        assertEquals(1, RATIONAL5_DOUBLE_NEG.sign)
        assertEquals(RATIONAL3_NEG, RATIONAL4_DENOM_NEG)
    }

    @Test
    fun isWhole() {
        assertTrue(RATIONAL1.isWhole)
        assertFalse(RATIONAL2.isWhole)
        assertFalse(RATIONAL3_NEG.isWhole)
    }

    @Test
    fun toDouble() {
        assertEquals(5.0, RATIONAL1.toDouble())
        assertEquals(2.333333333333, RATIONAL5_DOUBLE_NEG.toDouble(), 0.000000000001)
        assertEquals(-2.5, RATIONAL4_DENOM_NEG.toDouble())
    }

    @Test
    fun toInt() {
        assertEquals(5, RATIONAL1.toInt())
        assertEquals(2, RATIONAL5_DOUBLE_NEG.toInt())
    }

    @Test
    fun compareTo() {
        assertTrue(RATIONAL1 > RATIONAL2, "5/1 should be bigger than 2/3")
        assertTrue(RATIONAL2 > RATIONAL3_NEG, "2/3 should be bigger than -5/2")
        assertTrue(RATIONAL3_NEG >= RATIONAL4_DENOM_NEG && RATIONAL4_DENOM_NEG >= RATIONAL3_NEG, "-5/2 should equals 5/-2")
        assertTrue(RATIONAL5_DOUBLE_NEG > RATIONAL_ZERO)
    }

    @Test
    fun plusMinus() {
        assertEquals(RATIONAL_INF, RATIONAL_INF + RATIONAL4_DENOM_NEG)
        assertEquals(Rational.ZERO, -Rational.ZERO)
        assertEquals(Rational.NaN, Rational.POSITIVE_INFINITY - Rational.POSITIVE_INFINITY)
        assertEquals(Rational.POSITIVE_INFINITY, -Rational.NEGATIVE_INFINITY)
        assertEquals(Rational.NEGATIVE_INFINITY, -Rational.POSITIVE_INFINITY)
        assertEquals(Rational.POSITIVE_INFINITY, Rational.POSITIVE_INFINITY + Rational.POSITIVE_INFINITY)
        assertEquals(Rational.POSITIVE_INFINITY, Rational.POSITIVE_INFINITY - Rational.NEGATIVE_INFINITY)
        assertEquals(Rational.NEGATIVE_INFINITY, Rational.NEGATIVE_INFINITY + Rational.NEGATIVE_INFINITY)
    }

    @Test
    fun times() {
        assertEquals(Rational(25, 4), RATIONAL3_NEG * RATIONAL4_DENOM_NEG, "${RATIONAL3_NEG} times ${RATIONAL4_DENOM_NEG} should equal to 25/4")
        assertEquals(Rational(-5, 3), RATIONAL2 * RATIONAL3_NEG, "${RATIONAL2} times ${RATIONAL3_NEG} should equal to -5/3")
        assertEquals(RATIONAL_ZERO, RATIONAL1 * RATIONAL_ZERO)
        assertEquals(RATIONAL_INF, RATIONAL1 * RATIONAL_INF)
    }

    @Test
    fun div() {
        assertEquals(RATIONAL_INF, RATIONAL1 / RATIONAL_ZERO)
        assertEquals(RATIONAL_ZERO, RATIONAL1 / RATIONAL_INF)
    }

    @Test
    fun invert() {
        assertEquals(Rational(1, 5), RATIONAL1.invert())
        assertEquals(Rational(-2, 5), RATIONAL3_NEG.invert())
    }
}
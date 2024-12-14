package equations

import kotlin.math.abs
import kotlin.math.sign

class Rational : Number, Comparable<Rational> {
    val numerator: Long
    val denominator: Long
    val sign: Int
    val isWhole: Boolean

    constructor(numerator: Long, denominator: Long) : super() {
        // Normalize
        this.sign = numerator.sign * denominator.sign
        val absNum = abs(numerator).toULong()
        val absDenom = abs(denominator).toULong()
        if (denominator != 0L) {
            val divisor = gcd(absNum, absDenom)
            this.numerator = absNum.toLong() / divisor.toLong()
            this.denominator = absDenom.toLong() / divisor.toLong()
            isWhole = this.denominator == 1L
        } else {
            this.numerator = 1
            this.denominator = 0
            isWhole = false
        }
    }

    companion object {
        val MIN_VALUE: Rational = Rational(Long.MIN_VALUE, 1)
        val MAX_VALUE: Rational = Rational(Long.MAX_VALUE, 1)
        val POSITIVE_INFINITY: Rational = Rational(1, 0)
        val NEGATIVE_INFINITY: Rational = Rational(-1, 0)
        val NaN: Rational = Rational(0, 0)
        val ZERO: Rational = Rational(0, 1)
        fun of(num: Number) = Rational(num.toLong(), 1)
    }

    override fun toByte(): Byte {
        return toLong().toInt().toByte()
    }

    override fun toDouble(): Double {
        return numerator.toDouble() / denominator.toDouble() * sign
    }

    override fun toFloat(): Float {
        return toDouble().toFloat()
    }

    override fun toLong(): Long {
        if (isWhole) {
            return numerator * sign
        }
        return toDouble().toLong()
    }

    override fun toInt(): Int {
        return toLong().toInt()
    }

    override fun toShort(): Short {
        return toLong().toShort()
    }

    override fun compareTo(other: Rational): Int {
        var diff = this - other
        if (diff.numerator == 0L) {
            return 0
        }
        return diff.sign
    }

    operator fun unaryMinus(): Rational {
        return Rational(-sign * numerator, denominator)
    }

    operator fun plus(other: Rational): Rational {
        val thisUDenom = abs(denominator).toULong()
        val otherUDenom = abs(other.denominator).toULong()
        if (thisUDenom == 0UL && thisUDenom == otherUDenom && sign != other.sign) {
            return NaN
        } else if (thisUDenom == 0UL) {
            return POSITIVE_INFINITY * sign
        } else if (otherUDenom == 0UL) {
            return POSITIVE_INFINITY * other.sign
        }
        val commonDenominator = lcm(otherUDenom, thisUDenom)
        val thisCommonNumerator = numerator * (commonDenominator / thisUDenom).toLong() * sign
        val otherCommonNumerator = other.numerator * (commonDenominator / otherUDenom).toLong() * other.sign
        return Rational(thisCommonNumerator + otherCommonNumerator, commonDenominator.toLong())
    }

    operator fun minus(other: Rational): Rational {
        return this + (-other)
    }

    operator fun times(other: Rational): Rational {
        return Rational(numerator * other.numerator * sign * other.sign, denominator * other.denominator)
    }

    operator fun times(otherNumber: Number): Rational {
        return Rational(numerator * otherNumber.toLong() * sign, this.denominator)
    }



    operator fun div(other: Rational): Rational {
        return this * other.invert()
    }


    operator fun div(otherNumber: Number): Rational {
        return Rational(this.numerator * sign, this.denominator * otherNumber.toLong())
    }

    fun invert(): Rational {
        return Rational(sign * denominator, numerator)
    }

    // Greatest commmon divisor
    private fun gcd(a: ULong, b: ULong): ULong {
        if (b > a) {
            return gcd(b, a)
        }
        if (b == 0UL) {
            return a
        } else {
            return gcd(b, a % b)
        }
    }

    // Least common multiple
    private fun lcm(a: ULong, b: ULong): ULong {
        return (a * b) / gcd(a, b)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false

        if (numerator != other.numerator) return false
        if (denominator != other.denominator) return false
        if (sign != other.sign) return false

        return true
    }

    override fun hashCode(): Int {
        var result = numerator
        result = 31 * result + denominator
        result = 31 * result + sign
        return result.toInt()
    }

    override fun toString(): String {
        return "Rational(sign=$sign, denominator=$denominator, numerator=$numerator)"
    }
}
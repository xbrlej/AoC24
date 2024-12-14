package solutions.year24

import utils.LinearEquationSystemSolver

class Solver13(val machines: List<Array<Int>>) {
    fun solveFirstPart(): Long {
        return machines.sumOf { optimizeMachineUsingMath(it.map { it.toLong() }.toTypedArray()) }
    }

    fun solveSecondPart(): Long {
        val copy = machines.map { it.map{it.toLong()}.toTypedArray()}
        for (longs in copy) {
            longs[4] += 10000000000000L
            longs[5] += 10000000000000L
        }
        return copy.sumOf { optimizeMachineUsingMath(it) }
        // old solution, still working
        return copy.sumOf {optimizeMachine(it, 1000000L, 100000000L)}
    }

    // Unused brute forcing, the method using gauss elimination is the proper way
    fun optimizeMachine(machine: Array<Long>, multiplier: Long, multiplyRange: Long): Long {
        val aX = machine[0]
        val aY = machine[1]
        val bX = machine[2]
        val bY = machine[3]
        val pX = machine[4]
        val pY = machine[5]

        val pRate = pY.toDouble() / pX.toDouble()
        val bRate = bY.toDouble() / bX.toDouble()
        val aRate = aY.toDouble() / aX.toDouble()

        // optimization - you can never reach the prize if your vectors both have a smaller or both have a bigger slope than your prize vector
        if ((aRate < pRate && bRate < pRate) || (aRate > pRate && bRate > pRate)) {
            return 0L
        }

        if (bRate == pRate) {
            return (pX / bX).toLong()
        }

        if (aRate == pRate) {
            return ((pX / bX) * 3).toLong()
        }

        val multipliedAX = aX * multiplier
        val multipliedAY = aY * multiplier
        val multipliedBX = bX * multiplier
        val multipliedBY = bY * multiplier

        var aPresses = 0L
        var bPresses = 1L
        var resX = bX
        var resY = bY

        while (resX < pX && resY < pY) {
            val resRate = resY.toDouble() / resX.toDouble()

            val diffX = pX - resX
            val diffY = pY - resY
            val outOfRangeMultiplication = multiplier != 1L && diffX > multiplyRange && diffY > multiplyRange

            if (resRate > pRate) {
                if (aRate < pRate) {
                    if (outOfRangeMultiplication) {
                        resX += multipliedAX
                        resY += multipliedAY
                        aPresses += multiplier
                    } else {
                        resX += aX
                        resY += aY
                        aPresses += 1
                    }
                } else {
                    if (outOfRangeMultiplication) {
                        resX += multipliedBX
                        resY += multipliedBY
                        bPresses += multiplier
                    } else {
                        resX += bX
                        resY += bY
                        bPresses += 1
                    }
                }
            } else {
                if (aRate > pRate) {
                    if (outOfRangeMultiplication) {
                        resX += multipliedAX
                        resY += multipliedAY
                        aPresses += multiplier
                    } else {
                        resX += aX
                        resY += aY
                        aPresses += 1
                    }
                } else {
                    if (outOfRangeMultiplication) {
                        resX += multipliedBX
                        resY += multipliedBY
                        bPresses += multiplier
                    } else {
                        resX += bX
                        resY += bY
                        bPresses += 1
                    }
                }
            }
        }
        if (resX == pX && resY == pY) {
            return (bPresses + aPresses*3)
        } else {
            return 0L
        }
    }

    fun optimizeMachineUsingMath(machine: Array<Long>): Long {
        val aX = machine[0]
        val aY = machine[1]
        val bX = machine[2]
        val bY = machine[3]
        val pX = machine[4]
        val pY = machine[5]

        // Had a solution for 2 equations of 2 variables, then spent a few hours on generalized solution with gauss elimination
        val equationLeftSides = arrayOf(
            arrayOf(aX, bX),
            arrayOf(aY, bY)
        )
        val equationRightSides = arrayOf(pX, pY)
        val coefs = LinearEquationSystemSolver.solveXEquationsOfXVariablesGaussianElimination(equationLeftSides, equationRightSides)
        if (coefs.all { it.isWhole }) {
            return coefs[0].toLong() * 3 + coefs[1].toLong()
        } else {
            return 0L
        }
    }
}
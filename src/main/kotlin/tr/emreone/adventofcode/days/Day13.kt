package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day13 : Day(
    13,
    2024,
    "Claw Contraption",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val machines = inputAsGroups.map {
            val buttonA = "Button A: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                .find(it[0])
                ?.destructured
                ?.let { (x, y) -> x.toInt() to y.toInt() }
                ?: throw Exception("Button A not found")

            val buttonB = "Button B: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                .find(it[1])
                ?.destructured
                ?.let { (x, y) -> x.toInt() to y.toInt() }
                ?: throw Exception("Button B not found")

            val prize = "Prize: X=(?<X>\\d+), Y=(?<Y>\\d+)".toRegex()
                .find(it[2])
                ?.destructured
                ?.let { (x, y) -> x.toInt() to y.toInt() }
                ?: throw Exception("Prize not found")

            Triple(buttonA, buttonB, prize)
        }

        return machines.sumOf { (aButton, bButton, prize) ->
            solveClawMachine(aButton, bButton, prize) ?: 0
        }
    }

    override fun part2(): Long {
        val offset = 10_000_000_000_000L

        val machines = inputAsGroups.map {
            val buttonA = "Button A: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                .find(it[0])
                ?.destructured
                ?.let { (x, y) -> x.toLong() to y.toLong() }
                ?: throw Exception("Button A not found")

            val buttonB = "Button B: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                .find(it[1])
                ?.destructured
                ?.let { (x, y) -> x.toLong() to y.toLong() }
                ?: throw Exception("Button B not found")

            val prize = "Prize: X=(?<X>\\d+), Y=(?<Y>\\d+)".toRegex()
                .find(it[2])
                ?.destructured
                ?.let { (x, y) -> x.toLong() + offset to y.toLong() + offset }
                ?: throw Exception("Prize not found")

            Triple(buttonA, buttonB, prize)
        }

        return machines.sumOf { (aButton, bButton, prize) ->
            solveDiophantine(aButton, bButton, prize) ?: 0L
        }
    }

    private fun solveClawMachine(a: Pair<Int, Int>, b: Pair<Int, Int>, p: Pair<Int, Int>): Int? {
        val (ax, ay) = a
        val (bx, by) = b
        val (px, py) = p

        for (i in 0..100) {
            for (j in 0..100) {
                val x = i * ax + j * bx
                val y = i * ay + j * by
                if (x == px && y == py) {
                    return i * 3 + j * 1
                }
            }
        }
        return null
    }

    private fun extendedGcd(a: Long, b: Long): Triple<Long, Long, Long> {
        if (b == 0L) return Triple(a, 1, 0)
        val (g, x1, y1) = extendedGcd(b, a % b)
        return Triple(g, y1, x1 - (a / b) * y1)
    }

    private fun solveDiophantine(a: Pair<Long, Long>, b: Pair<Long, Long>, p: Pair<Long, Long>): Long? {
        val (ax, ay) = a
        val (bx, by) = b
        val (px, py) = p

        // Solve ax * i + bx * j = px and ay * i + by * j = py
        val (gcdX, x1, y1) = extendedGcd(ax, bx)
        val (gcdY, x2, y2) = extendedGcd(ay, by)

        // Check if solutions exist
        if (px % gcdX != 0L || py % gcdY != 0L) return null
        if (gcdX != gcdY) return null // Both equations must have the same gcd

        // Scale solutions
        val scaleX = px / gcdX
        val scaleY = py / gcdY
        val aX = x1 * scaleX
        val bX = y1 * scaleX
        val aY = x2 * scaleY
        val bY = y2 * scaleY

        // Verify solutions are valid
        val aa = if (aX >= 0) aX else bY
        val bb = if (bX >= 0) bX else aY
        return if (aa >= 0 && bb >= 0)
            aa * 3 + bb * 1
        else
            null
    }
}

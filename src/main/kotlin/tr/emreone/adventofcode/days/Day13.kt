package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.gcd
import kotlin.math.abs

class Day13 : Day(
    13,
    2024,
    "Claw Contraption",
    session = Resources.resourceAsString("session.cookie")
) {

    class ClawMachine(
        private val a: Pair<Long, Long>,
        private val b: Pair<Long, Long>,
        private val p: Pair<Long, Long>
    ) {
        companion object {
            fun parse(input: List<String>, offset: Long = 0): ClawMachine {
                assert(input.size == 3) {
                    "Invalid input size"
                }

                val buttonA = "Button A: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                    .find(input[0])
                    ?.destructured
                    ?.let { (x, y) -> x.toLong() to y.toLong() }
                    ?: throw Exception("Button A not found")

                val buttonB = "Button B: X\\+(?<X>\\d+), Y\\+(?<Y>\\d+)".toRegex()
                    .find(input[1])
                    ?.destructured
                    ?.let { (x, y) -> x.toLong() to y.toLong() }
                    ?: throw Exception("Button B not found")

                val prize = "Prize: X=(?<X>\\d+), Y=(?<Y>\\d+)".toRegex()
                    .find(input[2])
                    ?.destructured
                    ?.let { (x, y) -> x.toLong() + offset to y.toLong() + offset }
                    ?: throw Exception("Prize not found")

                return ClawMachine(buttonA, buttonB, prize)
            }
        }

        fun calculateMinTokens(): Long? {
            val (ax, ay) = this.a
            val (bx, by) = this.b
            val (px, py) = this.p

            for (aCount in 0..100L) {
                for (bCount in 0..100L) {
                    val x = aCount * ax + bCount * bx
                    val y = aCount * ay + bCount * by
                    if (x == px && y == py) {
                        return aCount * 3 + bCount
                    }
                }
            }
            return null
        }

        fun calculateMinTokensMathematically(): Long?{
            val (ax, ay) = this.a
            val (bx, by) = this.b
            val (px, py) = this.p

            assert(ax * by - ay * bx != 0L) {
                "Unsolvable equation"
            }

            val ca = (1.0 * px * by - py * bx) / (ax * by - ay * bx)
            val cb = (1.0 * px - ax * ca) / bx

            // check if the result is integer
            if (ca.toLong().toDouble() == ca && cb.toLong().toDouble() == cb) {
                return (ca * 3 + cb).toLong()
            }

            return null
        }
    }

    override fun part1(): Long {
        val machines = inputAsGroups.map {
            ClawMachine.parse(it)
        }

        return machines.sumOf { m ->
            m.calculateMinTokens() ?: 0
        }
    }

    override fun part2(): Long {
        val offset = 10_000_000_000_000L

        val machines = inputAsGroups.map {
            ClawMachine.parse(it, offset)
        }

        return machines.sumOf { m ->
            m.calculateMinTokensMathematically() ?: 0L
        }
    }
}

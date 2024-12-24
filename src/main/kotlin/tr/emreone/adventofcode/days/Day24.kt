package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log

class Day24 : Day(
    24,
    2024,
    "Crossed Wires",
    session = Resources.resourceAsString("session.cookie")
) {
    private val operators = mapOf(
        "AND" to { x: Int, y: Int -> x and y },
        "OR" to { x: Int, y: Int -> x or y },
        "XOR" to { x: Int, y: Int -> x xor y }
    )

    private val formulas = inputAsGroups[1].associate { line ->
        val (input1, operation, input2: String, output) = line.replace(" -> ", " ").split(" ")
        output to Triple(operation, input1, input2)
    }

    private fun prettyPrint(wire: String, depth: Int = 0): String {
        if ("([xy])\\d+".toRegex().matches(wire)) {
            return "  ".repeat(depth) + wire
        }
        val (op, x, y) = formulas[wire]!!
        return buildString {
            append("  ".repeat(depth) + "$op ($wire)")
            append("\n")
            append(prettyPrint(x, depth + 1))
            append("\n")
            append(prettyPrint(y, depth + 1))
        }
    }

    private fun verifyIntermediateXor(wire: String, num: Int): Boolean {
        log { "Verifying intermediate XOR $wire, $num" }
        val (op, x, y) = formulas[wire]!!
        if (op != "XOR") return false
        val xName = "x" + num.toString().padStart(2, '0')
        val yName = "y" + num.toString().padStart(2, '0')

        return listOf(x, y).sorted() == listOf(xName, yName)
    }

    private fun verifyCarryBit(wire: String, num: Int): Boolean {
        log { "Verifying carry bit $wire, $num" }
        val (op, x, y) = formulas[wire]!!

        if (num == 1) {
            if (op != "AND") return false
            return listOf(x, y).sorted() == listOf("x00", "y00")
        }
        if (op != "OR") return false
        return verifyDirectCarry(x, num - 1) && verifyRecarry(y, num - 1)
                || verifyDirectCarry(y, num - 1) && verifyRecarry(x, num - 1)
    }

    private fun verifyDirectCarry(wire: String, num: Int): Boolean {
        log { "Verifying direct carry $wire, $num" }
        val (op, x, y) = formulas[wire]!!

        if (op != "AND") return false

        val xName = "x" + num.toString().padStart(2, '0')
        val yName = "y" + num.toString().padStart(2, '0')
        return listOf(x, y).sorted() == listOf(xName, yName)
    }

    private fun verifyRecarry(wire: String, num: Int): Boolean {
        log { "Verifying recarry $wire, $num" }
        val (op, x, y) = formulas[wire]!!

        if (op != "AND") return false

        return verifyIntermediateXor(x, num) && verifyCarryBit(y, num)
                || verifyIntermediateXor(y, num) && verifyCarryBit(x, num)
    }

    fun verifyZ(wire: String, num: Int): Boolean {
        log { "Verifying $wire, $num" }

        val (op, x, y) = formulas[wire]!!
        if (op != "XOR") return false

        if (num == 0) {
            return listOf(x, y).sorted() == listOf("x00", "y00")
        }

        return verifyIntermediateXor(x, num) && verifyCarryBit(y, num)
                || verifyIntermediateXor(y, num) && verifyCarryBit(x, num)
    }

    override fun part1(): Long {
        val knownWires = inputAsGroups[0].associate { line ->
            val (wire, value) = line.split(": ")
            wire.trim() to value.trim().toInt()
        }.toMutableMap()

        fun calc(wire: String): Int {
            if (wire in knownWires) return knownWires[wire]!!

            val (op, x, y) = formulas[wire]!!

            knownWires[wire] = operators[op]!!(calc(x), calc(y))
            return knownWires[wire]!!
        }

        // find all Zxx wires
        val zWires = mutableListOf<Int>()
        var i = 0
        while (true) {
            val wire = buildString {
                append("z")
                append(i.toString().padStart(2, '0'))
            }
            println("Calculating $wire")
            if (wire !in formulas) break
            zWires.add(calc(wire))
            i++
        }

        val binaryOutput = zWires.reversed().joinToString("")
        val decimalOutput = binaryOutput.toLong(2)

        log {
            "Binary output: $binaryOutput \n" +
            "Decimal output: $decimalOutput"
        }

        return decimalOutput
    }

    override fun part2(): Long {
        var i = 0
        while (true) {
            val name = buildString {
                append("z")
                append(i.toString().padStart(2, '0'))
            }
            if (!verifyZ(name, i)) {
                log { "Found the first invalid wire: $name" }
                break
            }
            i++
        }

        return 0
    }

}

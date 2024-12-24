package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day24 : Day(
    24,
    2024,
    "Crossed Wires",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        val wires = mutableMapOf<String, Boolean>()
        val gates = mutableListOf<List<String>>()

        val initialValues = inputAsGroups[0]
        val gateConnections = inputAsGroups[1]

        initialValues.forEach { line ->
            val (wire, value) = line.split(": ")
            wires[wire.trim()] = value.trim() == "1"
        }

        gateConnections.forEach { line ->
            val (input1, operation, input2: String, _: String, output) = line.split(" ")
            gates.add(listOf(input1, input2, operation, output))
        }

        // Simulate the gates
        fun evaluateGate(gate: List<String>): Boolean? {
            val (input1, input2, operation, output) = gate
            val value1 = wires[input1]
            val value2 = wires[input2]
            if (value1 != null && value2 != null) {
                return when (operation) {
                    "AND" -> value1 && value2
                    "OR" -> value1 || value2
                    "XOR" -> value1 xor value2
                    else -> throw IllegalArgumentException("Invalid gate operation: $operation")
                }
            }
            return null
        }

        var outputCalculated = false
        while (!outputCalculated) {
            outputCalculated = true
            for (gate in gates) {
                val outputValue = evaluateGate(gate)
                if (outputValue != null && !wires.containsKey(gate[3])) {
                    wires[gate[3]] = outputValue
                    outputCalculated = false
                }
            }
        }

        // Calculate the decimal output
        val zWires = wires.filterKeys { it.startsWith("z") }.toSortedMap()
        val binaryOutput = zWires.values.joinToString("") { if (it) "1" else "0" }
        val decimalOutput = binaryOutput.toLong(2)

        println("Wires: $wires")
        println("Binary output: $binaryOutput")
        println("Decimal output: $decimalOutput")

        return decimalOutput
    }

    override fun part2(): Long {
        return 0
    }

}

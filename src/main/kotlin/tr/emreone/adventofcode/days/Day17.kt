package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log

class Day17 : Day(
    17,
    2024,
    "Chronospatial Computer",
    session = Resources.resourceAsString("session.cookie")
) {
    data class ThreeBitComputer(
        var registerA: Long,
        private var registerB: Long,
        private var registerC: Long,
        val program: List<Int>
    ) {
        private var instructionPointer = 0
        val output = mutableListOf<Long>()

        private fun getComboOperand(operand: Int): Long {
            return when (operand) {
                in 0..3 -> operand.toLong()
                4 -> registerA
                5 -> registerB
                6 -> registerC
                else -> throw IllegalArgumentException("Invalid combo operand: $operand")
            }
        }

        fun run() {
            this.output.clear()

            while (instructionPointer < program.size) {
                val opcode = program[instructionPointer]
                val operand = program[instructionPointer + 1]

                when (opcode) {
                    0 -> { // adv
                        registerA = registerA shr getComboOperand(operand).toInt()
                    }

                    1 -> { // bxl
                        registerB = registerB xor operand.toLong()
                    }

                    2 -> { // bst
                        registerB = getComboOperand(operand) % 8
                    }

                    3 -> { // jnz
                        if (registerA != 0L) {
                            instructionPointer = operand
                            continue
                        }
                    }

                    4 -> { // bxc
                        registerB = registerB xor registerC
                    }

                    5 -> { // out
                        output.add((getComboOperand(operand) % 8))
                    }

                    6 -> { // bdv
                        registerB = registerA shr getComboOperand(operand).toInt()
                    }

                    7 -> { // cdv
                        registerC = registerA shr getComboOperand(operand).toInt()
                    }

                    else -> throw IllegalArgumentException("Unknown opcode: $opcode")
                }

                instructionPointer += 2
            }
        }

        companion object {
            fun parse(input: List<List<String>>): ThreeBitComputer {
                val registerA = input.first()[0].split(":")[1].trim().toLong()
                val registerB = input.first()[1].split(":")[1].trim().toLong()
                val registerC = input.first()[2].split(":")[1].trim().toLong()

                val program = input.last()[0].split(":")[1].trim().split(",").map { it.toInt() }

                return ThreeBitComputer(registerA, registerB, registerC, program)
            }
        }
    }

    override fun part1(): String {
        val computer = ThreeBitComputer.parse(inputAsGroups)

        computer.run()

        return computer.output.joinToString(",")
    }

    override fun part2(): Long {
        val computer = ThreeBitComputer.parse(inputAsGroups)

        assert(computer.program.takeLast(2) == listOf(3, 0)) {
            "Program does not end with JNZ 0"
        }

        return computer.program
            .reversed()
            .map { it.toLong() }
            .fold(listOf(0L)) { candidates, instruction ->
                candidates.flatMap { candidate ->
                    val shifted = candidate shl 3
                    (shifted..shifted + 8).mapNotNull { attempt ->
                        computer.copy().run {
                            registerA = attempt
                            attempt.takeIf {
                                this.run()
                                this.output.first() == instruction
                            }
                        }
                    }
                }
            }.first()
    }
}

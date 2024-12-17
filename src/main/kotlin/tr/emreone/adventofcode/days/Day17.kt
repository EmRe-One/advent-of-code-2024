package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class ThreeBitComputer(
    private var registerA: Int,
    private var registerB: Int,
    private var registerC: Int,
    private val program: List<Int>
) {
    private var instructionPointer = 0
    private val output = mutableListOf<Int>()

    private fun getComboOperand(operand: Int): Int {
        return when (operand) {
            in 0..3 -> operand
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw IllegalArgumentException("Invalid combo operand: $operand")
        }
    }

    fun run(): String {
        while (instructionPointer < program.size) {
            val opcode = program[instructionPointer]
            val operand = program[instructionPointer + 1]

            // Default next instruction pointer
            var nextPointer = instructionPointer + 2

            when (opcode) {
                0 -> { // adv
                    val denominator = 1 shl getComboOperand(operand) // 2^operand
                    registerA /= denominator
                }

                1 -> { // bxl
                    registerB = registerB xor operand
                }

                2 -> { // bst
                    registerB = getComboOperand(operand) % 8
                }

                3 -> { // jnz
                    if (registerA != 0) nextPointer = operand
                }

                4 -> { // bxc
                    registerB = registerB xor registerC
                }

                5 -> { // out
                    output.add(getComboOperand(operand) % 8)
                }

                6 -> { // bdv
                    val denominator = 1 shl getComboOperand(operand) // 2^operand
                    registerB = registerA / denominator
                }

                7 -> { // cdv
                    val denominator = 1 shl getComboOperand(operand) // 2^operand
                    registerC = registerA / denominator
                }

                else -> throw IllegalArgumentException("Unknown opcode: $opcode")
            }

            instructionPointer = nextPointer
        }
        return output.joinToString(",")
    }
}

class Day17 : Day(
    17,
    2024,
    "Chronospatial Computer",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): String {
        val registerA = inputAsList[0].split(":")[1].trim().toInt()
        val registerB = inputAsList[1].split(":")[1].trim().toInt()
        val registerC = inputAsList[2].split(":")[1].trim().toInt()

        val program = inputAsList[4].split(":")[1].trim().split(",").map { it.toInt() }

        val computer = ThreeBitComputer(registerA, registerB, registerC, program)
        return computer.run()
    }

    override fun part2(): Int {
        val program = inputAsList[4].split(":")[1].trim().split(",").map { it.toInt() }

        return findRegisterAForOutput(program)
    }

    private fun findRegisterAForOutput(program: List<Int>): Int {
        val targetOutput = program.joinToString(",")
        var registerA = 1

        while (true) {
            val computer = ThreeBitComputer(registerA, 0, 0, program)
            val output = computer.run()

            if (output == targetOutput) {
                return registerA
            }
            if (registerA % 100_000_000 == 0) println("Checked registerA = $registerA")

            registerA++
        }
    }
}

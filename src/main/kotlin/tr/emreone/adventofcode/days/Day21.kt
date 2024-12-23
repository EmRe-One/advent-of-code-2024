package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.*

class Day21 : Day(
    21,
    2024,
    "Keypad Conundrum",
    session = Resources.resourceAsString("session.cookie")
) {
    data class State(val keypad: List<List<Char?>>, val pos: Pair<Int, Int>)

    private fun findShortestSequence(initialState: State, targetCode: String): String {
        val queue = LinkedList<Pair<State, String>>()
        val visited = mutableSetOf<State>()
        queue.add(initialState to "")

        while (queue.isNotEmpty()) {
            val (state, sequence) = queue.poll()
            if (sequence.endsWith(targetCode)) {
                return sequence.substring(0, sequence.length - targetCode.length)
            }
            if (state in visited) continue
            visited.add(state)

            val (keypad, pos) = state
            val (row, col) = pos
            for ((dr, dc, dir) in listOf(
                Triple(-1, 0, '^'),
                Triple(1, 0, 'v'),
                Triple(0, -1, '<'),
                Triple(0, 1, '>')
            )) {
                val newRow = row + dr
                val newCol = col + dc
                if (newRow in keypad.indices && newCol in keypad[0].indices && keypad[newRow][newCol] != null) {
                    val newPos = newRow to newCol
                    val newSequence = sequence + dir
                    if (keypad[newRow][newCol] != 'A') {
                        queue.add(State(keypad, newPos) to newSequence)
                    } else {
                        val activatedSequence = newSequence + 'A'
                        queue.add(State(keypad, newPos) to activatedSequence)
                    }
                }
            }
        }
        return ""
    }

    override fun part1(): Int {
        val yourKeypad = listOf(
            listOf(null, '^', 'A'),
            listOf('<', 'v', '>'),
        )
        val robotKeypad = yourKeypad
        val numericKeypad = listOf(
            listOf('7', '8', '9'),
            listOf('4', '5', '6'),
            listOf('1', '2', '3'),
            listOf(null, '0', 'A')
        )

        val codes = inputAsList
        var totalComplexity = 0

        for (code in codes) {
            val sequence = findShortestSequence(State(yourKeypad, 2 to 2), code)
            val robotSequence = findShortestSequence(State(robotKeypad, 0 to 2), sequence)
            val secondRobotSequence = findShortestSequence(State(robotKeypad, 0 to 2), robotSequence)
            val finalSequence = findShortestSequence(State(numericKeypad, 3 to 2), secondRobotSequence)
            val complexity = finalSequence.length * code.substring(code.indexOfFirst { it.isDigit() }).toInt()
            totalComplexity += complexity
            println("$code: $finalSequence (Complexity: $complexity)")
        }

        return totalComplexity
    }

    override fun part2(): Long {
        return 0
    }

}

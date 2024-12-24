package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.*

class Day21 : Day(
    21,
    2024,
    "Keypad Conundrum",
    session = Resources.resourceAsString("session.cookie")
) {

    data class KeypadButton(val symbol: Char, val position: Point)

    class Keypad(layout: List<List<Char?>>) {

        private val positions = layout.mapIndexed { y, row ->
            row.mapIndexed { x, symbol ->
                if (symbol != null)
                    KeypadButton(symbol, Point(x, y))
                else
                    null
            }.filterNotNull()
        }.flatten()
        private val sequences: MutableMap<Pair<KeypadButton, KeypadButton>, List<String>> = mutableMapOf()
        private val cache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()

        init {
            for(from in positions) {
                for(to in positions) {
                    if (from == to) {
                        sequences[Pair(from, to)] = listOf("A")
                        continue
                    }

                    val possibilities = mutableListOf<String>()
                    val queue = LinkedList<Pair<Point, String>>()
                    var optimalLength = Int.MAX_VALUE

                    queue.add(Pair(from.position, ""))
                    queue@ while(queue.isNotEmpty())  {
                        val (currentPosition, currentSequence) = queue.poll()

                        for(dir in Direction4.entries) {
                            val nextPos = currentPosition + dir.vector
                            if (nextPos.y !in layout.indices || nextPos.x !in layout[0].indices) {
                                continue
                            }
                            if (layout[nextPos.y][nextPos.x] == null) {
                                continue
                            }

                            if (nextPos == to.position) {
                                if (optimalLength < currentSequence.length + 1) break@queue

                                optimalLength = currentSequence.length + 1
                                possibilities.add(currentSequence + dir.symbol + "A")
                            }
                            else {
                                queue.add(Pair(nextPos, currentSequence + dir.symbol))
                            }
                        }
                    }
                    sequences[Pair(from, to)] = possibilities
                }
            }
        }

        fun getAllOptions(code: String): List<String> {
            val options = "A${code}".windowed(2).map {pair ->
                val firstButton = this.positions.first { it.symbol == pair[0] }
                val secondButton = this.positions.first { it.symbol == pair[1] }
                this.sequences[Pair(firstButton, secondButton)] ?: listOf()
            }

            // return the cartesian product of all options joined to a single string
            return options.fold(listOf("")) { acc, list ->
                acc.flatMap { accOption ->
                    list.map { listOption ->
                        accOption + listOption
                    }
                }
            }
        }

        fun computeLengthAtDepth(code: String, depth: Int = 2): Long {
            // If already calculated, return the value
            if (cache.containsKey(Pair(code, depth))) {
                return cache[Pair(code, depth)]!!
            }

            // If depth is 1, return the sum of the lengths of the sequences
            if (depth == 1) {
                return "A${code}".windowed(2).sumOf { pair ->
                    val keypadButton1 = this.positions.first { it.symbol == pair[0] }
                    val keypadButton2 = this.positions.first { it.symbol == pair[1] }

                    this.sequences[keypadButton1 to keypadButton2]?.first()!!.length.toLong() ?: 0L
                }
            }

            // Otherwise, calculate the sum of the lengths of the sequences
            var length = 0L
            "A${code}".windowed(2).forEach { pair ->
                val keypadButton1 = this.positions.first { it.symbol == pair[0] }
                val keypadButton2 = this.positions.first { it.symbol == pair[1] }

                length += this.sequences[keypadButton1 to keypadButton2]!!.minOf { subseq ->
                    this.computeLengthAtDepth(subseq, depth - 1)
                }
            }

            cache[Pair(code, depth)] = length
            return length
        }
    }

    private val numericKeypad = Keypad(
        listOf(
            listOf('7', '8', '9'),
            listOf('4', '5', '6'),
            listOf('1', '2', '3'),
            listOf(null, '0', 'A')
        )
    )

    private val directionalKeypad = Keypad(
        listOf(
            listOf(null, '^', 'A'),
            listOf('<', 'v', '>')
        )
    )

    private fun calculateComplexity(code: String, commandsLength: Long): Long {
        val numericValue = code.filter { it.isDigit() }.toLongOrNull() ?: 0L
        return commandsLength * numericValue
    }

    override fun part1(): Long {
        return inputAsList.sumOf { code ->
            // first get options of robot 1
            var nextOptions = numericKeypad.getAllOptions(code)

            for (i in 0 until 2) {
                nextOptions = nextOptions.flatMap { directionalKeypad.getAllOptions(it) }
                    .groupBy { it.length }
                    .toSortedMap()
                    .entries
                    .first()
                    .value
            }

            calculateComplexity(code, nextOptions.first().length.toLong())
        }
    }

    override fun part2(): Long {
        return inputAsList.sumOf { code ->
            // first get options of robot 1
            val nextOptions = numericKeypad.getAllOptions(code)

            val length = nextOptions.minOf { option ->
                directionalKeypad.computeLengthAtDepth(option, 25)
            }

            calculateComplexity(code, length)
        }

    }

}

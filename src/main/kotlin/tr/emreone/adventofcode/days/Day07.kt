package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import kotlin.math.pow

class Day07 : Day(
    7,
    2024,
    "Bridge Repair",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        return inputAsList
            .map { line -> line.split(":") }
            .filter { line ->
                val result = line[0].toLong()
                val term = line[1].trim().split("\\s+".toRegex()).map { it.toLong() }
                this.equationIsValid(result, term)
            }
            .sumOf { it[0].toLong() }
    }

    override fun part2(): Long {
        return inputAsList
            .map { line -> line.split(":") }
            .filter { line ->
                val result = line[0].toLong()
                val term = line[1].trim().split("\\s+".toRegex()).map { it.toLong() }
                this.equationIsValid(result, term, true)
            }
            .sumOf { it[0].toLong() }
    }

    private fun generateOperants(n: Int, concatAllowed: Boolean = false): List<List<String>> {
        val operants =  mutableListOf("+", "*")
        if (concatAllowed) {
            operants.add("||")
        }
        return List(operants.size.toDouble().pow(n.toDouble()).toInt()) { index ->
            List(n) { i ->
                operants[(index / operants.size.toDouble().pow((n - i - 1).toDouble()).toInt()) % operants.size]
            }
        }
    }

    private fun evaluate(term: List<Long>, operants: List<String>): Long {
        return term.foldIndexed(0L) { index, acc, i ->
            if (index == 0) {
                return@foldIndexed i
            }

            val op  = operants[index - 1]

            return@foldIndexed when(op) {
                "+" -> acc + i
                "*" -> acc * i
                "||" -> "${acc}${i}".toLong()
                else -> acc
            }

        }
    }

    private fun equationIsValid(result: Long, term: List<Long>, concatAllowed: Boolean = false): Boolean {
        if (term.size == 1) {
            return term[0] == result
        }

        val operants = generateOperants(term.size - 1, concatAllowed)

        return operants.any { op ->
            val evaluate = this.evaluate(term, op)
            evaluate == result
        }
    }

}

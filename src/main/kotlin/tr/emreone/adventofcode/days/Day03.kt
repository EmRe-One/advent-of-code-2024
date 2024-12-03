package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log

class Day03 : Day(
    3,
    2024,
    "Mull It Over",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        return "(mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\))".toRegex()
            .findAll(inputAsString)
            .sumOf {
                val a = it.groups["first"]?.value ?: "0"
                val b = it.groups["second"]?.value ?: "0"
                a.toLong() * b.toLong()
            }
    }

    override fun part2(): Long {
        val matches =  "(do\\(\\))|(don't\\(\\))|(mul\\((?<first>\\d{1,3}),(?<second>\\d{1,3})\\))".toRegex()
            .findAll(inputAsString)
            .toList()

        var enabled = true
        var result = 0L
        matches.forEach {
            if (it.groups[0]?.value == "do()") {
                enabled = true
            }
            else if (it.groups[0]?.value == "don't()") {
                enabled = false
            }
            else if (enabled) {
                val a = it.groups["first"]?.value ?: "0"
                val b = it.groups["second"]?.value ?: "0"
                result += a.toLong() * b.toLong()
            }
        }

        return result
    }

}

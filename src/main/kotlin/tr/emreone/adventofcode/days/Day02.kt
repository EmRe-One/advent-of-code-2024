package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import kotlin.math.abs

class Day02 : Day(
    2,
    2024,
    "Red-Nosed Reports",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        return inputAsList
            .map { reportLine ->
                reportLine.split("\\s+".toRegex()).map { level -> level.toInt() }
            }
            .count { report -> this.reportIsSafe(report) }
    }

    override fun part2(): Int {
        return inputAsList
            .map { reportLine ->
                reportLine.split("\\s+".toRegex()).map { level -> level.toInt() }
            }
            .count { report -> this.reportIsSafeWithSingleTolerance(report) }
    }

    private fun reportIsSafe(list: List<Int>): Boolean {
        val differences = list.windowed(2) { (first, second) -> second - first }
        return differences.all { it in -3..-1 }     // all decrease
                || differences.all { it in 1..3 }   // all increase
    }

    private fun reportIsSafeWithSingleTolerance(list: List<Int>): Boolean {
        val adjustedVersions = list.indices.map { index ->
            val copy = list.toMutableList()
            copy.removeAt(index)
            copy
        }

        return adjustedVersions.any { this.reportIsSafe(it) }
    }

}

package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import kotlin.math.abs

class Day01 : Day(
    1,
    2024,
    "Historian Hysteria",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val (left, right) = inputAsList
            .map { it.split("\\s+".toRegex()) }
            .map { nums -> nums[0].toInt() to nums[1].toInt() }
            .unzip()

        return left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(r - l) }
    }

    override fun part2(): Long {
        val (left, right) = inputAsList
            .map { it.split("\\s+".toRegex()) }
            .map { nums -> nums[0].toInt() to nums[1].toInt() }
            .unzip()

        val rightCounts = right.groupingBy { it }.eachCount()

        return left.sumOf { leftNum -> (rightCounts.getOrElse(leftNum) { 0 }).toLong() * leftNum}
    }

}

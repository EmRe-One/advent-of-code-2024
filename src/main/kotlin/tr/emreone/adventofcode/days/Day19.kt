package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day19 : Day(
    19,
    2024,
    "",
    session = Resources.resourceAsString("session.cookie")
) {
    private val towelPatterns = inputAsGroups[0][0].split(",").map { it.trim() }.toSet()
    private val desiredDesigns = inputAsGroups[1]

    override fun part1(): Int {
        return desiredDesigns.count {
            isDesignPossible(it, towelPatterns, mutableMapOf())
        }
    }

    override fun part2(): Long {
        return desiredDesigns.sumOf {
            countCombinations(it, towelPatterns, mutableMapOf())
        }
    }

    private fun isDesignPossible(design: String, towelPatterns: Set<String>, memo: MutableMap<String, Boolean>): Boolean {
        if (design.isEmpty()) return true

        if (memo.containsKey(design)) return memo[design]!!
        towelPatterns.forEach {  pattern ->
            if (design.startsWith(pattern) && isDesignPossible(design.substring(pattern.length), towelPatterns, memo)) {
                memo[design] = true
                return true
            }
        }

        memo[design] = false
        return false
    }

    private fun countCombinations(design: String, towelPatterns: Set<String>, memo: MutableMap<String, Long>): Long {
        if (design.isEmpty()) return 1 // Base case: one way to make an empty design
        if (memo.containsKey(design)) return memo[design]!! // Check if already calculated

        var combinations = 0L
        for (pattern in towelPatterns) {
            if (design.startsWith(pattern)) {
                combinations += countCombinations(design.substring(pattern.length), towelPatterns, memo)
            }
        }

        memo[design] = combinations // Store the result
        return combinations
    }
}

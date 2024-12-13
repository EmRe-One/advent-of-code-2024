package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day11 : Day(
    11,
    2024,
    "Plutonian Pebbles",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val initialStones = inputAsString.split("\\s+".toRegex()).map { it.toLong() }
        val blinks = 25

        val finalStones = evolveStones(initialStones, blinks)

        return finalStones.size
    }

    override fun part2(): Long {
        val initialStones = inputAsString.split("\\s+".toRegex()).map { it.toLong() }
        val blinks = 75

        val finalStones = evolveStonesEfficiently(initialStones, blinks)
        return finalStones.values.sum()
    }

    private fun evolveStones(stones: List<Long>, blinks: Int): List<Long> {
        var currentStones = stones

        repeat(blinks) {
            val newStones = mutableListOf<Long>()

            for (stone in currentStones) {
                when {
                    stone == 0L -> newStones.add(1L)
                    stone.toString().length % 2 == 0 -> {
                        val mid = stone.toString().length / 2
                        val left = stone.toString().substring(0, mid).toLong()
                        val right = stone.toString().substring(mid).toLong()
                        newStones.add(left)
                        newStones.add(right)
                    }
                    else -> newStones.add(stone * 2024L)
                }
            }

            currentStones = newStones
        }

        return currentStones
    }

    private fun evolveStonesEfficiently(stones: List<Long>, blinks: Int): Map<Long, Long> {
        var currentStones = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        repeat(blinks) {
            val newStones = mutableMapOf<Long, Long>()

            for ((stone, count) in currentStones) {
                when {
                    stone == 0L -> newStones[1L] = newStones.getOrDefault(1L, 0L) + count
                    stone.toString().length % 2 == 0 -> {
                        val mid = stone.toString().length / 2
                        val left = stone.toString().substring(0, mid).toLong()
                        val right = stone.toString().substring(mid).toLong()
                        newStones[left] = newStones.getOrDefault(left, 0L) + count
                        newStones[right] = newStones.getOrDefault(right, 0L) + count
                    }
                    else -> {
                        val newStone = stone * 2024
                        newStones[newStone] = newStones.getOrDefault(newStone, 0L) + count
                    }
                }
            }

            currentStones = newStones
        }

        return currentStones
    }

}

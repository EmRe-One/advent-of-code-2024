package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log

class Day22 : Day(
    22,
    2024,
    "Monkey Market",
    session = Resources.resourceAsString("session.cookie")
) {

    class SecretNumber {
        companion object {
            fun evolveSecretNumber(secret: Long): Long {
                var randomized = secret
                randomized = ((randomized * 64) xor randomized) % 16_777_216
                randomized = ((randomized / 32) xor randomized) % 16_777_216
                randomized = ((randomized * 2048) xor randomized) % 16_777_216

                return randomized
            }
        }
    }

    override fun part1(): Long {
        return inputAsList.sumOf { initial ->
            var num = initial.toLong()
            repeat(2000) {
                num = SecretNumber.evolveSecretNumber(num)
            }
            num.toLong()
        }
    }

    override fun part2(): Long {
        val sequenceToTotal = mutableMapOf<List<Long>, Long>()

        inputAsList.forEach { initial ->
            var num = initial.toLong()
            val buyer = mutableListOf<Long>()
            buyer.add(num % 10)
            repeat(2000) {
                num = SecretNumber.evolveSecretNumber(num)
                buyer.add(num % 10)
            }

            // if the sequence is already seen, skip, because monkey would sell at first opportunity
            val seen = mutableSetOf<List<Long>>()
             buyer.windowed(5).forEach buyerLoop@ { window ->
                val seq = window.windowed(2).map { it[1] - it[0] }
                if (seen.contains(seq)) {
                    return@buyerLoop
                }

                seen.add(seq)
                sequenceToTotal[seq] = sequenceToTotal.getOrDefault(seq, 0L) + window.last()
            }
        }

        val bestSequence = sequenceToTotal.maxBy { it.value }
        log {
            "Best sequence: $bestSequence"
        }
        return bestSequence.value
    }

}

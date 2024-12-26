package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log

class Day25 : Day(
    25,
    2024,
    "",
    session = Resources.resourceAsString("session.cookie")
) {

    class Lock(private val schema: List<String>) {
        val pinHeights: List<Int>

        init {
            this.pinHeights = schema.first().mapIndexed { index, _ -> getPinHeightAt(index) }
        }

        private fun getPinHeightAt(x: Int): Int {
            return this.schema.count { it[x] == '#' } - 1
        }
    }

    class Key(private val schema: List<String>) {
        val pinHeights: List<Int>

        init {
            this.pinHeights = schema.first().mapIndexed { index, _ -> getPinHeightAt(index) }
        }

        private fun getPinHeightAt(x: Int): Int {
            return this.schema.count { it[x] == '#' } - 1
        }
    }

    override fun part1(): Int {
        val locks = mutableListOf<Lock>()
        val keys = mutableListOf<Key>()

        inputAsGroups.forEach {
            if (it.first().first() == '#') {
                locks.add(Lock(it))
            } else {
                keys.add(Key(it))
            }
        }

        return locks.sumOf { lock ->
            keys.count keyCounter@ { key ->
                val match = lock.pinHeights.zip(key.pinHeights).all {
                    it.first + it.second <= 5
                }

                return@keyCounter match
            }
        }
    }

}

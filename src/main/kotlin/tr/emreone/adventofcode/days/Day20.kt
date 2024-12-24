package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Direction8
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.times
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.*
import kotlin.math.abs

class Day20 : Day(
    20,
    2024,
    "Race Condition",
    session = Resources.resourceAsString("session.cookie")
) {
    private val racetrack = Racetrack(inputAsList)

    override fun part1(): Int {
        val limit = if (inputAsList.size < 20) 10 else 100

        var counter = 0
        racetrack.map.forEachIndexed columnLoop@{ y, row ->
            row.forEachIndexed rowLoop@{ x, cell ->
                if (cell == '#') {
                    return@rowLoop
                }
                for (n in racetrack.getHalfNeighbors(x, y)) {
                    if (abs(racetrack.distances[y][x] - racetrack.distances[n.y][n.x]) >= limit + 2) {
                        counter++
                    }
                }
            }
        }

        return counter
    }

    override fun part2(): Int {
        val limit = if (inputAsList.size < 20) 70 else 100

        var counter = 0
        racetrack.map.forEachIndexed columnLoop@{ y, row ->
            row.forEachIndexed rowLoop@{ x, cell ->
                if (cell == '#') {
                    return@rowLoop
                }
                for (radius in 2..20) {
                    for (dx in 0..radius) {
                        val dy = radius - dx
                        val neighbors = setOf(
                            Point(x + dx, y + dy),
                            Point(x - dx, y + dy),
                            Point(x + dx, y - dy),
                            Point(x - dx, y - dy)
                        ).filter {
                            it.x in 0 until racetrack.width
                                    && it.y in 0 until racetrack.height
                                    && racetrack.map[it.y][it.x] != '#'
                        }
                        neighbors.forEach { n ->
                            if (racetrack.distances[y][x] - racetrack.distances[n.y][n.x] >= limit + radius) {
                                counter++
                            }
                        }
                    }
                }
            }
        }

        return counter
    }

    class Racetrack(input: List<String>) {
        val map = input.map { it.toCharArray() }
        val height = map.size
        val width = map[0].size
        private val start = findCell('S')
        private val end = findCell('E')

        val distances = Array(height) { IntArray(width) { -1 } }

        init {
            distances[start.y][start.x] = 0
            val queue: Queue<Point> = LinkedList()
            queue.add(start)

            while (queue.isNotEmpty()) {
                val current = queue.poll()
                for (dir in Direction4.entries) {
                    val next = current + dir.vector
                    if (
                        next.x in 0 until width
                        && next.y in 0 until height
                        && map[next.y][next.x] != '#'
                        && distances[next.y][next.x] == -1
                    ) {
                        distances[next.y][next.x] = distances[current.y][current.x] + 1
                        queue.add(next)
                    }
                }
            }
        }

        private fun findCell(symbol: Char): Point {
            for (y in map.indices) {
                for (x in map[y].indices) {
                    if (map[y][x] == symbol) {
                        return Point(x, y)
                    }
                }
            }
            throw IllegalArgumentException("No point found for symbol $symbol")
        }

        fun getHalfNeighbors(x: Int, y: Int): List<Point> {
            val neighbors = mutableListOf<Point>()

            neighbors.add(Point(x, y) + Direction8.NORTHEAST.vector)
            neighbors.add(Point(x, y) + Direction8.EAST.vector * 2)
            neighbors.add(Point(x, y) + Direction8.SOUTHEAST.vector)
            neighbors.add(Point(x, y) + Direction8.SOUTH.vector * 2)

            return neighbors.filter {
                it.x in 0 until width && it.y in 0 until height && map[it.y][it.x] != '#'
            }
        }

        fun printMap() {
            this.distances.forEach { row ->
                row.forEach { cell ->
                    if (cell == -1) {
                        print("####")
                    } else {
                        print("${cell.toString().padStart(3)} ")
                    }
                }
                println()
            }
        }
    }
}

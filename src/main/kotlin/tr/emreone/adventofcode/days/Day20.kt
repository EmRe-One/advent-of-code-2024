package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point
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

    data class Cheat(val start: Point, val end: Point, val timeSaved: Int)

    class Racetrack(input: List<String>) {
        private val directions = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))

        private val map = input.map { it.toCharArray() }
        private val height = map.size
        private val width = map[0].size
        val start = findStart(input)
        val end = findEnd(input)

        private fun findStart(map: List<String>): Point {
            for (y in map.indices) {
                for (x in map[y].indices) {
                    if (map[y][x] == 'S') {
                        return Point(x, y)
                    }
                }
            }
            throw IllegalArgumentException("No start point found")
        }

        private fun findEnd(map: List<String>): Point {
            for (y in map.indices) {
                for (x in map[y].indices) {
                    if (map[y][x] == 'E') {
                        return Point(x, y)
                    }
                }
            }
            throw IllegalArgumentException("No end point found")
        }

        fun findFastestLegalTime(from: Point, to: Point): Int {
            val queue: Queue<Pair<Point, Int>> = LinkedList()
            val visited = mutableSetOf<Point>()
            queue.add(Pair(from, 0))
            visited.add(from)

            while (queue.isNotEmpty()) {
                val (current, time) = queue.poll()
                if (current == to) {
                    return time
                }

                for (dir in directions) {
                    val next = Point(current.x + dir.x, current.y + dir.y)
                    if (
                        next.x in 0 until width
                        && next.y in 0 until height
                        && map[next.y][next.x] != '#'
                        && next !in visited
                    ) {
                        queue.add(Pair(next, time + 1))
                        visited.add(next)
                    }
                }
            }

            return Int.MAX_VALUE // Should not happen if there's a path
        }

        fun findCheats(fastestLegalTime: Int): List<Cheat> {
            val cheats = mutableListOf<Cheat>()

            for (startX in 0 until width) {
                for (startY in 0 until height) {
                    for (endX in 0 until width) {
                        for (endY in 0 until height) {
                            val start = Point(startX, startY)
                            val end = Point(endX, endY)
                            val cheat = isValidCheat(start, end, fastestLegalTime)
                            if (cheat != null) {
                                cheats.add(cheat)
                            }
                        }
                    }
                }
            }
            return cheats
        }

        private fun isValidCheat(from: Point, to: Point, fastestLegalTime: Int): Cheat? {
            if (from == to
                || from.x !in 0 until width
                || from.y !in 0 until height
                || to.x !in 0 until width
                || to.y !in 0 until height
                || map[from.y][from.x] == '#'
                || map[to.y][to.x] == '#'
            ) {
                return null
            }

            val dist = abs(from.x - to.x) + abs(from.y - to.y)
            if (dist != 2) {
                return null // Cheat must be exactly 2 moves
            }

            val timeToStart = findFastestLegalTime(start, from)
            val timeToEnd = findFastestLegalTime(to, end)
            if (timeToStart == Int.MAX_VALUE || timeToEnd == Int.MAX_VALUE) {
                return null // Unreachable
            }

            val cheatTime = timeToStart + 2 + timeToEnd
            val timeSaved = fastestLegalTime - cheatTime
            if (timeSaved >= 0) {
                return Cheat(from, to, timeSaved)
            }

            return null
        }
    }

    override fun part1(): Int {
        val limit = if (inputAsList.size < 20) 10 else 100
        val racetrack = Racetrack(inputAsList)

        val fastestLegalTime = racetrack.findFastestLegalTime(racetrack.start, racetrack.end)
        val cheats = racetrack.findCheats(fastestLegalTime)

        return cheats.count { it.timeSaved >= limit }
    }

    override fun part2(): Long {
        return 0
    }
}

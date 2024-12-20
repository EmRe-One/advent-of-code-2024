package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.*

class Day20 : Day(
    20,
    2024,
    "Race Condition",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val limit = if (inputAsList.size < 20) 10 else 100
        val racetrack = Racetrack(inputAsList)

        val fastestLegalTime = racetrack.findFastestLegalTime(racetrack.start, racetrack.end)
        val cheats = racetrack.findCheats(2, fastestLegalTime)

        return cheats.count { it.timeSaved >= limit }
    }

    override fun part2(): Int {
        val limit = if (inputAsList.size < 20) 70 else 100
        val racetrack = Racetrack(inputAsList)

        val fastestLegalTime = racetrack.findFastestLegalTime(racetrack.start, racetrack.end)
        val cheats = racetrack.findCheats(20, fastestLegalTime)

        return cheats.count { it.timeSaved >= limit }
    }

    data class Cheat(val start: Point, val end: Point, val timeSaved: Int)

    class Racetrack(input: List<String>) {
        private val directions = listOf(Point(1, 0), Point(0, 1), Point(-1, 0), Point(0, -1))

        private val map = input.map { it.toCharArray() }
        private val cachedTimes = mutableMapOf<Pair<Point, Point>, Int>()
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
            val key = Pair(from, to)
            if (key in cachedTimes) {
                return cachedTimes[key]!!
            }

            val queue: Queue<Pair<Point, Int>> = LinkedList()
            val visited = mutableSetOf<Point>()
            queue.add(Pair(from, 0))
            visited.add(from)

            while (queue.isNotEmpty()) {
                val (current, time) = queue.poll()
                if (current == to) {
                    cachedTimes[key] = time
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

            cachedTimes[key] = Int.MAX_VALUE
            return Int.MAX_VALUE // Should not happen if there's a path
        }

        fun findCheats(cheatLength: Int, fastestLegalTime: Int): List<Cheat> {
            val cheats = mutableSetOf<Cheat>()

            for (x in 0 until width) {
                for (y in 0 until height) {
                    if (map[y][x] == '#') {
                        continue
                    }
                    val point = Point(x, y)
                    cheats.addAll(findCheatsFromPoint(point, cheatLength, fastestLegalTime))
                }
            }

            return cheats.toList()
        }

        private fun findCheatsFromPoint(point: Point, cheatLength: Int, fastestLegalTime: Int): List<Cheat> {
            val cheats = mutableListOf<Cheat>()
            val visited = mutableSetOf<Point>()

            fun explore(current: Point, path: List<Point>, remainingCheat: Int) {
                if (remainingCheat == 0 || current in visited || path.size > fastestLegalTime + cheatLength) {
                    return // Pruning: Stop if cheat is too long
                }
                visited.add(current)

                for (dir in directions) {
                    val next = Point(current.x + dir.x, current.y + dir.y)
                    if (next.x in 0 until width && next.y in 0 until height) {
                        val newPath = path + next
                        if (map[next.y][next.x] != '#') {
                            val timeSaved = calculateTimeSaved(point, next, newPath.size, fastestLegalTime)
                            if (timeSaved >= 0) {
                                cheats.add(Cheat(point, next, timeSaved))
                            }
                        }
                        explore(next, newPath, remainingCheat - 1)
                    }
                }
            }

            explore(point, listOf(point), cheatLength)
            return cheats
        }

        private fun calculateTimeSaved(from: Point, to: Point, cheatLength: Int, fastestLegalTime: Int): Int {
            val timeToStart = findFastestLegalTime(start, from)
            val timeToEnd = findFastestLegalTime(to, end)
            if (timeToStart == Int.MAX_VALUE || timeToEnd == Int.MAX_VALUE) {
                return -1
            }

            val cheatTime = timeToStart + cheatLength + timeToEnd
            return fastestLegalTime - cheatTime + 1 // +1 for the time it takes to move to the next point
        }
    }
}

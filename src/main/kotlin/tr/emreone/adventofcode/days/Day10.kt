package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point

class Day10 : Day(
    10,
    2024,
    "Hoof It",
    session = Resources.resourceAsString("session.cookie")
) {

    private val mapData = inputAsList.map { line -> line.map { it.toString().toInt() } }
    private val trailheads = findTrailheads(this.mapData)

    override fun part1(): Int {
        return this.trailheads.sumOf {
            bfsCountNines(this.mapData, it)
        }
    }

    override fun part2(): Int {
        return this.trailheads.sumOf {
            bfsTrailCountWithRating(this.mapData, it)
        }
    }

    private fun findTrailheads(topographicMap: List<List<Int>>): List<Point> {
        val trailheads = mutableListOf<Point>()
        for (y in topographicMap.indices) {
            for (x in topographicMap[y].indices) {
                if (topographicMap[y][x] == 0) {
                    trailheads.add(Pair(x, y))
                }
            }
        }
        return trailheads
    }

    private fun bfsCountNines(mapData: List<List<Int>>, start: Point): Int {
        val rows = mapData.size
        val cols = mapData[0].size
        val queue = mutableListOf(start)
        val visited = mutableSetOf(start)
        var reachableNines = 0

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()

            listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)).forEach { (dx, dy) ->
                val nx = x + dx
                val ny = y + dy

                if (ny in 0 until rows && nx in 0 until cols && Pair(nx, ny) !in visited) {
                    if (mapData[ny][nx] == mapData[y][x] + 1) { // Valid trail step
                        visited.add(Pair(nx, ny))
                        queue.add(Pair(nx, ny))

                        if (mapData[ny][nx] == 9) {
                            reachableNines++
                        }
                    }
                }
            }
        }

        return reachableNines
    }

    private fun bfsTrailCountWithRating(mapData: List<List<Int>>, start: Point): Int {
        val rows = mapData.size
        val cols = mapData[0].size
        val queue = mutableListOf(Pair(start, mutableListOf(start))) // Pair of current position and trail
        var trailCount = 0

        while (queue.isNotEmpty()) {
            val (current, trail) = queue.removeFirst()
            val (x, y) = current

            listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1)).forEach { (dx, dy) ->
                val nx = x + dx
                val ny = y + dy

                if (ny in 0 until rows && nx in 0 until cols && !trail.contains(Pair(nx, ny))) {
                    if (mapData[ny][nx] == mapData[y][x] + 1) { // Valid trail step
                        val newTrail = trail.toMutableList()
                        newTrail.add(Pair(nx, ny))

                        if (mapData[ny][nx] == 9) {
                            trailCount++
                        } else {
                            queue.add(Pair(Pair(nx, ny), newTrail))
                        }
                    }
                }
            }
        }
        return trailCount
    }

}

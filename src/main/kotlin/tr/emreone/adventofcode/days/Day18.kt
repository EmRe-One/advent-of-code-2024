package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.*

class Day18 : Day(
    18,
    2024,
    "RAM Run",
    session = Resources.resourceAsString("session.cookie")
) {

    private val directions = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
    private val incomingBytes = inputAsList
        .map { it.split(",") }
        .map { it[0].toInt() to it[1].toInt() }

    // Grid size and limit for test and real input
    private val gridSize = if (incomingBytes.size < 100) 7 else 71
    private val kbLimit = if (incomingBytes.size < 100) 12 else 1024
    private val emptyGrid = Array(gridSize) { CharArray(gridSize) { '.' } }

    override fun part1(): Int {
        val grid = emptyGrid.map { it.copyOf() }.toTypedArray()

        // Simulate falling bytes
        for (i in incomingBytes.indices) {
            if (i >= kbLimit) break // Stop after the first 1024 bytes
            val (x, y) = incomingBytes[i]
            grid[y][x] = '#'
        }

        for(y in 0 until gridSize) {
            for(x in 0 until gridSize) {
                print(grid[y][x])
            }
            println()
        }

        // Function to find the shortest path using BFS
        fun findShortestPath(): Int {
            val visited = Array(gridSize) { BooleanArray(gridSize) { false } }
            val queue: Queue<Triple<Int, Int, Int>> = LinkedList() // (x, y, steps)

            queue.add(Triple(0, 0, 0))
            visited[0][0] = true

            while (queue.isNotEmpty()) {
                val (x, y, steps) = queue.poll()

                if (x == gridSize - 1 && y == gridSize - 1) {
                    return steps // Reached the exit
                }

                for ((dx, dy) in directions) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 0 until gridSize && ny in 0 until gridSize && !visited[ny][nx] && grid[ny][nx] == '.') {
                        visited[ny][nx] = true
                        queue.add(Triple(nx, ny, steps + 1))
                    }
                }
            }

            return -1 // No path found
        }

        return findShortestPath()
    }

    override fun part2(): String {
        val grid = emptyGrid.map { it.copyOf() }.toTypedArray()

        // Function to check if a path exists using BFS
        fun pathExists(): Boolean {
            val visited = Array(gridSize) { BooleanArray(gridSize) { false } }
            val queue: Queue<Pair<Int, Int>> = LinkedList()

            queue.add(Pair(0, 0))
            visited[0][0] = true

            while (queue.isNotEmpty()) {
                val (x, y) = queue.poll()

                if (x == gridSize - 1 && y == gridSize - 1) {
                    return true // Reached the exit
                }

                for ((dx, dy) in directions) {
                    val nx = x + dx
                    val ny = y + dy
                    if (nx in 0 until gridSize && ny in 0 until gridSize && !visited[ny][nx] && grid[ny][nx] == '.') {
                        visited[ny][nx] = true
                        queue.add(Pair(nx, ny))
                    }
                }
            }

            return false // No path found
        }

        // Simulate falling bytes and check connectivity
        for ((index, byte) in incomingBytes.withIndex()) {
            val (x, y) = byte
            grid[y][x] = '#' // Mark the byte as corrupted

            if (!pathExists()) {
                return "${x},${y}"
            }
        }

        return "No corrupted byte found"
    }

}

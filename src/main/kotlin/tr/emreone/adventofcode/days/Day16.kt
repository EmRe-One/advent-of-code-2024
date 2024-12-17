package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.*

class Day16 : Day(
    16,
    2024,
    "Reindeer Maze",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val maze = inputAsList
        val rows = maze.size
        val cols = maze[0].length
        val directions = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1)) // East, South, West, North
        val start = findPosition(maze, 'S')
        val end = findPosition(maze, 'E')

        val visited = Array(rows) { Array(cols) { BooleanArray(4) } }
        val priorityQueue = PriorityQueue<State>()

        priorityQueue.add(State(start.first, start.second, 0, 0)) // Add all directions from Start

        while (priorityQueue.isNotEmpty()) {
            val (x, y, direction, score) = priorityQueue.poll()

            if (Pair(x,y) == end) return score // Reached End Tile

            if (visited[y][x][direction]) continue
            visited[y][x][direction] = true

            // Option 1: Move Forward
            val forwardX = x + directions[direction].first
            val forwardY = y + directions[direction].second
            if (isValid(forwardX, forwardY, rows, cols, maze)) {
                priorityQueue.add(State(forwardX, forwardY, direction, score + 1))
            }

            // Option 2: Rotate Clockwise
            val clockwiseDir = (direction + 1) % 4
            priorityQueue.add(State(x, y, clockwiseDir, score + 1000))

            // Option 3: Rotate Counterclockwise
            val counterclockwiseDir = (direction + 3) % 4
            priorityQueue.add(State(x, y, counterclockwiseDir, score + 1000))
        }

        return -1 // Should never reach here
    }

    override fun part2(): Int {
        val maze = inputAsList
        val rows = maze.size
        val cols = maze[0].length
        val directions = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1)) // East, South, West, North
        val start = findPosition(maze, 'S')
        val end = findPosition(maze, 'E')

        val visited = Array(rows) { Array(cols) { IntArray(4) { Int.MAX_VALUE } } }
        val parent = Array(rows) { Array(cols) { mutableListOf<Pair<Int, Int>>() } } // For backtracking

        val priorityQueue = PriorityQueue<State>()

        // Start from S Tile with East direction
        priorityQueue.add(State(start.first, start.second, 0, 0))
        visited[start.second][start.first][0] = 0

        while (priorityQueue.isNotEmpty()) {
            val (x, y, direction, score) = priorityQueue.poll()

            if (score > visited[y][x][direction]) continue

            // Move Forward
            val forwardX = x + directions[direction].first
            val forwardY = y + directions[direction].second
            if (isValid(forwardX, forwardY, rows, cols, maze)) {
                val newScore = score + 1
                if (newScore <= visited[forwardY][forwardX][direction]) {
                    visited[forwardY][forwardX][direction] = newScore
                    parent[forwardY][forwardX].add(Pair(x, y))
                    priorityQueue.add(State(forwardX, forwardY, direction, newScore))
                }
            }

            // Rotate Clockwise
            val clockwiseDir = (direction + 1) % 4
            val clockwiseScore = score + 1000
            if (clockwiseScore <= visited[y][x][clockwiseDir]) {
                visited[y][x][clockwiseDir] = clockwiseScore
                parent[y][x].add(Pair(x, y))
                priorityQueue.add(State(x, y, clockwiseDir, clockwiseScore))
            }

            // Rotate Counterclockwise
            val counterclockwiseDir = (direction + 3) % 4
            val counterclockwiseScore = score + 1000
            if (counterclockwiseScore <= visited[y][x][counterclockwiseDir]) {
                visited[y][x][counterclockwiseDir] = counterclockwiseScore
                parent[y][x].add(Pair(x, y))
                priorityQueue.add(State(x, y, counterclockwiseDir, counterclockwiseScore))
            }
        }

        // Backtrack to find all tiles part of best paths
        val bestPathTiles = mutableSetOf<Pair<Int, Int>>()
        backtrack(end.first, end.second, parent, bestPathTiles)

        return bestPathTiles.size
    }

    data class State(
        val x: Int,
        val y: Int,
        val direction: Int,
        val score: Int
    ) : Comparable<State> {
        override fun compareTo(other: State): Int = this.score.compareTo(other.score)
    }

    private fun findPosition(maze: List<String>, target: Char): Pair<Int, Int> {
        for (y in maze.indices) {
            for (x in maze[y].indices) {
                if (maze[y][x] == target) return Pair(x, y)
            }
        }
        throw IllegalArgumentException("Target $target not found in maze")
    }

    private fun isValid(x: Int, y: Int, rows: Int, cols: Int, maze: List<String>): Boolean {
        return x in 0 until cols && y in 0 until rows && maze[y][x] != '#'
    }

    private fun backtrack(x: Int, y: Int, parent: Array<Array<MutableList<Pair<Int, Int>>>>, bestPathTiles: MutableSet<Pair<Int, Int>>) {
        if (!bestPathTiles.add(Pair(x, y))) return // Avoid revisiting tiles
        for ((px, py) in parent[y][x]) {
            backtrack(px, py, parent, bestPathTiles)
        }
    }

}

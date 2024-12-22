package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
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

        val start = findPosition(maze, 'S')
        val end = findPosition(maze, 'E')

        val visited = Array(rows) { Array(cols) { BooleanArray(4) } }
        val priorityQueue = PriorityQueue<State>()

        priorityQueue.add(State(start, Direction4.EAST, 0))

        while (priorityQueue.isNotEmpty()) {
            val (p, direction, score) = priorityQueue.poll()

            if (p == end) return score // Reached End Tile

            if (visited[p.y][p.x][direction.ordinal]) continue
            visited[p.y][p.x][direction.ordinal] = true

            // Option 1: Move Forward
            val forward = p + direction.vector
            if (isValid(forward, rows, cols, maze)) {
                priorityQueue.add(State(forward, direction, score + 1))
            }

            // Option 2: Rotate Clockwise
            val clockwiseDir = direction.right
            priorityQueue.add(State(p, clockwiseDir, score + 1000))

            // Option 3: Rotate Counterclockwise
            val counterclockwiseDir = direction.left
            priorityQueue.add(State(p, counterclockwiseDir, score + 1000))
        }

        return -1 // Should never reach here
    }

    override fun part2(): Int {
        val maze = inputAsList
        val rows = maze.size
        val cols = maze[0].length

        val start = findPosition(maze, 'S')
        val end = findPosition(maze, 'E')

        val visited = Array(rows) { Array(cols) { IntArray(4) { Int.MAX_VALUE } } }
        val parent = Array(rows) { Array(cols) { mutableListOf<Point>() } } // For backtracking

        val priorityQueue = PriorityQueue<State>()

        // Start from S Tile with East direction
        priorityQueue.add(State(start, Direction4.EAST, 0))

        while (priorityQueue.isNotEmpty()) {
            val (p, direction, score) = priorityQueue.poll()

            if (score > visited[p.y][p.x][direction.ordinal]) continue

            // Move Forward
            val forward = p + direction.vector
            if (isValid(forward, rows, cols, maze)) {
                val newScore = score + 1
                if (newScore <= visited[forward.y][forward.x][direction.ordinal]) {
                    visited[forward.y][forward.x][direction.ordinal] = newScore
                    parent[forward.y][forward.x].add(p)
                    priorityQueue.add(State(forward, direction, newScore))
                }
            }

            // Rotate Clockwise
            val clockwiseDir = direction.right
            val clockwiseScore = score + 1000
            if (clockwiseScore <= visited[p.y][p.x][clockwiseDir.ordinal]) {
                visited[p.y][p.x][clockwiseDir.ordinal] = clockwiseScore
                parent[p.y][p.x].add(p)
                priorityQueue.add(State(p, clockwiseDir, clockwiseScore))
            }

            // Rotate Counterclockwise
            val counterclockwiseDir = direction.left
            val counterclockwiseScore = score + 1000
            if (counterclockwiseScore <= visited[p.y][p.x][counterclockwiseDir.ordinal]) {
                visited[p.y][p.x][counterclockwiseDir.ordinal] = counterclockwiseScore
                parent[p.y][p.x].add(p)
                priorityQueue.add(State(p, counterclockwiseDir, counterclockwiseScore))
            }
        }

        // Backtrack to find all tiles part of best paths
        val bestPathTiles = mutableSetOf<Pair<Int, Int>>()
        backtrack(end.first, end.second, parent, bestPathTiles)

        return bestPathTiles.size
    }

    data class State(
        val p: Point,
        val direction: Direction4,
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

    private fun isValid(p: Point, rows: Int, cols: Int, maze: List<String>): Boolean {
        return p.x in 0 until cols && p.y in 0 until rows && maze[p.y][p.x] != '#'
    }

    private fun backtrack(
        x: Int,
        y: Int,
        parent: Array<Array<MutableList<Pair<Int, Int>>>>,
        bestPathTiles: MutableSet<Pair<Int, Int>>
    ) {
        if (!bestPathTiles.add(Pair(x, y))) return // Avoid revisiting tiles
        for ((px, py) in parent[y][x]) {
            backtrack(px, py, parent, bestPathTiles)
        }
    }

}

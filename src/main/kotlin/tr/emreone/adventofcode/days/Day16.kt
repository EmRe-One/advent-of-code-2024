package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log
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

    class Maze(input: List<String>) {
        val maze = input
        val height = maze.size
        val width = maze[0].length

        val start = findPosition('S')
        val end = findPosition('E')

        private fun findPosition(symbol: Char): Point {
            for (y in maze.indices) {
                for (x in maze[y].indices) {
                    if (maze[y][x] == symbol) return Point(x, y)
                }
            }
            throw IllegalArgumentException("Target '$symbol' not found in maze")
        }

        fun isValidPosition(p: Point): Boolean {
            return p.x in 0 until this.width && p.y in 0 until this.height && maze[p.y][p.x] != '#'
        }

        fun backtrack(endStates: Set<State>, backtrack: Map<State, Set<State>>): Set<Point> {
            val bestPathTiles = mutableSetOf<Point>()

            val visited = mutableSetOf<State>()
            val queue = LinkedList<State>()
            queue.addAll(endStates)
            bestPathTiles.addAll(endStates.map { it.p })

            while (queue.isNotEmpty()) {
                val currentState = queue.poll()

                for (nextState in backtrack.getOrDefault(currentState, emptySet())) {
                    if (!visited.add(nextState)) continue

                    queue.add(nextState)
                    bestPathTiles.add(nextState.p)
                }
            }

            return bestPathTiles
        }
    }

    data class State(
        val p: Point,
        val direction: Direction4,
        val score: Int
    ) : Comparable<State> {
        override fun compareTo(other: State): Int = this.score.compareTo(other.score)
    }

    private val maze = Maze(inputAsList)

    override fun part1(): Int {
        val visited = Array(maze.height) { Array(maze.width) { BooleanArray(4) } }
        val priorityQueue = PriorityQueue<State>()

        priorityQueue.add(State(maze.start, Direction4.EAST, 0))

        while (priorityQueue.isNotEmpty()) {
            val (p, direction, score) = priorityQueue.poll()

            if (p == maze.end) return score // Reached End Tile

            if (visited[p.y][p.x][direction.ordinal]) continue
            visited[p.y][p.x][direction.ordinal] = true

            // Option 1: Move Forward
            val forward = p + direction.vector
            if (maze.isValidPosition(forward)) {
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
        val priorityQueue = PriorityQueue<State>()
        val lowestCosts = mutableMapOf<Pair<Point, Direction4>, Int>()
        var bestCost = Int.MAX_VALUE
        val backtrack = mutableMapOf<State, Set<State>>() // For backtracking
        val endStates = mutableSetOf<State>()

        // Start from S Tile with East direction
        priorityQueue.add(State(maze.start, Direction4.EAST, 0))
        lowestCosts[Pair(maze.start, Direction4.EAST)] = 0

        while (priorityQueue.isNotEmpty()) {
            val currentState = priorityQueue.poll()

            val (p, direction, score) = currentState
            if (score > lowestCosts.getOrDefault(Pair(p, direction), Int.MAX_VALUE)) continue

            if (maze.maze[p.y][p.x] == 'E') {
                if (score > bestCost) break
                bestCost = score
                endStates.add(currentState)
            }

            listOf(
                State(p + direction.vector, direction, score + 1),   // Move Forward
                State(p, direction.right, score + 1000),                // Rotate Clockwise
                State(p, direction.left, score + 1000)                  // Rotate Counterclockwise
            ).forEach { state ->
                if (!maze.isValidPosition(state.p)) return@forEach

                val lowest = lowestCosts.getOrDefault(Pair(state.p, state.direction), Int.MAX_VALUE)

                if (state.score > lowest) return@forEach

                if (state.score < lowest) {
                    backtrack[state] = emptySet()
                    lowestCosts[Pair(state.p, state.direction)] = state.score
                }
                backtrack[state] = backtrack.getOrDefault(state, emptySet()) + currentState
                priorityQueue.add(state)
            }
        }

        log {
            backtrack.entries.joinToString("\n") { (state, nextStates) ->
                "$state -> $nextStates"
            }
            "End States: $endStates"
        }

        // Backtrack to find all tiles part of best paths
        val bestPathTiles = maze.backtrack(endStates, backtrack)

        log {
            "Best Path Tiles: $bestPathTiles"
        }

        return bestPathTiles.size
    }

}

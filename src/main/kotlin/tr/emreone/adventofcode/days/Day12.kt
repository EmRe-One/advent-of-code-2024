package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import java.util.*

class Day12 : Day(
    12,
    2024,
    "Garden Groups",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val gardenMap = inputAsList

        val rows = gardenMap.size
        val cols = gardenMap[0].length
        val visited = Array(rows) { BooleanArray(cols) }
        var totalCost = 0

        // Directions for moving up, right, down, left
        val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

        // Function to perform flood-fill
        fun floodFill(x: Int, y: Int, plantType: Char): Pair<Int, Int> {
            val stack = mutableListOf(Pair(x, y))
            var area = 0
            var perimeter = 0

            while (stack.isNotEmpty()) {
                val (xx, yy) = stack.removeAt(stack.size - 1)

                if (yy !in 0 until rows || xx !in 0 until cols || visited[yy][xx] || gardenMap[yy][xx] != plantType) {
                    continue
                }

                visited[yy][xx] = true
                area++

                // Check neighbors for perimeter and add valid neighbors to stack
                for ((dx, dy) in directions) {
                    val nx = xx + dx
                    val ny = yy + dy
                    if (ny !in 0 until rows || nx !in 0 until cols || gardenMap[ny][nx] != plantType) {
                        perimeter++ // Edge of region or boundary
                    } else if (!visited[ny][nx]) {
                        stack.add(Pair(nx, ny))
                    }
                }
            }

            return area to perimeter
        }

        // Iterate through each cell in the map
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                if (!visited[y][x]) {
                    val plantType = gardenMap[y][x]
                    val (area, perimeter) = floodFill(x, y, plantType)
                    totalCost += area * perimeter
                }
            }
        }

        return totalCost
    }

    override fun part2(): Int {
        val gardenMap = inputAsList
        val rows = gardenMap.size
        val cols = gardenMap[0].length
        val visited = Array(rows) { BooleanArray(cols) }
        var totalPrice = 0

        // Helper function for flood-fill
        fun floodFillAndCalculateSides(x: Int, y: Int, plantType: Char): Pair<Int, Int> {
            // Directions for moving up, right, down, left
            val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
            val queue: Queue<Pair<Int, Int>> = LinkedList()
            queue.add(Pair(x, y))
            visited[y][x] = true

            var area = 0
            var sides = 0
            val fenceEdges = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

            while (queue.isNotEmpty()) {
                val (curX, curY) = queue.poll()
                area++

                for ((dx, dy) in directions) {
                    val nx = curX + dx
                    val ny = curY + dy

                    // Define the current edge for sides calculation
                    val currentEdge = if (dx != 0) {
                        if (dx > 0) Pair(Pair(curX, curY), Pair(nx, ny)) else Pair(Pair(nx, ny), Pair(curX, curY))
                    } else {
                        if (dy > 0) Pair(Pair(curX, curY), Pair(nx, ny)) else Pair(Pair(nx, ny), Pair(curX, curY))
                    }

                    // Check if within bounds
                    if (nx in 0 until rows && ny in 0 until cols) {
                        if (gardenMap[ny][nx] == plantType) {
                            if (!visited[ny][nx]) {
                                visited[ny][nx] = true
                                queue.add(Pair(nx, ny))
                            }
                        } else {
                            // Add the edge to the fence if it's on the boundary
                            fenceEdges.add(currentEdge)
                        }
                    } else {
                        // Add the edge to the fence if it's out of bounds
                        fenceEdges.add(currentEdge)
                    }
                }
            }

            sides = fenceEdges.size
            return area to sides
        }

        // Iterate through the garden map
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                if (!visited[y][x]) {
                    val plantType = gardenMap[y][x]
                    val (area, sides) = floodFillAndCalculateSides(x, y, plantType)
                    totalPrice += area * sides
                }
            }
        }

        return totalPrice
    }
}

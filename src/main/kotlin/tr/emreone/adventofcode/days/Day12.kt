package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day12 : Day(
    12,
    2024,
    "Garden Groups",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        return this.calculateFencingCost(inputAsList)
    }

    override fun part2(): Int {
        return 0
    }

    data class RegionStats(val area: Int, val perimeter: Int)

    private fun calculateFencingCost(gardenMap: List<String>): Int {
        val rows = gardenMap.size
        val cols = gardenMap[0].length
        val visited = Array(rows) { BooleanArray(cols) }
        var totalCost = 0

        // Directions for moving up, right, down, left
        val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

        // Function to perform flood-fill
        fun floodFill(x: Int, y: Int, plantType: Char): RegionStats {
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

            return RegionStats(area, perimeter)
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
}

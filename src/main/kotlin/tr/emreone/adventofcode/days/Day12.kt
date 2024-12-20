package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.*

class Day12 : Day(
    12,
    2024,
    "Garden Groups",
    session = Resources.resourceAsString("session.cookie")
) {

    // Directions for moving up, right, down, left

    class Garden(private val input: List<String>) {
        private val garden = input.map { it.toList() }
        val height = garden.size
        val width = garden[0].size

        private val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
        val regions = mutableListOf<Pair<Char, MutableList<Point>>>()

        fun analyzeMap() {
            val visited = Array(this.height) { BooleanArray(this.width) }
            for (y in 0 until this.height) {
                for (x in 0 until this.width) {
                    if (!visited[y][x]) {
                        val region = mutableListOf<Point>()
                        val plantType = getPlantTypeAt(x, y)
                        floodFill(visited, Point(x, y), plantType, region)
                        regions.add(plantType to region)
                    }
                }
            }
        }

        fun getPlantTypeAt(x: Int, y: Int): Char {
            return garden[y][x]
        }

        fun floodFill(
            visited: Array<BooleanArray>,
            p: Point,
            plantType: Char,
            region: MutableList<Point>
        ) {
            if (p.y < 0 || p.y >= this.height || p.x < 0 || p.x >= width || visited[p.y][p.x] || this.garden[p.y][p.x] != plantType) {
                return
            }

            visited[p.y][p.x] = true
            region.add(p)

            for ((dx, dy) in directions) {
                val nx = p.x + dx
                val ny = p.y + dy

                floodFill(visited, Point(nx, ny), plantType, region)
            }
        }

        fun calculatePerimeter(plantType: Char, region: List<Pair<Int, Int>>): Int {
            return region.sumOf { (x, y) ->
                directions.count { (dx, dy) ->
                    val nx = x + dx
                    val ny = y + dy

                    nx < 0 || nx >= width || ny < 0 || ny >= height || garden[ny][nx] != plantType
                }
            }
        }

        fun calculateSides(plantType: Char, region: List<Pair<Int, Int>>): Int {
            var perimeter = this.calculatePerimeter(plantType, region)

            // Korrektur für innere Kanten
            var innerEdges = 0
            for ((y, x) in region) {
                if (y > 0 && garden[y - 1][x] == plantType) innerEdges++
                if (y < this.height - 1 && garden[y + 1][x] == plantType) innerEdges++
                if (x > 0 && garden[y][x - 1] == plantType) innerEdges++
                if (x < this.width - 1 && garden[y][x + 1] == plantType) innerEdges++
            }
            perimeter -= innerEdges / 2 // Jede innere Kante wird zweimal gezählt

            return perimeter
        }
    }

    override fun part1(): Int {
        val garden = Garden(inputAsList)
        garden.analyzeMap()

        return garden.regions.sumOf { (plantType, region) ->
            val perimeter = garden.calculatePerimeter(plantType, region)
            println("Plant type: $plantType, region: $region, size: ${region.size}, perimeter: $perimeter")
            region.size * perimeter
        }
    }

    override fun part2(): Int {
        val garden = Garden(inputAsList)

        val visited = Array(garden.height) { BooleanArray(garden.width) }
        var totalPrice = 0

        /*// Helper function for flood-fill
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
*/
        return totalPrice
    }
}

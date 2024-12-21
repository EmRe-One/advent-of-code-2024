package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import kotlin.math.abs

class Day12 : Day(
    12,
    2024,
    "Garden Groups",
    session = Resources.resourceAsString("session.cookie")
) {

    class FencableRegion(val plantType: Char, val points: List<Point>) {
        val area = points.size

        val perimeter: Int
            get() {
                return points.sumOf { point ->
                    Direction4.entries.count { direction ->
                        val n = point + direction.vector
                        n !in points
                    }
                }
            }

        val sides: List<Set<Point>>
            get() {
                val topEdges = points.filter { point ->
                    val n = point + Direction4.NORTH.vector
                    n !in points
                }
                val rightEdges = points.filter { point ->
                    val n = point + Direction4.EAST.vector
                    n !in points
                }
                val bottomEdges = points.filter { point ->
                    val n = point + Direction4.SOUTH.vector
                    n !in points
                }
                val leftEdges = points.filter { point ->
                    val n = point + Direction4.WEST.vector
                    n !in points
                }

                val allEdges = mutableListOf(topEdges, rightEdges, bottomEdges, leftEdges)

                val sides = mutableListOf<Set<Point>>()
                for (edges in allEdges) {
                    // one of top, right, bottom, left edges
                    val currentEdgeGroup = edges.toMutableList()

                    while (currentEdgeGroup.isNotEmpty()) {
                        val firstEdge = currentEdgeGroup.removeFirst()
                        val side = findNeighbors(firstEdge, currentEdgeGroup)
                        currentEdgeGroup.removeAll(side)
                        sides.add(side)
                    }
                }

                return sides
            }

        private fun findNeighbors(edge: Point, edges: MutableList<Point>): Set<Point> {
            val neighbors = mutableSetOf(edge)
            for (other in edges) {
                if (neighbors.any { isNeighbor(it, other) }) {
                    neighbors.add(other)
                }
            }
            return neighbors
        }

        private fun isNeighbor(p1: Point, p2: Point): Boolean {
            return Direction4.entries.any {
                p2 == p1 + it.vector
            }
        }
    }

    class Garden(private val input: List<String>) {
        private val garden = input.map { it.toList() }
        val height = garden.size
        val width = garden[0].size

        private val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))
        val regions = mutableListOf<FencableRegion>()

        fun analyzeMap() {
            val visited = Array(this.height) { BooleanArray(this.width) }
            for (y in 0 until this.height) {
                for (x in 0 until this.width) {
                    if (!visited[y][x]) {
                        val region = mutableListOf<Point>()
                        val plantType = getPlantTypeAt(x, y)
                        floodFill(visited, Point(x, y), plantType, region)

                        regions.add(FencableRegion(plantType, region))
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

        /*fun calculateSides(plantType: Char, region: List<Pair<Int, Int>>): Int {
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
        }*/
    }

    private val garden = Garden(inputAsList)

    init {
        garden.analyzeMap()
    }

    override fun part1(): Int {
        return garden.regions.sumOf {
            // println("Plant type: ${it.plantType}, region: ${it.points}, size: ${it.area}, perimeter: ${it.perimeter}")
            it.area * it.perimeter
        }
    }

    override fun part2(): Int {
        return garden.regions.sumOf {
            println("Plant type: ${it.plantType}, size: ${it.area}, sides: ${it.sides.size}")
//            it.sides.forEach { side ->
//                println("-> Side: $side")
//            }
            it.area * it.sides.size
        }
    }
}

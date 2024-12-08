package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.areaOf
import tr.emreone.kotlin_utils.math.contains
import tr.emreone.kotlin_utils.math.minus
import tr.emreone.kotlin_utils.math.plus

class Day08 : Day(
    8,
    2024,
    "Resonant Collinearity",
    session = Resources.resourceAsString("session.cookie")
) {
    private val topLeft = Point(0, 0)
    private val bottomRight = Point(inputAsGrid[0].size - 1, inputAsGrid.size - 1)
    private val area = areaOf(topLeft, bottomRight)

    private val antennas = emptyMap<Char, MutableList<Point>>().toMutableMap()

    init {
        inputAsGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell == '.') return@forEachIndexed

                val temp = antennas.getOrElse(cell) { mutableListOf() }
                temp.add(Point(x, y))
                antennas[cell] = temp
            }
        }
    }

    private fun generateAntiNodes(antiNodesRule: (Point, Point) -> List<Point>): Set<Point> {
        val antiNodes = emptySet<Point>().toMutableSet()

        antennas.forEach { (_, points) ->
            points.forEachIndexed { index, a ->
                for(i in index + 1 until points.size) {
                    val b = points[i]
                    antiNodes.addAll(antiNodesRule(a, b))
                }
            }
        }

        return antiNodes
    }

    private fun antiNodesRulePart1(a: Point, b: Point): List<Point> {
        val vectorAB = b.minus(a)

        val antiNode = a.minus(vectorAB)
        val antiNode2 = b.plus(vectorAB)

        return listOf(antiNode, antiNode2).filter { area.contains(it) }
    }

    private fun antiNodesRulePart2(a: Point, b: Point): List<Point> {
        val vectorAB = b.minus(a)

        return generateSequence(a) { it.minus(vectorAB)}.takeWhile { area.contains(it) }.toList() +
                generateSequence(b) { it.plus(vectorAB) }.takeWhile { area.contains(it) }.toList()
    }

    override fun part1(): Int {
        val antiNodes = this.generateAntiNodes(::antiNodesRulePart1)

        return antiNodes.size
    }

    override fun part2(): Int {
        val antiNodes = this.generateAntiNodes(::antiNodesRulePart2)

        return antiNodes.size
    }

}

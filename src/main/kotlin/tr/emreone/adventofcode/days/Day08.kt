package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Area
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.areaOf
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

class Day08 : Day(
    8,
    2024,
    "Resonant Collinearity",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val topLeft = Point(0, 0)
        val bottomRight = Point(inputAsGrid[0].size, inputAsGrid.size)
        val area = areaOf(topLeft, bottomRight)

        val antennas = emptyMap<Char, MutableList<Point>>().toMutableMap()

        inputAsGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell == '.') return@forEachIndexed

                val temp = antennas.getOrElse(cell) { mutableListOf() }
                temp.add(Point(x, y))
                antennas[cell] = temp
            }
        }

        val antiNodes = emptySet<Point>().toMutableSet()

        antennas.forEach { (_, points) ->
            points.forEachIndexed { index, pair ->
                for(i in index + 1 until points.size) {
                    val pair2 = points[i]

                    val deltaX = pair2.x - pair.x
                    val deltaY = pair2.y - pair.y

                    
                }
            }
        }

        antennas.forEach {
            println(it)
        }

        return 0
    }

    override fun part2(): Long {
        return 0
    }

}

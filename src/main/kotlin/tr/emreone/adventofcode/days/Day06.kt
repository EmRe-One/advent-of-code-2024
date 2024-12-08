package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.areaOf
import tr.emreone.kotlin_utils.math.contains
import tr.emreone.kotlin_utils.math.neighbor

class Day06 : Day(
    6,
    2024,
    "Guard Gallivant",
    session = Resources.resourceAsString("session.cookie")
) {

    val obstructions = mutableListOf<Point>()
    var startingPosition: Point = Point(0, 0)
    val visitedCells = emptySet<Point>().toMutableSet()

    init {
        inputAsGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                if (cell == '#') {
                    obstructions.add(Point(x, y))
                } else if (cell == '^') {
                    startingPosition = Point(x, y)
                }
            }
        }
    }

    override fun part1(): Int {
        val area = areaOf(0 to 0, inputAsGrid.size - 1 to inputAsGrid[0].size - 1)
        var currentCell = startingPosition
        var currentDirection = Direction4.NORTH

        visitedCells.add(currentCell)
        while (true) {
            val neighborCell = currentCell.neighbor(currentDirection, 1)
            if (neighborCell in obstructions) {
                currentDirection = turnRight(currentDirection)
            } else if (area.contains(neighborCell)) {
                currentCell = neighborCell
                visitedCells.add(currentCell)
            } else {
                break
            }
        }

        return visitedCells.size
    }

    override fun part2(): Int {
        val area = areaOf(0 to 0, inputAsGrid.size - 1 to inputAsGrid[0].size - 1)
        val obstructionForLoops = mutableListOf<Point>()
        assert(visitedCells.isNotEmpty()) {
            "Part 1 should be run before part 2"
        }

        visitedCells.forEach { point ->

            val visitedCells = emptySet<Pair<Point, Direction4>>().toMutableSet()
            val tempObstructions = obstructions.toMutableList()

            var currentCell = startingPosition
            var currentDirection = Direction4.NORTH
            tempObstructions.add(point)
            visitedCells.add(currentCell to currentDirection)

            while (true) {
                val neighborCell = currentCell.neighbor(currentDirection, 1)
                if (neighborCell in tempObstructions) {
                    currentDirection = turnRight(currentDirection)
                } else if (area.contains(neighborCell)) {
                    currentCell = neighborCell
                    if (visitedCells.contains(currentCell to currentDirection)) {
                        obstructionForLoops.add(point)
                        return@forEach
                    }
                    visitedCells.add(currentCell to currentDirection)
                } else {
                    break
                }
            }
        }

        return obstructionForLoops.size
    }

    private fun turnRight(direction: Direction4): Direction4 {
        return when (direction) {
            Direction4.NORTH -> Direction4.EAST
            Direction4.EAST -> Direction4.SOUTH
            Direction4.SOUTH -> Direction4.WEST
            Direction4.WEST -> Direction4.NORTH
        }
    }
}

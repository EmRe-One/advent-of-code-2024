package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

class Day15 : Day(
    15,
    2024,
    "Warehouse Woes",
    session = Resources.resourceAsString("session.cookie")
) {

    class WareHouse(private val input: List<List<String>>) {
        private val warehouse = input[0].map { it.toMutableList() }.toMutableList()
        private val moves = input[1][0]

        private val directions = mapOf(
            '^' to Pair(0, -1),
            '>' to Pair(1, 0),
            'v' to Pair(0, 1),
            '<' to Pair(-1, 0)
        )
        private var robotPosition = findRobotPosition()

        // Function to check if a position is valid (not a wall)
        private fun isValid(pos: Point): Boolean {
            return warehouse[pos.y][pos.x] != '#'
        }

        private fun findRobotPosition(): Pair<Int, Int> {
            for (y in warehouse.indices) {
                for (x in warehouse[y].indices) {
                    if (warehouse[y][x] == '@') return Pair(x, y)
                }
            }
            throw IllegalStateException("Robot not found!")
        }

        private fun canMove(pos: Pair<Int, Int>): Boolean {
            val (x, y) = pos
            return y in warehouse.indices && x in warehouse[y].indices && warehouse[y][x] != '#'
        }

        private fun moveRobot(from: Pair<Int, Int>, to: Pair<Int, Int>) {
            warehouse[to.y][to.x] = '@'
            warehouse[from.y][from.x] = '.'
        }

        private fun moveBox(from: Pair<Int, Int>, to: Pair<Int, Int>) {
            warehouse[to.y][to.x] = 'O'
            warehouse[from.y][from.x] = '.'
        }

        private fun findPushDestination(boxPos: Pair<Int, Int>, direction: Pair<Int, Int>): Pair<Int, Int>? {
            var current = boxPos
            while (true) {
                val next = Pair(current.first + direction.first, current.second + direction.second)
                if (!canMove(next)) break // Stop if we hit a wall
                if (warehouse[next.y][next.x] == '.') return next // Return empty destination
                current = next
            }
            return null // No valid push destination
        }

        fun printWarehouse() {
            warehouse.forEach { println(it.joinToString("")) }
        }

        fun simulateMoves() {
            for (move in moves) {
                val direction = directions[move]!!
                val nextPos = Pair(robotPosition.first + direction.first, robotPosition.second + direction.second)

                if (canMove(nextPos)) {
                    when (warehouse[nextPos.y][nextPos.x]) {
                        '.' -> {
                            // Move robot to empty space
                            moveRobot(robotPosition, nextPos)
                            robotPosition = nextPos
                        }
                        'O' -> {
                            // Try to push the box
                            val boxNextPos = findPushDestination(nextPos, direction)
                            if (boxNextPos != null) {
                                moveBox(nextPos, boxNextPos)
                                moveRobot(robotPosition, nextPos)
                                robotPosition = nextPos
                            }
                        }
                    }
                }
            }
        }

        fun calculateGPS(): Int {
            var sum = 0
            for (y in warehouse.indices) {
                for (x in warehouse[y].indices) {
                    if (warehouse[y][x] == 'O') {
                        sum += 100 * y + x
                    }
                }
            }
            return sum
        }
    }

    override fun part1(): Int {
        val wareHouse = WareHouse(inputAsGroups)
        wareHouse.printWarehouse()
        wareHouse.simulateMoves()
        wareHouse.printWarehouse()
        return wareHouse.calculateGPS()
    }

    override fun part2(): Long {
        return 0
    }
}

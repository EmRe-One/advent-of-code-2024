package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.LinkedList
import java.util.Queue

class Day15 : Day(
    15,
    2024,
    "Warehouse Woes",
    session = Resources.resourceAsString("session.cookie")
) {

    class WareHouse(input: List<List<String>>) {
        private val warehouse = input[0].map { it.toMutableList() }.toMutableList()
        private val moves = input[1].joinToString("")

        private val directions = mapOf(
            '^' to Direction4.NORTH,
            '>' to Direction4.EAST,
            'v' to Direction4.SOUTH,
            '<' to Direction4.WEST
        )
        private var robotPosition = findRobotPosition()

        private fun findRobotPosition(): Point {
            for (y in warehouse.indices) {
                for (x in warehouse[y].indices) {
                    if (warehouse[y][x] == '@') return Point(x, y)
                }
            }
            throw IllegalStateException("Robot not found!")
        }

        private fun clearPosition(pos: Point) {
            warehouse[pos.y][pos.x] = '.'
        }

        private fun moveRobot(from: Point, inDirection: Direction4) {
            val newRobotPos = from + inDirection.vector
            warehouse[newRobotPos.y][newRobotPos.x] = '@'
        }

        private fun moveBox(from: Point, inDirection: Direction4) {
            val newBoxPos = from + inDirection.vector
            warehouse[newBoxPos.y][newBoxPos.x] = 'O'
        }

        fun printWarehouse() {
            warehouse.forEach { println(it.joinToString("")) }
        }

        fun simulateMoves() {
            for (move in moves) {
                val direction = directions[move]!!

                var canMove = true
                var currentPosition = robotPosition
                val targets = mutableListOf(currentPosition)

                while (true) {
                    currentPosition += direction.vector
                    when (warehouse[currentPosition.y][currentPosition.x]) {
                        '#' -> {
                            canMove = false
                            break
                        }

                        'O' -> {
                            targets.add(currentPosition)
                        }

                        '.' -> {
                            break
                        }
                    }
                }

                if (!canMove) continue

                this.clearPosition(robotPosition)
                targets.forEachIndexed { index, loc ->
                    if (index == 0) {
                        this.moveRobot(loc, direction)
                    } else {
                        this.moveBox(loc, direction)
                    }
                }
                robotPosition = robotPosition.copy() + direction.vector
            }
        }

        fun calculateGpsScore(): Int {
            return this.warehouse.indices.sumOf { y ->
                this.warehouse[y].indices.sumOf { x ->
                    if (this.warehouse[y][x] == 'O') 100 * y + x else 0
                }
            }
        }
    }

    class BigWareHouse(input: List<List<String>>) {
        private val warehouse = input[0]
            .map { line ->
                line.map { cell ->
                    when (cell) {
                        '#' -> "##"
                        '.' -> ".."
                        'O' -> "[]"
                        '@' -> "@."
                        else -> throw IllegalStateException("Invalid cell: $cell")
                    }
                }
                    .joinToString("")
                    .toMutableList()
            }
            .toMutableList()
        private val moves = input[1].joinToString("")

        private val directions = mapOf(
            '^' to Direction4.NORTH,
            '>' to Direction4.EAST,
            'v' to Direction4.SOUTH,
            '<' to Direction4.WEST
        )
        private var robotPosition = findRobotPosition()

        private fun findRobotPosition(): Point {
            for (y in warehouse.indices) {
                for (x in warehouse[y].indices) {
                    if (warehouse[y][x] == '@') return Point(x, y)
                }
            }
            throw IllegalStateException("Robot not found!")
        }

        private fun clearPosition(pos: Point) {
            warehouse[pos.y][pos.x] = '.'
        }

        private fun moveRobot(from: Point, inDirection: Direction4) {
            val newRobotPos = from + inDirection.vector
            warehouse[newRobotPos.y][newRobotPos.x] = '@'
        }

        private fun swapCell(from: Point, inDirection: Direction4) {
            val newCellPos = from + inDirection.vector
            val movingChar = warehouse[from.y][from.x]
            val otherChar = warehouse[newCellPos.y][newCellPos.x]

            warehouse[newCellPos.y][newCellPos.x] = movingChar
            warehouse[from.y][from.x] = otherChar
        }

        fun printWarehouse() {
            warehouse.forEach { println(it.joinToString("")) }
        }

        fun simulateMoves() {
            for (move in moves) {
                val direction = directions[move]!!

                var canMove = true
                val targets = mutableListOf(robotPosition)

                val checkPositions: Queue<Point> = LinkedList()
                checkPositions.add(robotPosition)

                while (checkPositions.isNotEmpty()) {
                    val neighbor = checkPositions.poll() + direction.vector

                    // because already checked
                    if (neighbor in targets) continue

                    when (warehouse[neighbor.y][neighbor.x]) {
                        '#' -> {
                            canMove = false
                            break
                        }

                        '[' -> {
                            val p2 = neighbor + Direction4.EAST.vector
                            targets.add(neighbor)
                            targets.add(p2)

                            checkPositions.add(neighbor)
                            checkPositions.add(p2)
                        }

                        ']' -> {
                            val p2 = neighbor + Direction4.WEST.vector
                            targets.add(neighbor)
                            targets.add(p2)

                            checkPositions.add(neighbor)
                            checkPositions.add(p2)
                        }
                    }
                }

                if (!canMove) continue

                targets.reversed().forEach { loc ->
                    this.swapCell(loc, direction)
                }
                robotPosition = robotPosition.copy() + direction.vector
            }
        }

        fun calculateGpsScore(): Int {
            return this.warehouse.indices.sumOf { y ->
                this.warehouse[y].indices.sumOf { x ->
                    if (this.warehouse[y][x] == '[') 100 * y + x else 0
                }
            }
        }
    }

    override fun part1(): Int {
        val wareHouse = WareHouse(inputAsGroups)
        log {
            wareHouse.printWarehouse()
        }
        wareHouse.simulateMoves()
        log {
            wareHouse.printWarehouse()
        }
        return wareHouse.calculateGpsScore()
    }

    override fun part2(): Int {
        val wareHouse = BigWareHouse(inputAsGroups)
        log {
            wareHouse.printWarehouse()
        }
        wareHouse.simulateMoves()
        log {
            wareHouse.printWarehouse()
        }

        return wareHouse.calculateGpsScore()
    }
}

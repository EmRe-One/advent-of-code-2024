package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

class Day14 : Day(
    14,
    2024,
    "Restroom Redoubt",
    session = Resources.resourceAsString("session.cookie")
) {

    class Robot(var position: Point, private val velocity: Point) {
        fun move(maxX: Int, maxY: Int) {
            val newX = (this.position.x + this.velocity.x).mod(maxX)
            val newY = (this.position.y + this.velocity.y).mod(maxY)

            this.position = Point(newX, newY)
        }
    }

    class Bathroom(private val input: List<String>) {
        // 11x7 for example input, 101x103 for real input
        private val maxX = if (input.size < 15) 11 else 101
        private val maxY = if (input.size < 15) 7 else 103
        private val regex = "p=(\\d+),(\\d+) v=(-?\\d+),(-?\\d+)".toRegex()

        private val robots = input.map {
            regex.matchEntire(it)
                ?.destructured
                ?.let { (px, py, vx, vy) ->
                    Robot(
                        Point(px.toInt(), py.toInt()),
                        Point(vx.toInt(), vy.toInt())
                    )
                }
                ?: throw Exception("Invalid input")
        }

        fun tick() {
            this.robots.forEach { it.move(this.maxX, this.maxY) }
        }

        fun isChristmasTree(): Boolean {
            val numberOfRobotsWithNeighbors = this.robots.count { robot ->
                this.robots.any { otherRobot ->
                    val neighbours = listOf(
                        Point(robot.position.x, robot.position.y - 1),
                        Point(robot.position.x + 1, robot.position.y),
                        Point(robot.position.x, robot.position.y + 1),
                        Point(robot.position.x - 1, robot.position.y),
                    )

                    otherRobot.position in neighbours
                }
            }

            // assume that more than half of the robots have neighbors to build a Christmas tree
            return numberOfRobotsWithNeighbors > this.robots.size / 2
        }

        fun score(): Int {
            val leftTop = this.robots.count {
                it.position.x < maxX / 2 && it.position.y < maxY / 2
            }
            val rightTop = this.robots.count {
                it.position.x > maxX / 2 && it.position.y < maxY / 2
            }
            val leftBottom = this.robots.count {
                it.position.x < maxX / 2 && it.position.y > maxY / 2
            }
            val rightBottom = this.robots.count {
                it.position.x > maxX / 2 && it.position.y > maxY / 2
            }

            return leftTop * rightTop * leftBottom * rightBottom
        }

        fun print() {
            for (y in 0 until this.maxY) {
                for (x in 0 until this.maxX) {
                    val numberOfRobots = this.robots.count { it.position == Point(x, y) }

                    if (numberOfRobots > 0) {
                        print(numberOfRobots)
                    } else {
                        print(".")
                    }
                }
                println()
            }

        }
    }

    override fun part1(): Int {
        val bathroom = Bathroom(inputAsList)

        repeat(100) {
            bathroom.tick()
        }

        return bathroom.score()
    }

    override fun part2(): Int {
        if (inputAsList.size < 15) {
            return 0
        }

        val bathroom = Bathroom(inputAsList)

        var counter = 0
        while(true) {
            bathroom.tick()
            counter++

            if (bathroom.isChristmasTree()) {
                bathroom.print()
                break
            }
        }

        return counter
    }

}

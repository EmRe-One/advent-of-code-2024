package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.automation.log
import tr.emreone.kotlin_utils.math.Direction4
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.plus
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y
import java.util.*

class Day21 : Day(
    21,
    2024,
    "Keypad Conundrum",
    session = Resources.resourceAsString("session.cookie")
) {

    data class KeypadButton(val symbol: Char, val position: Point)

    class Keypad(val layout: List<List<Char?>>) {

        private val positions = layout.mapIndexed { y, row ->
            row.mapIndexed { x, symbol ->
                if (symbol != null)
                    KeypadButton(symbol, Point(x, y))
                else
                    null
            }.filterNotNull()
        }.flatten()
        private val sequences: MutableMap<Pair<KeypadButton, KeypadButton>, List<String>> = mutableMapOf()

        init {
            for(from in positions) {
                for(to in positions) {
                    if (from == to) {
                        sequences[Pair(from, to)] = listOf("A")
                        continue
                    }

                    val possibilities = mutableListOf<String>()
                    val queue = LinkedList<Pair<Point, String>>()
                    var optimalLength = Int.MAX_VALUE

                    queue.add(Pair(from.position, ""))
                    queue@ while(queue.isNotEmpty())  {
                        val (currentPosition, currentSequence) = queue.poll()

                        for(dir in Direction4.entries) {
                            val nextPos = currentPosition + dir.vector
                            if (nextPos.y !in layout.indices || nextPos.x !in layout[0].indices) {
                                continue
                            }
                            if (layout[nextPos.y][nextPos.x] == null) {
                                continue
                            }

                            if (nextPos == to.position) {
                                if (optimalLength < currentSequence.length + 1) break@queue

                                optimalLength = currentSequence.length + 1
                                possibilities.add(currentSequence + dir.symbol + "A")
                            }
                            else {
                                queue.add(Pair(nextPos, currentSequence + dir.symbol))
                            }
                        }
                    }
                    sequences[Pair(from, to)] = possibilities
                }
            }
        }
    }

    data class RobotArm(val name: String, val keypad: Keypad, var initialPosition: Point) {

        var currentPosition = initialPosition

        fun move(direction: Char) {
            val (x, y) = currentPosition
            val dir = Direction4.entries.first { it.symbol == direction }

            currentPosition += dir.vector

            // Überprüfe, ob die neue Position gültig ist
            if (
                currentPosition.first !in keypad.layout.indices
                || currentPosition.second !in keypad.layout[0].indices
                || keypad.layout[currentPosition.first][currentPosition.second] == null
            ) {
                throw IllegalStateException("Ungültige Bewegung")
            }
        }

        fun pressButton(): Char? {
            return this.keypad.layout[this.currentPosition.x][this.currentPosition.y]
        }
    }

    private val numericKeypad = Keypad(
        listOf(
            listOf('7', '8', '9'),
            listOf('4', '5', '6'),
            listOf('1', '2', '3'),
            listOf(null, '0', 'A')
        )
    )

    private val directionalKeypad = Keypad(
        listOf(
            listOf(null, '^', 'A'),
            listOf('<', 'v', '>')
        )
    )

    private val robotArm1 = RobotArm("Robot1", numericKeypad, Point(3, 2))
    private val robotArm2 = RobotArm("Robot2", directionalKeypad, Point(0, 2))
    private val robotArm3 = RobotArm("Robot3", directionalKeypad, Point(0, 2))

    private fun generateCode(commands: String, robotArm: RobotArm = robotArm1): String {
        var code = ""
        for (command in commands) {
            when (command) {
                '^', 'v', '<', '>' -> robotArm.move(command)
                'A' -> code += robotArm.pressButton()
            }
        }
        return code
    }

    private fun findShortestSequences(code: String, keypad: Keypad): List<String> {


        return listOf()
    }


    private fun findShortestSequence(code: String): String {
        val queue = LinkedList<Pair<String, RobotArm>>()
        val visited = mutableSetOf<Pair<Point, String>>()

        // start with robotArm3
        queue.add(Pair("", robotArm3))
        visited.add(Pair(robotArm3.currentPosition, ""))

        while (queue.isNotEmpty()) {
            val (currentCommands, currentArm) = queue.poll()
            log {
                "Checking sequence $currentCommands with robotArm $currentArm"
            }

            // Simuliere Roboterarm 3 auf dem Richtungspad
            val commandsForRobotArm2 = generateCode(currentCommands, currentArm)
            log {
                "Commands for robotArm2: $commandsForRobotArm2"
            }

            // Simuliere Roboterarm 2 auf dem Richtungspad, um die Befehle für Roboterarm 1 zu erhalten
            val commandsForRobotArm1 = generateCode(commandsForRobotArm2, robotArm2)
            log {
                "Commands for robotArm1: $commandsForRobotArm1"
            }

            // Simuliere Roboterarm 1 auf dem numerischen Keypad, um den finalen Code zu erhalten
            val currentCode = generateCode(commandsForRobotArm1, robotArm1)
            log {
                "Generated code: $currentCode"
            }

            if (currentCode == code) {
                return currentCommands
            }

            for (direction in listOf('^', 'v', '<', '>')) {
                try {
                    val newArm = currentArm.copy()
                    newArm.move(direction)

                    val newCommands = currentCommands + direction

                    if (Pair(newArm.currentPosition, newCommands) !in visited) {
                        queue.add(Pair(newCommands, newArm))
                        visited.add(Pair(newArm.currentPosition, newCommands))
                    }
                } catch (e: IllegalStateException) {
                    // Ignoriere ungültige Bewegungen
                }
            }

            val newCommands = currentCommands + 'A'
            if (Pair(currentArm.currentPosition, newCommands) !in visited) {
                queue.add(Pair(newCommands, currentArm))
                visited.add(Pair(currentArm.currentPosition, newCommands))
            }
        }

        return "" // Keine Lösung gefunden
    }

    private fun calculateComplexity(code: String, commands: String): Int {
        val numericValue = code.filter { it.isDigit() }.toIntOrNull() ?: 0
        return commands.length * numericValue
    }

    override fun part1(): Int {
        print(numericKeypad)



        return inputAsList.sumOf { code ->
            log {
                "Calculating complexity for code $code"
            }
            val shortestSequence = findShortestSequence(code)

            calculateComplexity(code, shortestSequence)
        }
//        for (code in codes) {
//            val sequence = findShortestSequence(State(yourKeypad, Point(2, 2)), code)
//            val robotSequence = findShortestSequence(State(robotKeypad, Point(0, 2)), sequence)
//            val secondRobotSequence = findShortestSequence(State(robotKeypad, Point(0, 2)), robotSequence)
//            val finalSequence = findShortestSequence(State(numericKeypad, Point(3, 2)), secondRobotSequence)
//            val complexity = finalSequence.length * "\\d+".toRegex().findAll(code).first().value.toInt()
//            totalComplexity += complexity
//            println("$code: $finalSequence (Complexity: $complexity)")
//        }
//
//        return totalComplexity
    }

    override fun part2(): Long {
        return 0
    }

}

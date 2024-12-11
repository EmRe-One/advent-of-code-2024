package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day10 : Day(
    10,
    2024,
    "Hoof It",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val input = inputAsGrid.map { row -> row.map { cell -> cell.digitToInt() } }
        return calculateTotalScore(input)
    }

    override fun part2(): Long {
        return 0
    }

    private fun findHikingTrails(map: List<MutableList<Int>>, startRow: Int, startCol: Int): List<List<Pair<Int, Int>>> {
        val trails = mutableListOf<List<Pair<Int, Int>>>()
        val currentTrail = mutableListOf<Pair<Int, Int>>()

        fun explore(row: Int, col: Int) {
            // Überprüfen, ob die Position gültig ist
            if (row < 0 || row >= map.size || col < 0 || col >= map[0].size || map[row][col] == -1) {
                return
            }

            // Aktuelle Position zum Weg hinzufügen
            currentTrail.add(Pair(row, col))

            // Überprüfen, ob das Ziel erreicht ist
            if (map[row][col] == 9) {
                trails.add(currentTrail.toList())
                currentTrail.removeLast()
                return
            }

            // Markiere die aktuelle Position als besucht
            val currentHeight = map[row][col]
            map[row][col] = -1

            // Erkunde die Nachbarpositionen
            explore(row + 1, col) // Nach unten
            explore(row - 1, col) // Nach oben
            explore(row, col + 1) // Nach rechts
            explore(row, col - 1) // Nach links

            // Stelle den ursprünglichen Höhenwert wieder her
            map[row][col] = currentHeight

            // Entferne die aktuelle Position vom Weg
            currentTrail.removeLast()
        }

        explore(startRow, startCol)
        return trails
    }

    private fun calculateTotalScore(map: List<List<Int>>): Int {
        var totalScore = 0
        for (row in map.indices) {
            for (col in map[0].indices) {
                if (map[row][col] == 0) {
                    val trails = findHikingTrails(map.map { it.toMutableList() }, row, col) // Kopie der Karte erstellen
                    totalScore += trails.size
                }
            }
        }
        return totalScore
    }
}

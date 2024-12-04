package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day
import tr.emreone.kotlin_utils.math.Direction8
import tr.emreone.kotlin_utils.math.Point
import tr.emreone.kotlin_utils.math.neighbor
import tr.emreone.kotlin_utils.math.x
import tr.emreone.kotlin_utils.math.y

class Day04 : Day(
    4,
    2024,
    "",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Int {
        val words = mutableListOf<String>()
        inputAsGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                words.addAll(getAllWordsFromCell(inputAsGrid, x, y).filter { it == "XMAS" })
            }
        }

        return words.size
    }

    override fun part2(): Int {
        val coords = mutableListOf<Point>()
        inputAsGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cell ->
                val words = getXWordsForCell(inputAsGrid, x, y).filter { it == "MAS" }
                if (words.size == 2) {
                    coords.add(Point(x, y))
                }
            }
        }

        return coords.size
    }

    private fun getWordFromCellToDirection(grid: List<List<Char>>, x: Int, y: Int, length: Int, dir: Direction8): String {
        val word = StringBuilder()
        val p = Point(x, y)

        (0 until length)
            .forEach { i ->
                val t = p.neighbor(dir, i)
                word.append(grid.getOrNull(t.second)?.getOrNull(t.first) ?: '-')
            }

        return word.toString()
    }

    private fun getAllWordsFromCell(grid: List<List<Char>>, x: Int, y: Int): List<String> {
        val words = mutableListOf<String>()

        Direction8.values.forEach { dir ->
            words.add(getWordFromCellToDirection(grid, x, y, 4, dir))
        }

        return words
    }

    private fun getXWordsForCell(grid: List<List<Char>>, x: Int, y: Int): List<String> {
        if (grid.getOrNull(y)?.getOrNull(x) != 'A') return emptyList()

        val words = mutableListOf<String>()

        val pTopLeft = Point(x - 1, y - 1)
        val pBottomLeft = Point(x - 1, y + 1)

        var word = this.getWordFromCellToDirection(grid, pTopLeft.x, pTopLeft.y, 3, Direction8.SOUTHEAST)
        words.add(word)
        words.add(word.reversed())
        word = this.getWordFromCellToDirection(grid, pBottomLeft.x, pBottomLeft.y, 3, Direction8.NORTHEAST)
        words.add(word)
        words.add(word.reversed())

        return words
    }

}

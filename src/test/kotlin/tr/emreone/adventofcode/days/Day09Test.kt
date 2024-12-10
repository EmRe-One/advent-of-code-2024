package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day09>(false) {
        Resources.resourceAsList("day09_example.txt")
            .joinToString("\n") part1 1928 part2 0
    }

}

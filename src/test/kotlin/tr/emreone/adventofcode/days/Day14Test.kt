package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day14>(true) {
        Resources.resourceAsList("day14_example.txt")
            .joinToString("\n") part1 12 part2 0
    }

}

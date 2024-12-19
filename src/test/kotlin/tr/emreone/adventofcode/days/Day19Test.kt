package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day19>(false) {
        Resources.resourceAsList("day19_example.txt")
            .joinToString("\n") part1 6 part2 16
    }

}

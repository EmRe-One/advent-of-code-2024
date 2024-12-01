package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day$1>(false) {
        Resources.resourceAsList("day$1_example.txt")
            .joinToString("\n") part1 0 part2 0
    }

}

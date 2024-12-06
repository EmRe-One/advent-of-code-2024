package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day06>(false) {
        Resources.resourceAsList("day06_example.txt")
            .joinToString("\n") part1 41 part2 0
    }

}

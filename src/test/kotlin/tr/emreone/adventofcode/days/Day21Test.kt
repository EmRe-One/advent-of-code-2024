package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day21>(false) {
        Resources.resourceAsList("day21_example.txt")
            .joinToString("\n") part1 126_384 part2 154_115_708_116_294L
    }

}

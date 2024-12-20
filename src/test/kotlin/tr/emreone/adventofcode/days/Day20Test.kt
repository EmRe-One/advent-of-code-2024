package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day20>(false) {
        Resources.resourceAsList("day20_example.txt")
            .joinToString("\n") part1 10 part2 0
    }

}

package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day23>(false) {
        Resources.resourceAsList("day23_example.txt")
            .joinToString("\n") part1 7 part2 "co,de,ka,ta"
    }

}

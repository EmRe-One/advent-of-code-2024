package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.solve

fun main() {

    solve<Day17>(false) {
        Resources.resourceAsList("day17_example_1.txt")
            .joinToString("\n") part1 "4,6,3,5,6,3,5,2,1,0"
    }

    println("======================== PART 2 ===============================")

    solve<Day17>(false) {
        Resources.resourceAsList("day17_example_2.txt")
            .joinToString("\n") part2 117_440
    }

}

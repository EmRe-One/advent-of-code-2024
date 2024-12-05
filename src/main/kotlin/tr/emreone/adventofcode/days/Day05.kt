package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day05 : Day(
    5,
    2024,
    "Print Queue",
    session = Resources.resourceAsString("session.cookie")
) {

    // Adjust inputs according to my needs

    // first group is rules
    private val pageOrderRules = inputAsGroups[0]
        .map { rule -> rule.split("|").map { it.toInt() } }
        .groupBy { it[0] }
        .mapValues { it.value.flatten().filter { i -> i != it.key } }

    // second group is pages to produce
    private val pagesToProduce = inputAsGroups[1]
        .map { page -> page.split(",").map { it.toInt() } }

    /*
     * Part 1
     */
    override fun part1(): Int {
        val correctPrints = pagesToProduce.filter {
            this.isCorrectPrint(it)
        }

        return correctPrints.sumOf { it[it.size / 2] }
    }

    /*
     * Part 2
     */
    override fun part2(): Int {
        val incorrectPrints = pagesToProduce.filter {
            !this.isCorrectPrint(it)
        }

        val adjustedPrints = incorrectPrints.map {
            this.fixPrint(it)
        }

        return adjustedPrints.sumOf { it[it.size / 2] }
    }

    private fun isCorrectPrint(pages: List<Int>): Boolean {
        return pages.windowed(2).all {
            val currentPage = it[0]
            val nextPage = it[1]

            val rules = this.pageOrderRules[currentPage] ?: return false

            val rule = rules.firstOrNull { r -> r == nextPage } ?: return false

            true
        }
    }

    private fun fixPrint(incorrectPrint: List<Int>): List<Int> {
        val fixedPrintOrder = mutableListOf<Int>()

        incorrectPrint.forEach { incorrectPage ->
            if (fixedPrintOrder.isEmpty()) {
                fixedPrintOrder.add(incorrectPage)
            } else {
                // if pageOrder is (it, incorrectPage)
                fixedPrintOrder.findLast {
                    val rules = this.pageOrderRules[it] ?: return@findLast false
                    rules.contains(incorrectPage)
                }?.let {
                    val index = fixedPrintOrder.indexOf(it)
                    fixedPrintOrder.add(index + 1, incorrectPage)
                    return@forEach
                }

                // if pageOrder is (incorrectPage, it)
                fixedPrintOrder.find {
                    val rules = this.pageOrderRules[incorrectPage] ?: return@find false
                    rules.contains(it)
                }?.let {
                    val index = fixedPrintOrder.indexOf(it)
                    fixedPrintOrder.add(index, incorrectPage)
                    return@forEach
                }
            }
        }

        return fixedPrintOrder
    }

}

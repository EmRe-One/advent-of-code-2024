package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day23 : Day(
    23,
    2024,
    "LAN Party",
    session = Resources.resourceAsString("session.cookie")
) {

    private val network = mutableMapOf<String, MutableSet<String>>()

    init {
        for (connection in inputAsList) {
            val (a, b) = connection.split("-")
            network.computeIfAbsent(a) { mutableSetOf() }.add(b)
            network.computeIfAbsent(b) { mutableSetOf() }.add(a)
        }
    }

    override fun part1(): Int {

        val triplets = mutableSetOf<Set<String>>()

        for ((computer, neighbors) in network) {
            // Check each pair of neighbors for a connection
            val neighborList = neighbors.toList()
            for (i in neighborList.indices) {
                for (j in i + 1 until neighborList.size) {
                    val a = neighborList[i]
                    val b = neighborList[j]
                    if (network[a]?.contains(b) == true) {
                        triplets.add(setOf(computer, a, b))
                    }
                }
            }
        }

        return triplets.count { triplet ->
            triplet.any { it.startsWith('t') }
        }
    }

    override fun part2(): String {

        // Graph-algorithm to find the largest clique in the network
        fun bronKerbosch(
            r: Set<String>, p: Set<String>, x: MutableSet<String>, cliques: MutableList<Set<String>>
        ) {
            if (p.isEmpty() && x.isEmpty()) {
                cliques.add(r)
                return
            }
            val pCopy = p.toMutableSet()
            for (v in p) {
                bronKerbosch(
                    r + v,
                    pCopy.intersect(network[v] ?: emptySet()),
                    x.intersect(network[v] ?: emptySet()).toMutableSet(),
                    cliques
                )
                pCopy.remove(v)
                x += v
            }
        }

        val cliques = mutableListOf<Set<String>>()
        bronKerbosch(emptySet(), network.keys, mutableSetOf(), cliques)

        // Generate the password with the largest clique, sorted alphabetically
        return cliques.maxBy { it.size }.sorted().joinToString(",")
    }

}

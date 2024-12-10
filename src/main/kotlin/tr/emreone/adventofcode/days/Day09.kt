package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day09 : Day(
    9,
    2024,
    "Disk Fragmenter",
    session = Resources.resourceAsString("session.cookie")
) {

    override fun part1(): Long {
        // Step 1: Parse the disk map into file positions and free spaces
        val (filePositions, freeSpaces) = parseDiskMap(inputAsString)
        println(filePositions)
        println(freeSpaces)

        // Step 2: Simulate the compacting process
        val compactedPositions = compactFiles(filePositions, freeSpaces.map { it.second }.toMutableList())

        // Step 3: Calculate the checksum
        return calculateChecksum(compactedPositions)
    }

    override fun part2(): Long {
        return 0
    }

    private fun parseDiskMap(diskMap: String): Pair<MutableList<Pair<Int, Int>>, MutableList<Pair<Int, Int>>> {
        val filePositions = mutableListOf<Pair<Int, Int>>() // Pair(start index, length)
        val freeSpaces = mutableListOf<Pair<Int, Int>>() // List of indices of free spaces

        var currentIndex = 0
        var isFile = true
        var fileId = 0

        for (i in diskMap.indices) {
            val length = diskMap[i].digitToInt()
            if (isFile) {
                filePositions.add(Pair(currentIndex, length))
                fileId++
            } else {
                freeSpaces.add(Pair(currentIndex, length))
            }
            currentIndex += length
            isFile = !isFile
        }
        return Pair(filePositions, freeSpaces)
    }

    // Simulate compacting files into the leftmost free spaces
    private fun compactFiles(
        filePositions: MutableList<Pair<Int, Int>>,
        freeSpaces: MutableList<Int>
    ): List<Pair<Int, Int>> {
        val freeSpaceIterator = freeSpaces.iterator()
        val compactedPositions = mutableListOf<Pair<Int, Int>>()

        for ((start, length) in filePositions) {
            var compactStart = start
            while (freeSpaceIterator.hasNext() && freeSpaceIterator.next() < start) {
                compactStart--
            }
            compactedPositions.add(Pair(compactStart, length))
        }
        return compactedPositions
    }

    // Calculate the checksum
    private fun calculateChecksum(filePositions: List<Pair<Int, Int>>): Long {
        var checksum = 0L
        for ((fileId, position) in filePositions.withIndex()) {
            val (start, length) = position
            for (offset in 0 until length) {
                checksum += (start + offset) * fileId
            }
        }
        return checksum
    }
}

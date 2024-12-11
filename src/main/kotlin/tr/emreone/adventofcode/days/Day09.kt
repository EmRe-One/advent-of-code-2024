package tr.emreone.adventofcode.days

import tr.emreone.kotlin_utils.Resources
import tr.emreone.kotlin_utils.automation.Day

class Day09 : Day(
    9,
    2024,
    "Disk Fragmenter",
    session = Resources.resourceAsString("session.cookie")
) {

    data class File(val id: Int, var size: Int)

    override fun part1(): Long {
        val (disk, files) = parseDiskMap(inputAsString)

        val compactedDisk = compactDisk(disk, files)

        return calculateChecksum(compactedDisk)
    }

    override fun part2(): Long {
        val (disk, files) = parseDiskMap(inputAsString)

        val compactedDisk = compactDisk2(disk.toMutableList(), files)

        return calculateChecksum(compactedDisk)
    }

    private fun parseDiskMap(input: String): Pair<MutableList<Any>, MutableList<File>> {
        val disk = mutableListOf<Any>()
        val files = mutableListOf<File>()
        var fileId = 0
        var isFile = true
        var i = 0
        while (i < input.length) {
            if (isFile) {
                val fileSize = input[i].digitToInt()
                repeat(fileSize) { disk.add(fileId) }
                files.add(File(fileId++, fileSize))
            } else {
                val freeSpace = input[i].digitToInt()
                repeat(freeSpace) { disk.add(".") }
            }

            i++
            isFile = !isFile
        }
        return Pair(disk, files)
    }

    private fun compactDisk(disk: MutableList<Any>, files: MutableList<File>): MutableList<Any> {
        var freeSpaceIndex = disk.indexOfFirst { it == "." }
        var currentFileIndex = disk.lastIndex

        while (freeSpaceIndex != -1 && currentFileIndex > freeSpaceIndex) {
            if (disk[currentFileIndex] != ".") {
                disk[freeSpaceIndex] = disk[currentFileIndex]
                disk[currentFileIndex] = "."
                freeSpaceIndex = disk.indexOfFirst { it == "." }
            }
            currentFileIndex--
        }
        return disk
    }

    private fun compactDisk2(disk: MutableList<Any>, files: List<File>): List<Any> {
        for (fileId in files.indices.reversed()) {
            val file = files[fileId]
            val fileSize = file.size
            val startIndex = disk.indexOf(fileId)
            val endIndex = startIndex + fileSize - 1

            // Finde den am weitesten links liegenden freien Speicherplatz, der groß genug ist
            var bestFreeSpaceStart = -1
            var freeSpaceStart = -1
            var freeSpaceCount = 0
            for (i in 0 until startIndex) {
                if (disk[i] == ".") {
                    if (freeSpaceCount == 0) {
                        freeSpaceStart = i
                    }
                    freeSpaceCount++
                    if (freeSpaceCount >= fileSize && (bestFreeSpaceStart == -1 || freeSpaceStart < bestFreeSpaceStart)) {
                        bestFreeSpaceStart = freeSpaceStart
                    }
                } else {
                    freeSpaceCount = 0
                }
            }

            // Verschiebe die Datei, falls genügend freier Speicherplatz gefunden wurde
            if (bestFreeSpaceStart != -1) {
                for (i in endIndex downTo startIndex) {
                    disk[bestFreeSpaceStart + endIndex - i] = disk[i]
                    disk[i] = "."
                }
            }
        }
        return disk
    }

    // Calculate the checksum
    private fun calculateChecksum(disk: List<Any>): Long {
        var checksum = 0L
        for (i in disk.indices) {
            if (disk[i] is Int) {
                checksum += i * (disk[i] as Int).toLong()
            }
        }
        return checksum
    }
}

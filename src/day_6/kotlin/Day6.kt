// AOC Day 6

fun findMarker(dataStream: String, markerSize: Int): Int? {
    for (headPosition in markerSize until dataStream.length) {
        /** [String.substring] start is inclusive, end exclusive */
        val tailPositionIndex = headPosition - markerSize
        if (!dataStream.substring(tailPositionIndex, headPosition).containsDuplicates()) {
            return headPosition
        }
    }

    return null
}

fun part1(dataStream: String) {
    val firstPacketPosition = findMarker(dataStream, 4)!!

    println("Part 1: The first packet starts at position $firstPacketPosition")
}

fun part2(dataStream: String) {
    val firstMessagePacketPosition = findMarker(dataStream, 14)!!

    println("Part 2: The first message packet starts at position $firstMessagePacketPosition")
}

fun main() {
    val dataStream = getAOCInput { it.trim() }

    part1(dataStream)
    part2(dataStream)
}

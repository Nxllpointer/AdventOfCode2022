// AOC Day 4

fun part1(pairs: List<Pair<IntRange, IntRange>>) {
    val containingCount = pairs.count { it.first.containsAll(it.second) || it.second.containsAll(it.first) }

    println("Part 1: In $containingCount assignment pairs one fully contains the others assignment")
}

fun part2(pairs: List<Pair<IntRange, IntRange>>) {
    val overlappingCount = pairs.count { it.first.containsAny(it.second) || it.second.containsAny(it.first) }

    println("Part 1: In $overlappingCount assignment pairs the assignments are overlapping")
}

fun main() {
    val pairs = getAOCInput { rawInput: String ->
        rawInput
            .trim()
            .split("\n")
            .map { it.split(',', '-').mapToInt() }
            .map { IntRange(it[0], it[1]) to IntRange(it[2], it[3]) }
    }

    part1(pairs)
    part2(pairs)
}

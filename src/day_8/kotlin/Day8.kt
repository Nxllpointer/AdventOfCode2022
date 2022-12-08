// AOC Day 8

fun Array<Array<Int>>.isTreeVisible(treePos: Pair<Int, Int>): Boolean {
    val maxTreeIndex = this.lastIndex
    val treeHeight = this[treePos.first][treePos.second]

    return ((0 until treePos.first).all { vIndex -> this[vIndex][treePos.second] < treeHeight } ||
            (maxTreeIndex downUntil treePos.first).all { vIndex -> this[vIndex][treePos.second] < treeHeight } ||
            (0 until treePos.second).all { hIndex -> this[treePos.first][hIndex] < treeHeight } ||
            (maxTreeIndex downUntil treePos.second).all { hIndex -> this[treePos.first][hIndex] < treeHeight })
}

fun Array<Array<Int>>.getColnumSection(colnum: Int, from: Int, to: Int): List<Int> =
    (from..to).filter { it in 0..this.lastIndex }.map { this[it][colnum] }

fun Array<Array<Int>>.getRowSection(row: Int, from: Int, to: Int): List<Int> =
    (from..to).filter { it in 0..this.lastIndex }.map { this[row][it] }

fun Array<Array<Int>>.getScenicScore(treePos: Pair<Int, Int>): Int {
    val maxTreeIndex = this.lastIndex
    val treeHeight = this[treePos.first][treePos.second]

    fun List<Int>.getDistance() = slice(0..indexOfFirstOr(lastIndex) { it >= treeHeight }).size

    val distanceUp = this.getColnumSection(treePos.second, 0, treePos.first - 1).reversed().getDistance()
    val distanceDown = this.getColnumSection(treePos.second, treePos.first + 1, maxTreeIndex).getDistance()
    val distanceLeft = this.getRowSection(treePos.first, 0, treePos.second - 1).reversed().getDistance()
    val distanceRight = this.getRowSection(treePos.first, treePos.second + 1, maxTreeIndex).getDistance()

    return distanceUp * distanceDown * distanceLeft * distanceRight
}

fun part1(trees: Array<Array<Int>>) {
    val maxTreeIndex = trees.lastIndex

    var visibleTreeCount = 0
    for (vIndex in 0..maxTreeIndex) {
        for (hIndex in 0..maxTreeIndex) {
            if (trees.isTreeVisible(vIndex to hIndex)) visibleTreeCount++
        }
    }

    println("Part 1: $visibleTreeCount trees are visible from the outside")
}

fun part2(trees: Array<Array<Int>>) {
    val maxTreeIndex = trees.lastIndex

    val scenicScores = mutableListOf<Int>()
    for (vIndex in 0..maxTreeIndex) {
        for (hIndex in 0..maxTreeIndex) {
            scenicScores += trees.getScenicScore(vIndex to hIndex)
        }
    }

    val maxScenicScore = scenicScores.max()

    println("Part 2: The highest scenic score is $maxScenicScore")
}

fun main() {
    val trees = getAOCInput { rawInput ->
        rawInput.trim().split("\n")
            .map { row ->
                row
                    .map { colnum -> colnum.digitToInt() }
                    .toTypedArray()
            }.toTypedArray()
    }

    part1(trees)
    part2(trees)
}

// AOC Day 12

operator fun Array<IntArray>.get(position: Vector2D) = this[position.y][position.x]
operator fun Array<IntArray>.set(position: Vector2D, value: Int) = this[position.y].set(position.x, value)
val Array<IntArray>.verticalSize get() = this.size
val Array<IntArray>.horizontalSize get() = this.first().size

fun generateDistanceMap(heightMap: Array<IntArray>, startPosition: Vector2D): Array<IntArray> {
    val distanceMap = Array(heightMap.size) { IntArray(heightMap.first().size) { -1 } }

    fun Vector2D.getAdjacentUnsetPositions(): Set<Vector2D> =
        listOf(Vector2D(x, y + 1), Vector2D(x + 1, y), Vector2D(x, y - 1), Vector2D(x - 1, y))
            .filter {
                val isInMap = it.x in 0 until heightMap.horizontalSize && it.y in 0 until heightMap.verticalSize
                val isUnset by lazy { distanceMap[it] == -1 } // ----- Lazy to avoid index out of bounds
                val isElevationChangeAllowed by lazy { heightMap[it] >= heightMap[this] - 1 } // -------

                isInMap && isUnset && isElevationChangeAllowed
            }.toSet()


    distanceMap[startPosition] = 0
    var adjacentPositions = startPosition.getAdjacentUnsetPositions()
    var adjacentDistance = 1

    while (adjacentPositions.isNotEmpty()) {
        adjacentPositions = adjacentPositions.flatMap { adjacentPosition ->
            distanceMap[adjacentPosition] = adjacentDistance
            adjacentPosition.getAdjacentUnsetPositions()
        }.toSet()
        adjacentDistance++
    }

    return distanceMap
}

fun part1and2(heightMap: Array<IntArray>, startPosition: Vector2D, destinationPosition: Vector2D) {
    val distanceMap = generateDistanceMap(heightMap, destinationPosition)

    val lowestPositionsSteps = mutableListOf<Int>()
    for (rowIndex in 0 until heightMap.verticalSize) {
        for (colnumIndex in 0 until heightMap.horizontalSize) {
            val position = Vector2D(colnumIndex, rowIndex)
            val height = heightMap[position]
            val distance = distanceMap[position]

            if (height == 0 && distance > 0) {
                lowestPositionsSteps += distance
            }
        }
    }

    val destinationStepsStartPosition = distanceMap[startPosition]
    val destinationStepsNearestLowestPosition = lowestPositionsSteps.min()

    println("Part 1: You need to walk $destinationStepsStartPosition from the start position to reach the destination")
    println("Part 2: You need to walk $destinationStepsNearestLowestPosition from the nearest lowest position to reach the destination")
}

fun Char.letterToHeight() = "abcdefghijklmnopqrstuvwxyz".indexOf(this)

fun main() {
    val (heightMap, startPosition, destinationPosition) = getAOCInput { rawInput ->
        lateinit var startPosition: Vector2D
        lateinit var destinationPosition: Vector2D

        val heightMap = rawInput.trim().lines().mapIndexed { rowIndex, row ->
            row.mapIndexed { colnumIndex, letter ->
                when (letter) {
                    'S' -> {
                        startPosition = Vector2D(colnumIndex, rowIndex)
                        'a'
                    }

                    'E' -> {
                        destinationPosition = Vector2D(colnumIndex, rowIndex)
                        'z'
                    }

                    else -> letter
                }.letterToHeight()
            }.toIntArray()
        }.toTypedArray()

        Triple(heightMap, startPosition, destinationPosition)
    }

    part1and2(heightMap, startPosition, destinationPosition)
}

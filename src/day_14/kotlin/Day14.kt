import kotlin.math.sign

// AOC Day 14

const val rockCharacter = '#'
const val sandCharacter = '.'
const val airCharacter = ' '
const val printCave = false

typealias Cave = Array<CharArray>

fun Cave.print() = forEach { println(it.joinToString("  ")) }.also { println() }
operator fun Cave.get(point: Vector2D) = this[point.y][point.x]
operator fun Cave.set(point: Vector2D, char: Char) {
    this[point.y][point.x] = char
}

val sandMovesPriorityOrdered = listOf(Vector2D(0, 1), Vector2D(-1, 1), Vector2D(1, 1))
fun Cave.simulateFallingSand(startPosition: Vector2D): Vector2D {
    var currentPosition = startPosition
    var isResting = false

    while (!isResting) {
        sandMovesPriorityOrdered
            .map { currentPosition + it }
            .filter { it.y in 0..this.lastIndex && it.x in 0..this[0].lastIndex }
            .filter { this[it] == airCharacter }
            .apply {
                if (none()) isResting = true
                else currentPosition = first()
            }
    }

    return currentPosition
}

fun part1(cave: Cave, sandSourcePoint: Vector2D) {
    var sandUnits = 0
    while (true) {
        val sandRestingPoint = cave.simulateFallingSand(sandSourcePoint)
        if (sandRestingPoint.y == cave.lastIndex - 1) break
        sandUnits++
        cave[sandRestingPoint] = sandCharacter
    }

    if (printCave) cave.print()
    println("Part 1: The first unit of sand falls into the abyss after $sandUnits sand units have come to rest")
}

fun part2(cave: Cave, sandSourcePoint: Vector2D) {
    var sandUnits = 0
    while (true) {
        val sandRestingPoint = cave.simulateFallingSand(sandSourcePoint)
        sandUnits++
        cave[sandRestingPoint] = sandCharacter
        if (sandRestingPoint == sandSourcePoint) break
    }

    if (printCave) cave.print()
    println("Part 2: $sandUnits sand untis have to come to rest for the source to be blocked")
}

fun String.toPoint() = this.split(",").mapToInt().run { Vector2D(first(), last()) }
fun Cave.deepCopy(): Cave = map { it.toList().toCharArray() }.toTypedArray()
val pointRegex = "\\d+,\\d+".toRegex()

fun main() {
    val (cave, sandSourcePoint) = getAOCInput { rawInput ->
        val points = pointRegex.findAll(rawInput).map { it.value.toPoint() }

        var minX = points.minOf { it.x }
        var maxX = points.maxOf { it.x }
        val minY = 0 // Sand level
        var maxY = points.maxOf { it.y }


        val floorHeight = maxY + 2
        maxY = floorHeight
        minX -= floorHeight
        maxX += floorHeight

        fun Vector2D.asNormalized() = Vector2D(x - minX, y - minY)

        val cave: Cave = Array(maxY - minY + 1) { CharArray(maxX - minX + 1) { airCharacter } }

        val sandSourcePoint = Vector2D(500, 0).asNormalized()
        cave[sandSourcePoint] = 'S'

        val floorPath = Vector2D(minX, floorHeight).asNormalized() to Vector2D(maxX, floorHeight).asNormalized()

        val paths = rawInput.trim().lines()
            .flatMap { line ->
                line
                    .split(" -> ")
                    .map { it.toPoint().asNormalized() }
                    .zipWithNext()
            }
            .plus(floorPath)

        paths.forEach { (start, end) ->
            var currentPos = start

            val signX = (end.x - start.x).sign
            val signY = (end.y - start.y).sign

            while (currentPos.x != end.x) {
                cave[currentPos] = rockCharacter
                currentPos += Vector2D(signX, 0)
                cave[currentPos] = rockCharacter
            }

            while (currentPos.y != end.y) {
                cave[currentPos] = rockCharacter
                currentPos += Vector2D(0, signY)
                cave[currentPos] = rockCharacter
            }
        }

        cave to sandSourcePoint
    }

    part1(cave.deepCopy(), sandSourcePoint)
    part2(cave.deepCopy(), sandSourcePoint)
}

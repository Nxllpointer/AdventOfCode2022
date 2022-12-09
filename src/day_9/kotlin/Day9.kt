import kotlin.math.absoluteValue
import kotlin.math.sign

// AOC Day 9

// Simulating the tail movement is done by storing its relative position to the head.
// This enables not having to simulate the head itself and is easier to work with.
// So if we want to move the head we actually have to move the tails relative position opposite of the heads direction.
class Tail {

    private var position = Vector2D(x = 0, y = 0)
    private var positionRelative = Vector2D(x = 0, y = 0)
    val uniquePositions = mutableSetOf<Vector2D>()

    fun moveHead(vector: Vector2D): Vector2D {
        positionRelative += !vector
        return correct()
    }

    private val isTailTouching get() = positionRelative.x.absoluteValue <= 1 && positionRelative.y.absoluteValue <= 1
    private val isTailSameRow get() = positionRelative.y == 0
    private val isTailSameColnum get() = positionRelative.x == 0

    private fun correct(): Vector2D {
        if (isTailTouching) return Vector2D(x = 0, y = 0)

        val correctionVector = if (isTailSameRow) {
            when (positionRelative.x.sign) {
                +1 -> Vector2D(x = -1, y = 0)
                -1 -> Vector2D(x = +1, y = 0)
                else -> throw AssertionError("Tail relative x position sign was neither 1 nor -1")
            }
        } else if (isTailSameColnum) {
            when (positionRelative.y.sign) {
                +1 -> Vector2D(x = 0, y = -1)
                -1 -> Vector2D(x = 0, y = +1)
                else -> throw AssertionError("Tail relative y position sign was neither 1 nor -1")
            }
        } else {
            Vector2D(x = -positionRelative.x.sign, y = -positionRelative.y.sign)
        }

        return move(correctionVector)
    }

    private fun move(vector: Vector2D): Vector2D {
        uniquePositions += position

        position += vector
        positionRelative += vector

        uniquePositions += position

        return vector
    }

}

fun part1(motions: List<Vector2D>) {
    val tail = Tail()

    motions.forEach { tail.moveHead(it) }

    val uniquePositionCount = tail.uniquePositions.size

    println("Part 1: The rope tail visited $uniquePositionCount unique positions")
}

fun part2(motions: List<Vector2D>) {
    val tails = List(size = 9) { Tail() }

    motions.forEach { motion ->
        var lastMoveVector: Vector2D = motion
        tails.forEach { tail -> lastMoveVector = tail.moveHead(!lastMoveVector) }
    }

    val lastTailUniquePositionCount = tails.last().uniquePositions.size

    println("Part 2: The rope tail visited $lastTailUniquePositionCount unique positions")
}

fun main() {
    val motions = getAOCInput { rawInput ->
        rawInput.trim().lines().flatMap { line ->
            val (directionRaw, timesRaw) = line.splitInTwo(" ")

            val times = timesRaw.toInt()
            val motion = when (directionRaw) {
                "U" -> Vector2D(x = 0, y = 1)
                "D" -> Vector2D(x = 0, y = -1)
                "R" -> Vector2D(x = 1, y = 0)
                "L" -> Vector2D(x = -1, y = 0)
                else -> throw IllegalArgumentException("Invalid direction")
            }

            List(size = times) { motion }
        }
    }

    part1(motions)
    part2(motions)
}

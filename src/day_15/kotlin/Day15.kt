import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

// AOC Day 15

fun Vector2D.manhattanDistanceTo(other: Vector2D) = (this - other).abs().run { x + y }

data class Sensor(val position: Vector2D, val beaconPosition: Vector2D) {
    val beaconDistance = position.manhattanDistanceTo(beaconPosition)
}

fun part1(sensors: List<Sensor>) {
    val rowToCheck = 2000000

    val beaconXPositions = sensors.filter { it.beaconPosition.y == rowToCheck }.map { it.beaconPosition.x }
    val coveringSensors = sensors.filter { sensor ->
        (rowToCheck - sensor.position.y).absoluteValue <= sensor.beaconDistance
    }

    val rowCoveredXPositions = mutableSetOf<Int>()

    coveringSensors.forEach { sensor ->
        val xDistance = sensor.beaconDistance - (sensor.position.y - rowToCheck).absoluteValue
        for (xOffset in -xDistance..+xDistance) {
            val xPosition = sensor.position.x + xOffset

            if (xPosition !in beaconXPositions) rowCoveredXPositions += xPosition
        }
    }

    println("Part 1: No beacon can be placed at ${rowCoveredXPositions.size} positions in row $rowToCheck")
}

fun part2(sensors: List<Sensor>) {
    // Since the distress beacon is the only uncovered point we can check every point next to the edges of sensor coverages
    // We can the check if any sensor coveres the point. If it is not covered we found the distress beacon.

    val minX = sensors.minOf { it.position.x }.coerceAtLeast(0)
    val maxX = sensors.maxOf { it.position.x }.coerceAtMost(4000000)
    val minY = sensors.minOf { it.position.y }.coerceAtLeast(0)
    val maxY = sensors.maxOf { it.position.y }.coerceAtMost(4000000)

    val rangeX = minX..maxX
    val rangeY = minY..maxY

    fun Vector2D.isCovered() = sensors.any { sensor ->
        sensor.position.manhattanDistanceTo(this) <= sensor.beaconDistance
    }

    sensors.forEach sensors@{ sensor ->
        val edgeDistance = sensor.beaconDistance + 1
        for (xOffset in -edgeDistance..+edgeDistance) {
            val posX = sensor.position.x + xOffset
            if (posX !in rangeX) continue

            val unsignedHOffset = edgeDistance - xOffset.absoluteValue

            listOf(+unsignedHOffset, -unsignedHOffset).forEach hOffsets@{ hOffset ->
                val posY = sensor.position.y + hOffset
                if (posY !in rangeY) return@hOffsets

                val position = sensor.position + Vector2D(xOffset, hOffset)

                if (!position.isCovered()) {
                    val tuningFrequency = position.x.toLong() * 4000000L + position.y.toLong()
                    println("Part 2: The distress tuning frequency is $tuningFrequency")
                    return
                }
            }
        }
    }
}

fun main() {
    val positionRegex = "x=(-*\\d+), y=(-*\\d+)".toRegex()
    fun MatchResult.toPosition() =
        this.groupValues.drop(1).mapToInt().run { Vector2D(first(), last()) }

    val sensors = getAOCInput { rawInput ->
        rawInput.trim().lines().map { line ->
            val positionMatches = positionRegex.findAll(line)
            val sensorPosition = positionMatches.first().toPosition()
            val beaconPosition = positionMatches.last().toPosition()

            Sensor(sensorPosition, beaconPosition)
        }
    }

    measureTimeMillis { part1(sensors) }.also { println("Part 1 took ${it / 1000f} seconds") }
    measureTimeMillis { part2(sensors) }.also { println("Part 2 took ${it / 1000f} seconds") }
}

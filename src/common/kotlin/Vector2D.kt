data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(other: Vector2D) = copy(x = x + other.x, y = y + other.y)
    operator fun not() = copy(x = -x, y = -y)
}

fun <T : Comparable<T>> ClosedRange<T>.containsAll(other: ClosedRange<T>): Boolean {
    return other.start >= this.start && other.endInclusive <= this.endInclusive
}

fun <T : Comparable<T>> ClosedRange<T>.containsAny(other: ClosedRange<T>): Boolean {
    return other.start <= this.endInclusive && other.endInclusive >= this.start
}

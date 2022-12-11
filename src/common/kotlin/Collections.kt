fun <T : Collection<String>> T.mapToInt(): List<Int> {
    return map { it.toInt() }
}

fun <T> Collection<T>.indexOfFirstOr(fallbackIndex: Int, predicate: (T) -> Boolean) =
    indexOfFirst(predicate).let { if (it >= 0) it else fallbackIndex }

fun <T: Number> Collection<T>.product() = map { it.toLong() }.reduce(Long::times)

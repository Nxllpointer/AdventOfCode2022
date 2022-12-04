fun <T : Collection<String>> T.mapToInt(): List<Int> {
    return map { it.toInt() }
}

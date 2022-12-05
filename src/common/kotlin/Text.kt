fun CharSequence.splitInTwo(delimiter: String): Pair<String, String> {
    return this.split(delimiter, limit = 2).let { it.first() to it.last() }
}

import kotlin.math.max

// AOC Day 13

fun Pair<List<*>, List<*>>.isInRightOrder(): Boolean? {
    val left = first
    val right = second

    for (listIndex in 0..max(left.lastIndex, right.lastIndex)) {
        if (listIndex > left.lastIndex) return true
        if (listIndex > right.lastIndex) return false

        val itemLeft = left[listIndex]
        val itemRight = right[listIndex]

        if (itemLeft is Int && itemRight is Int) {
            if (itemLeft < itemRight) return true
            if (itemLeft > itemRight) return false
        }

        if (itemLeft is List<*> && itemRight is List<*>)
            (itemLeft to itemRight).isInRightOrder().also {
                if (it != null) return it
            }

        if (itemLeft is Int && itemRight is List<*>) {
            val order = (listOf(itemLeft) to itemRight).isInRightOrder()
            if (order != null) return order
        }

        if (itemLeft is List<*> && itemRight is Int) {
            val order = (itemLeft to listOf(itemRight)).isInRightOrder()
            if (order != null) return order
        }
    }

    return null
}

fun compareLists(first: MutableList<*>, other: MutableList<*>) =
    when ((first to other).isInRightOrder()) {
        false -> 1
        true -> -1
        null -> throw IllegalArgumentException("Order was not able to be determined")
    }

fun part1(lists: MutableList<MutableList<Any>>) {
    val pairs = lists
        .chunked(2)
        .map { it.first() to it.last() }

    val correctlyOrderedIndicesSum = pairs
        .filter { it.isInRightOrder()!! }
        .sumOf { pairs.indexOf(it) + 1 }

    println("Part 1: The sum of indices of correctly ordered pairs is $correctlyOrderedIndicesSum")
}

fun part2(lists: MutableList<MutableList<Any>>) {
    val dividers = arrayOf(
        mutableListOf(mutableListOf(2)),
        mutableListOf(mutableListOf(6))
    )

    val decoderKey = lists
        .plus(dividers)
        .sortedWith(::compareLists)
        .let { sortedLists ->
            sortedLists
                .filter { it in dividers }
                .map { sortedLists.indexOf(it) + 1 }
                .product()
        }

    println("Part 2: The decoder key is $decoderKey")
}

fun main() {
    val lists = getAOCInput { rawInput ->
        val lists = mutableListOf<MutableList<Any>>()

        var currentCharIndex = 0
        var currentChar = rawInput[currentCharIndex]
        var nextChar = rawInput[currentCharIndex + 1]

        fun next() {
            currentCharIndex++
            currentChar = rawInput[currentCharIndex]
            nextChar = rawInput.getOrElse(currentCharIndex + 1) { '?' }
        }

        fun readInt(): Int {
            var intString = currentChar.toString()

            while (nextChar.isDigit()) {
                next()
                intString += currentChar
            }

            return intString.toInt()
        }

        fun readList(): MutableList<Any> {
            val list = mutableListOf<Any>()

            while (nextChar != ']') {
                next()
                if (currentChar.isDigit()) list.add(readInt())
                else if (currentChar == '[') list.add(readList())
            }
            next()

            return list
        }

        while (nextChar != '?') {
            if (currentChar == '[') readList().also { lists += it }
            next()
        }

        lists
    }

    part1(lists)
    part2(lists)
}

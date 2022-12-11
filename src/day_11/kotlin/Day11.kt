data class Monkey(
    val itemWrorryLevels: MutableList<Long>,
    val wrorryOperation: (Long) -> Long,
    val testDivisor: Int,
    val throwToTestSuccess: Int,
    val throwToTestFailed: Int
) {
    fun inspectItem() {
        itemWrorryLevels[0] = wrorryOperation(itemWrorryLevels[0])
    }

    fun throwItem(monkeys: List<Monkey>) {
        val testSucceeded = itemWrorryLevels[0] % testDivisor == 0L
        val throwTo = if (testSucceeded) throwToTestSuccess else throwToTestFailed
        monkeys[throwTo].itemWrorryLevels += itemWrorryLevels.removeAt(0)
    }
}

fun playKeepAway(monkeys: List<Monkey>, rounds: Int, afterInspection: Monkey.() -> Unit): Long {

    val inspectionCounts = MutableList(monkeys.size) { 0 }

    repeat(rounds) {
        monkeys.forEachIndexed { monkeyIndex, monkey ->
            while (monkey.itemWrorryLevels.isNotEmpty()) {
                monkey.inspectItem()
                inspectionCounts[monkeyIndex]++
                monkey.afterInspection()
                monkey.throwItem(monkeys)
            }
        }
    }

    return inspectionCounts.sortedDescending().take(2).product()
}

fun part1(monkeys: List<Monkey>) {
    val monkeyBusinessLevel = playKeepAway(monkeys, rounds = 20, afterInspection = {
        // Calm down
        itemWrorryLevels[0] /= 3L
    })
    println("Part 1: The level of monkey business is $monkeyBusinessLevel")
}

fun part2(monkeys: List<Monkey>) {
    // By calculating the product of all divisors we get a number that is divisible by all divisors.
    // This means that any number that is divisible by our product is a multiple of each divisor.
    // We can use this to keep the wrorry level in a computable range while also being able to get correct test results.
    val divisorsProduct = monkeys.map { it.testDivisor }.product()

    val monkeyBusinessLevel = playKeepAway(monkeys, rounds = 10000, afterInspection = {
        itemWrorryLevels[0] %= divisorsProduct
    })
    println("Part 2: The level of monkey business is $monkeyBusinessLevel")
}

fun Collection<Monkey>.deepCopy() = map { monkey ->
    monkey.copy(itemWrorryLevels = monkey.itemWrorryLevels.toMutableList())
}

fun main() {
    val monkeys = getAOCInput { rawInput ->
        rawInput.trim()
            .split("\n\n")
            .map { it.split("\n") }
            .map { monkeyLines ->
                val itemWrorryLevels = monkeyLines[1]
                    .splitInTwo(": ")
                    .second
                    .split(", ")
                    .map { it.toLong() }
                    .toMutableList()

                val operation: (Long) -> Long = monkeyLines[2]
                    .splitInTwo("= old ")
                    .second
                    .splitInTwo(" ")
                    .let { (operator, other) ->
                        { old ->
                            val number = if (other == "old") old else other.toLong()
                            when (operator) {
                                "+" -> old + number
                                "*" -> old * number
                                else -> throw IllegalArgumentException("Unknown operator $operator")
                            }
                        }
                    }

                fun String.lastInt() = split(" ").last().toInt()

                val testDivisor = monkeyLines[3].lastInt()
                val throwToTestSuccess = monkeyLines[4].lastInt()
                val throwToTestFailure = monkeyLines[5].lastInt()

                Monkey(itemWrorryLevels, operation, testDivisor, throwToTestSuccess, throwToTestFailure)
            }
    }

    part1(monkeys.deepCopy())
    part2(monkeys.deepCopy())
}

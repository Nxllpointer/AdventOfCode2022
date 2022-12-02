// AOC Day 2

enum class Move(val score: Int, defeats: () -> Move) {
    ROCK(1, { SCISSORS }),
    PAPER(2, { ROCK }),
    SCISSORS(3, { PAPER });

    val defeats by lazy { defeats() }
}

data class Round(val opponentMove: Move, val myMove: Move) {
    enum class Result(val score: Int) {
        LOOSE(0),
        WIN(6),
        DRAW(3)
    }

    val result = when {
        opponentMove.defeats == myMove -> Result.LOOSE
        myMove.defeats == opponentMove -> Result.WIN
        else -> Result.DRAW
    }
    val myScore = myMove.score + result.score
}

fun moveLetterToMove(letter: String): Move? = when (letter) {
    "A", "X" -> Move.ROCK
    "B", "Y" -> Move.PAPER
    "C", "Z" -> Move.SCISSORS
    else -> null
}

fun resultLetterToResult(letter: String): Round.Result? = when (letter) {
    "X" -> Round.Result.LOOSE
    "Y" -> Round.Result.DRAW
    "Z" -> Round.Result.WIN
    else -> null
}

fun part1(roundsRaw: List<List<String>>) {
    val rounds = roundsRaw
        .map {
            it.map { moveLetter -> moveLetterToMove(moveLetter) }
        }
        .map { Round(it[0]!!, it[1]!!) }

    val totalScore = rounds.sumOf { it.myScore }

    println("Part 1: The total score of all rounds is $totalScore")
}

fun part2(roundsRaw: List<List<String>>) {
    val rounds = roundsRaw.map {
        val opponentMove = moveLetterToMove(it[0])!!
        val neededResult = resultLetterToResult(it[1])!!

        val neededMove = when (neededResult) {
            Round.Result.LOOSE -> Move.values().first { move -> opponentMove.defeats == move }
            Round.Result.DRAW -> opponentMove
            Round.Result.WIN -> Move.values().first { move -> move.defeats == opponentMove }
        }

        Round(opponentMove, neededMove)
            .apply { check(result == neededResult) { "Needed move was not chosen correctly" } }
    }

    val totalScore = rounds.sumOf { it.myScore }

    println("Part 2: The total score of all rounds is $totalScore")
}

fun main() {
    val roundsRaw = getAOCInput { rawInput ->
        rawInput
            .trim()
            .split("\n")
            .map { it.split(" ") }
    }

    part1(roundsRaw)
    part2(roundsRaw)
}

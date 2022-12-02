// AOC Day 1

data class Elf(var totalCalories: Int)

fun part1(elves: Collection<Elf>) {
    val elfWithMostCalories =
        elves.maxByOrNull { it.totalCalories } ?: throw IllegalArgumentException("No elf provided")

    val topElfTotalCalories = elfWithMostCalories.totalCalories

    println("Part 1: The elf with the most amount of calories carrys a total of $topElfTotalCalories calories")
}

fun part2(elves: Collection<Elf>) {
    val elvesWithMostCalories = elves.sortedByDescending { it.totalCalories }.take(3)
    check(elvesWithMostCalories.size >= 3) { "Not enough elves provided" }

    val topElvesTotalCalories = elvesWithMostCalories.sumOf { it.totalCalories }

    println("Part 2: The top 3 calorie carrying elves have a total of $topElvesTotalCalories calories")
}

fun main() {
    val elves = getAOCInput { rawInput ->
        rawInput
            .trim()
            .split("\n\n")
            .map { rawElfCaloriesList ->
                val elfCalories = rawElfCaloriesList
                    .split("\n")
                    .sumOf { it.toInt() }

                Elf(elfCalories)
            }
    }

    part1(elves)
    part2(elves)
}

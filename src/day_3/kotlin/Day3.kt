// AOC Day 3

data class Rucksack(val items: String) {
    val firstCompartment = items.drop(items.length / 2)
    val secondCompartment = items.dropLast(items.length / 2)

    val misorderedItem by lazy {
        firstCompartment.first { secondCompartment.contains(it) }
    }
}

const val priorities = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
fun itemToPriority(itemType: Char): Int = priorities.indexOf(itemType) + 1


fun part1(rucksacks: List<Rucksack>) {
    val misorderedItemsPrioritySum = rucksacks.sumOf { itemToPriority(it.misorderedItem) }

    println("Part 1: The priority sum of misordered items is $misorderedItemsPrioritySum")
}

fun part2(rucksacks: List<Rucksack>) {
    val groups = rucksacks.chunked(3)

    val groupPrioritiesSum = groups
        .map { group -> // Get character contained by all rucksacks
            group[0].items.first {
                group[1].items.contains(it) && group[2].items.contains(it)
            }
        }
        .sumOf { itemToPriority(it) }

    println("Part 2: The priority sum of all groups is $groupPrioritiesSum")
}


fun main() {
    val rucksacks = getAOCInput { rawInput ->
        rawInput
            .trim()
            .split("\n")
            .map { Rucksack(it) }
    }

    part1(rucksacks)
    part2(rucksacks)
}

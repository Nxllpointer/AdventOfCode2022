// AOC Day 5

typealias Container = Char
typealias ContainerStack = ArrayDeque<Container>
typealias StackNumber = Int

data class MoveInstruction(val amount: Int, val from: Int, val to: Int)

fun Map<StackNumber, ContainerStack>.deepCopy(): Map<StackNumber, ContainerStack> {
    return toMap().mapValues { ArrayDeque(it.value) }
}


fun part1(stacks: Map<StackNumber, ContainerStack>, instructions: Collection<MoveInstruction>) {
    instructions.forEach { (amount, from, to) ->
        repeat(amount) {
            stacks[from]!!.removeFirst().also { stacks[to]!!.addFirst(it) }
        }
    }

    val topContainersText = stacks.toSortedMap().values.map { it.first() }.joinToString("")

    println("Part 1: The top most containers form the text $topContainersText")
}

fun part2(stacks: Map<StackNumber, ContainerStack>, instructions: Collection<MoveInstruction>) {
    instructions.forEach { (amount, from, to) ->
        val transferStack = ContainerStack()
        repeat(amount) {
            stacks[from]!!.removeFirst().also { transferStack.addFirst(it) }
        }
        repeat(amount) {
            transferStack.removeFirst().also { stacks[to]!!.addFirst(it) }
        }
    }

    val topContainersText = stacks.toSortedMap().values.map { it.first() }.joinToString("")

    println("Part 2: The top most containers form the text $topContainersText")
}

val INSTRUCTION_REGEX = Regex("\\d+")
fun main() {
    val (stacks, instructions) = getAOCInput { rawInput ->
        val (sectionStacks, sectionInstructions) = rawInput.trim('\n').splitInTwo("\n\n").run {
            first.split("\n") to second.split("\n")
        }

        val stacks = mutableMapOf<StackNumber, ContainerStack>()
        sectionStacks.last().forEachIndexed { charIndex, char ->
            if (char.isDigit()) {
                val stack = ContainerStack().also { stacks[char.digitToInt()] = it }
                for (layer in sectionStacks.dropLast(1).reversed()) {
                    val containerLabel = layer[charIndex]
                    if (containerLabel.isLetter()) stack.addFirst(layer[charIndex])
                }
            }
        }

        val instructions = sectionInstructions.map { rawInstruction ->
            val instructionValues = INSTRUCTION_REGEX.findAll(rawInstruction).toList().map { it.value }.mapToInt()
            MoveInstruction(instructionValues[0], instructionValues[1], instructionValues[2])
        }

        stacks to instructions
    }

    part1(stacks.deepCopy(), instructions)
    part2(stacks.deepCopy(), instructions)
}

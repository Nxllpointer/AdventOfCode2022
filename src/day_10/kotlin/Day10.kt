// AOC Day 10

interface Instruction {
    data class AddX(val amount: Int) : Instruction
    object Noop : Instruction
}

data class InstructionExecution<T : Instruction>(val instruction: T, var executionCyclesLeft: Int)

fun Iterator<Instruction>.run(maxCycles: Int, duringCycleAction: (cycle: Int, registerX: Int) -> Unit) {
    var currentInstructionExecution: InstructionExecution<*>? = null
    var registerX = 1

    fun readInstruction() {
        currentInstructionExecution = when (val instruction = this.next()) {
            is Instruction.AddX -> InstructionExecution(instruction, executionCyclesLeft = 2)
            is Instruction.Noop -> InstructionExecution(instruction, executionCyclesLeft = 1)
            else -> null
        }
    }

    fun updateCurrentInstruction() {
        currentInstructionExecution?.apply {
            executionCyclesLeft--

            if (instruction is Instruction.AddX && executionCyclesLeft == 0)
                registerX += instruction.amount

            if (executionCyclesLeft == 0)
                currentInstructionExecution = null
        }
    }

    for (cycle in 1..maxCycles) {
        // Start
        if (currentInstructionExecution == null) {
            if (this.hasNext()) readInstruction()
            else break
        }

        // During
        duringCycleAction(cycle, registerX)

        // After
        updateCurrentInstruction()
    }
}

fun part1(instructions: Iterator<Instruction>) {
    var signalStrengthSum = 0

    instructions.run(220, duringCycleAction = { cycle, registerX ->
        if ((cycle + 20) % 40 == 0) {
            signalStrengthSum += cycle * registerX
        }
    })

    println("Part 1: The sum of the signal strengths is $signalStrengthSum")
}

fun part2(instructions: Iterator<Instruction>) {
    println("Part 2:")
    instructions.run(maxCycles = 40 * 6, duringCycleAction = { cycle, registerX ->
        val crtPositionX = (cycle - 1) % 40
        val spritePositionsX = (registerX - 1)..(registerX + 1)

        val shouldLightPixel = crtPositionX in spritePositionsX
        val pixel = if (shouldLightPixel) "#" else " "
        print(" $pixel ")

        val isEndOfLine = cycle % 40 == 0
        if (isEndOfLine) println()
    })
}

fun main() {
    val instructions = getAOCInput { rawInput ->
        rawInput.trim().split("\n")
            .map { instruction ->
                val instructionSections = instruction.split(" ")
                when (instructionSections[0]) {
                    "addx" -> Instruction.AddX(instructionSections[1].toInt())
                    "noop" -> Instruction.Noop
                    else -> throw IllegalArgumentException("Unknown instruction")
                }
            }
    }

    // Create new iterator for each puzzle
    part1(instructions.iterator())
    part2(instructions.iterator())
}

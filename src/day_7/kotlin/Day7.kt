// AOC Day 7


data class File(val name: String, val size: Long)
data class Directory(
    val name: String,
    val parent: Directory?,
    val subDirectories: MutableList<Directory> = mutableListOf(),
    val files: MutableList<File> = mutableListOf()
) {
    fun getOrCreateSubDirectory(name: String): Directory =
        subDirectories.firstOrNull { it.name == name } ?: Directory(name, this).also { subDirectories.add(it) }

    fun getAllFiles(): List<File> = files + subDirectories.flatMap { it.getAllFiles() }
    fun getAllSubDirectories(): List<Directory> = subDirectories + subDirectories.flatMap { it.getAllSubDirectories() }

    fun getSize() = getAllFiles().sumOf { it.size }
}

fun part1(rootDirectory: Directory) {
    val smallDirectoriesTotalSize = (rootDirectory.getAllSubDirectories() + rootDirectory)
        .map { it.getSize() }
        .filter { it <= 100000 }
        .sum()

    println("Part 1: The small directories have a total size of $smallDirectoriesTotalSize")
}

const val TOTAL_SPACE = 70_000_000
const val REQUIRED_SPACE = 30_000_000
fun part2(rootDirectory: Directory) {
    val usedSpace = rootDirectory.getSize()
    val smallestDirectoryToDeleteSize = (rootDirectory.getAllSubDirectories() + rootDirectory)
        .map { it.getSize() }
        .filter { TOTAL_SPACE - usedSpace + it >= REQUIRED_SPACE }
        .min()

    println("Part 2: The smallest directory to delete for the update has a size of $smallestDirectoryToDeleteSize")
}

fun main() {
    val rootDirectory = getAOCInput { rawInput ->
        val terminalOutput = rawInput.trim().split("\n").drop(1) // Drop root cd

        var currentDirectory = Directory("/", null)

        fun List<String>.isFileInfo() = this[0].all { it.isDigit() }
        fun List<String>.isCDBackCommand() = this[0] == "$" && this[1] == "cd" && this[2] == ".."
        fun List<String>.isCDCommand() = this[0] == "$" && this[1] == "cd"

        terminalOutput.forEach { line ->
            val sections = line.split(" ")

            with(sections) {
                when {
                    isFileInfo() -> currentDirectory.files.add(File(sections[1], sections[0].toLong()))
                    isCDBackCommand() -> currentDirectory = currentDirectory.parent!!
                    isCDCommand() -> currentDirectory = currentDirectory.getOrCreateSubDirectory(sections[2])
                }
            }
        }

        // Go back to root
        while (currentDirectory.parent != null) currentDirectory = currentDirectory.parent!!

        currentDirectory
    }

    part1(rootDirectory)
    part2(rootDirectory)
}

import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "de.nxll.aoc2022"
version = "1.0"

val currentDay = 15

repositories {
    mavenCentral()
}

dependencies {

}

kotlin {

    target {
        compilations {

            val common by creating

            for (day in 1..currentDay) {
                val dayCompilation = create("day_$day") {
                    associateWith(common)
                    generateAOCBaseStructure(day)
                }

                tasks.create<JavaExec>("runDay$day") {
                    group = "aoc"
                    mainClass.set("Day${day}Kt")
                    classpath = dayCompilation.runtimeDependencyFiles
                }
            }

            all {
                kotlinOptions {
                    jvmTarget = "17"
                }
            }

        }
    }

    jvmToolchain(17)

}


fun KotlinCompilation<*>.generateAOCBaseStructure(day: Int) {
    val kotlinDir = defaultSourceSet.kotlin.sourceDirectories.first { it.name == "kotlin" }
    val resourcesDir = defaultSourceSet.resources.srcDirs.first()
    val mainFile = kotlinDir.resolve("Day${day}.kt")
    val inputFile = resourcesDir.resolve("input.txt")
    kotlinDir.mkdirs()
    resourcesDir.mkdirs()
    inputFile.createNewFile()

    if (mainFile.createNewFile()) {
        val templateContent = """
            // AOC Day $day
            fun main() {
                println("Hello Day $day")
            }
        """.trimIndent()

        mainFile.writeText(templateContent)
    }
}

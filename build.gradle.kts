plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.serialization") version "1.9.20"
    application
}

group = "tr.emreone.adventofcode"
version = "2024"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_19
    targetCompatibility = JavaVersion.VERSION_19
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    // Do not use, no effect; will be overridden by kotlinDslPluginOptions.jvmTarget, see KotlinDslCompilerPlugins.
    kotlinOptions.jvmTarget = JavaVersion.VERSION_19.toString()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src/main/kotlin")
        }
    }
    test {
        useJUnitPlatform()
    }
    wrapper {
        gradleVersion = "8.5"
    }
    "run"(JavaExec::class) {
        environment("SESSION_COOKIE", getValue("SESSION_COOKIE"))
    }
}

tasks.withType<Test> {
    environment("SESSION_COOKIE", getValue("SESSION_COOKIE"))
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("ch.qos.logback:logback-core:1.4.12")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.1.0")

    implementation("org.reflections:reflections:0.10.2")
    implementation("com.github.ajalt.mordant:mordant:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // implementation("tr.emreone:kotlin-utils:0.4.0")
    // implementation("com.github.EmRe-One:Kotlin-Utils:master-SNAPSHOT")
    implementation("com.github.EmRe-One:Kotlin-Utils:0.4.0")

    testImplementation(kotlin("test"))
}

/**
 *
 */
fun getValue(key: String, filename: String = "../keys.properties"): String {
    val items = HashMap<String, String>()
    val f = File(filename)

    f.forEachLine {
        val split = it.split("=")
        items[split[0].trim()] = split[1].trim().removeSurrounding("\"")
    }

    return items[key] ?: throw IllegalArgumentException("Key $key not found")
}

tasks.register("prepareNextDay") {
    var day = 1
    var packageId = ""

    doFirst {
        day = properties["day"]?.toString()?.toInt() ?: 0
        packageId = properties["packageId"]?.toString() ?: "tr.emreone.adventofcode"
    }

    doLast {
        val nextDay = day.toString().padStart(2, '0')
        val withTest = true
        val packageIdPath = packageId.replace(".", "/")

        val mainFile = "${projectDir}/src/main/kotlin/${packageIdPath}/Main.kt"
        val readmeFile = "${projectDir}/README.md"
        val newSrcFile = "${projectDir}/src/main/kotlin/${packageIdPath}/days/Day${nextDay}.kt"
        val newTestFile = "${projectDir}/src/test/kotlin/${packageIdPath}/days/Day${nextDay}Test.kt"

        if (file(newSrcFile).exists()) {
            println("WARNING: Files for Day$nextDay already exists. Do you really want to overwrite it?")
        } else {
            file(newSrcFile).writeText(
                file("${projectDir}/template/DayX.kt")
                    .readText()
                    .replace("$1", nextDay)
            )

            //file("${projectDir}/src/main/resources/day${nextDay}.txt")
            //    .writeText("")

            file(readmeFile).writeText(
                file(readmeFile).readText()
                    .replace(
                        "<!-- $1 -->", """
                            | [Day ${nextDay}](https://adventofcode.com/2023/day/${day}) | [Day${nextDay}Test.kt](./src/test/kotlin/tr/emreone/adventofcode/days/Day${nextDay}Test.kt) | [Day${nextDay}.kt](./src/main/kotlin/tr/emreone/adventofcode/days/Day${nextDay}.kt) | ![Day ${nextDay}](./aoc_tiles/2023/${nextDay}.png) |
                            ${"<!-- $1 -->"}
                        """.trimIndent()
                    )
            )

            if (withTest) {
                file(newTestFile).writeText(
                    file("${projectDir}/template/DayXTest.kt")
                        .readText()
                        .replace("$1", nextDay)
                )

                file("${projectDir}/src/test/resources/day${nextDay}_example.txt")
                    .writeText("")
            }
        }
    }
}

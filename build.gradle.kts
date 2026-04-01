plugins {
    kotlin("jvm") version "2.3.10"
    application
}

group = "eitan.alon"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
//    // Used for fuzzy time formatting
//    implementation("org.ocpsoft.prettytime:prettytime:5.0.4.Final")
}

application {
    mainClass = "eitan.alon.MainKt"
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

// Produce a self-contained fat JAR: sets Main-Class in the manifest and bundles
// all runtime dependencies (including the Kotlin stdlib) so the JAR can be run
// with plain `java -jar` without needing a separate classpath.
tasks.jar {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// Without this, Gradle does not forward the terminal's stdin to the spawned JVM process,
// causing readlnOrNull() to immediately return null and the app to exit on startup.
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
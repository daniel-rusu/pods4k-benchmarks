plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.jmhGradlePlugin)
    `jvm-test-suite`
}

group = "com.danrusu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    jmhImplementation(libs.jmhCore)
    jmhImplementation(libs.jmhAnnotations)
    jmhImplementation(libs.jmhGenerator)

    jmhImplementation(libs.pods4k)
}

plugins.withId("org.jetbrains.kotlin.jvm") {
    dependencies {
        testImplementation(libs.strikt)
        testRuntimeOnly(libs.jUnitPlatformLauncher)
    }
    testing {
        suites {
            val test by getting(JvmTestSuite::class) {
                useJUnitJupiter(libs.versions.jUnit)
            }
        }
    }
}

jmh {
    jmhVersion = libs.versions.jmh

    // Uncomment this when you want to specify which benchmark classes to run
    // includes = listOf("MapBenchmarks")
}

// Consider the output of the JMH task always out of date to allow running benchmarks again even if no code changed
tasks.withType<me.champeau.jmh.JMHTask> {
    outputs.upToDateWhen { false }
}

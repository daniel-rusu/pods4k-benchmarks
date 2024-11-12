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

    // ### Specify which benchmarks to run ###

    // Don't specify any "includes" filter if you want to run all benchmarks (takes several hours)

    // Or specify a category of benchmarks that you're interested in
    //includes = listOf("immutableArrays")

    // Or specify a sub-category of benchmarks that better resembles your use-case or includes common operations
    //includes = listOf("immutableArrays.scenarioBenchmarks")

    // Or specify an individual benchmark class
    includes = listOf("scenarioBenchmarks.matrixOperations.MatrixBenchmarks")
}

// Consider the output of the JMH task always out of date to allow running benchmarks again even if no code changed
tasks.withType<me.champeau.jmh.JMHTask> {
    outputs.upToDateWhen { false }
}

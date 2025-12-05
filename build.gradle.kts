import java.net.URI

plugins {
    kotlin("jvm") version libs.versions.kotlin
    alias(libs.plugins.jmhGradlePlugin)
    `jvm-test-suite`
}

group = "com.danrusu"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        name = "Central Portal Snapshots"
        url = URI("https://central.sonatype.com/repository/maven-snapshots")
    }
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

    /*
    ### Specify which benchmarks to run ###
    =======================================
    - Specify an individual benchmark class to run
    includes = listOf("immutableArrays.objectCollectionBenchmarks.MapBenchmarks")

    - Or specify a sub-category of benchmarks that includes common operations or which better resembles your use-case
    includes = listOf("immutableArrays.flatCollectionBenchmarks")

    - Or specify a main category of benchmarks that you're interested in (can take several hours!):
    includes = listOf("immutableArrays")

    - Or comment out the "includes" filter if you want to run all benchmarks (takes many hours!)
    // includes = ...
     */
    includes = listOf("FilterBenchmarks")
}

// Consider the output of the JMH task always out of date to allow running benchmarks again even if no code changed
tasks.withType<me.champeau.jmh.JMHTask> {
    outputs.upToDateWhen { false }
}

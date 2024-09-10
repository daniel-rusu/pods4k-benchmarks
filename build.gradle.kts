plugins {
    kotlin("jvm") version libs.versions.kotlin
    `jvm-test-suite`
}

group = "com.danrusu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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

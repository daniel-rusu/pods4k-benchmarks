package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.random.Random

class DataGeneratorTest {
    @Test
    fun `randomChar returns an alphanumeric character`() {
        val validCharacters = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        expectThat(DataGenerator.randomChar(Random(123)) in validCharacters)
            .isEqualTo(true)
    }
}

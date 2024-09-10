package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.isIn
import strikt.assertions.length
import strikt.assertions.message
import kotlin.random.Random

class DataGeneratorTest {
    @Test
    fun `generateString validation`() {
        expectThrows<IllegalArgumentException> {
            DataGenerator.generateString(minLength = -1, maxLength = 10, Random.Default)
        }.message.isEqualTo("minLength (-1) cannot be negative")

        expectThrows<IllegalArgumentException> {
            DataGenerator.generateString(minLength = 10, maxLength = 9, Random.Default)
        }.message.isEqualTo("minLength (10) cannot be larger than maxLength(9)")

        expectThat(DataGenerator.generateString(minLength = 7, maxLength = 7, Random.Default))
            .hasLength(7)

        expectThat(
            DataGenerator.generateString(minLength = 3, maxLength = 6, Random.Default)
        ).length.isIn(3..6)
    }
}

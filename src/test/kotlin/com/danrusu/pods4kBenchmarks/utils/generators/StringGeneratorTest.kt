package com.danrusu.pods4kBenchmarks.utils.generators

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.isIn
import strikt.assertions.length
import strikt.assertions.message
import kotlin.random.Random

class StringGeneratorTest {
    @Test
    fun `validates configured length bounds`() {
        expectThrows<IllegalArgumentException> {
            StringGenerator(random = Random.Default, minLength = -1, maxLength = 10)
        }.message.isEqualTo("minLength (-1) cannot be negative")

        expectThrows<IllegalArgumentException> {
            StringGenerator(random = Random.Default, minLength = 10, maxLength = 9)
        }.message.isEqualTo("minLength (10) cannot be larger than maxLength(9)")
    }

    @Test
    fun `generates strings with configured length bounds`() {
        val generator = StringGenerator(
            random = Random(123),
            minLength = 5,
            maxLength = 5,
        )

        expectThat(generator.objectClass)
            .isEqualTo(String::class.java)

        expectThat(generator.next().length)
            .isEqualTo(5)
    }

    @Test
    fun `generates strings within variable length bounds`() {
        val generator = StringGenerator(
            random = Random(123),
            minLength = 3,
            maxLength = 6,
        )

        expectThat(generator.next())
            .length.isIn(3..6)
    }

    @Test
    fun `generates strings with fixed length`() {
        val generator = StringGenerator(
            random = Random.Default,
            minLength = 7,
            maxLength = 7,
        )

        expectThat(generator.next())
            .hasLength(7)
    }
}

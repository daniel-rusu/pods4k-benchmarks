package com.danrusu.pods4kBenchmarks.utils.generators

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.random.Random

class StringGeneratorTest {
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
}

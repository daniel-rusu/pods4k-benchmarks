package com.danrusu.pods4kBenchmarks.utils.generators

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import strikt.assertions.message
import kotlin.random.Random

class RandomNullabilityPolicyTest {
    @Test
    fun `validates null probability`() {
        val random = Random.Default

        expectThrows<IllegalArgumentException> {
            RandomNullabilityPolicy(nullProbability = -0.1, random = random)
        }.message.isEqualTo("nullProbability (-0.1) must be between 0.0 and 1.0")

        expectThrows<IllegalArgumentException> {
            RandomNullabilityPolicy(nullProbability = 1.1, random = random)
        }.message.isEqualTo("nullProbability (1.1) must be between 0.0 and 1.0")
    }

    @Test
    fun `null probability boundaries choose expected nullability`() {
        val random = Random.Default

        expectThat(
            RandomNullabilityPolicy(nullProbability = 0.0, random = random).shouldGenerateNull()
        ).isFalse()

        expectThat(
            RandomNullabilityPolicy(nullProbability = 1.0, random = random).shouldGenerateNull()
        ).isTrue()
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message
import kotlin.random.Random

class RandomNullabilityPolicyTest {
    @Test
    fun `validates null ratio`() {
        val random = Random.Default

        expectThrows<IllegalArgumentException> {
            RandomNullabilityPolicy(nullRatio = -0.1, random = random)
        }.message.isEqualTo("nullRatio (-0.1) must be between 0.0 and 1.0")

        expectThrows<IllegalArgumentException> {
            RandomNullabilityPolicy(nullRatio = 1.1, random = random)
        }.message.isEqualTo("nullRatio (1.1) must be between 0.0 and 1.0")
    }

    @Test
    fun `null ratio boundaries choose expected nullability`() {
        val random = Random.Default

        expectThat(RandomNullabilityPolicy(nullRatio = 0.0, random = random).shouldGenerateNull())
            .isEqualTo(false)

        expectThat(RandomNullabilityPolicy(nullRatio = 1.0, random = random).shouldGenerateNull())
            .isEqualTo(true)
    }
}

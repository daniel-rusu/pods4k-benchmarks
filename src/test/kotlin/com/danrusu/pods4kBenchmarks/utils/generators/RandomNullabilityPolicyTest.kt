package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.percent
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import kotlin.random.Random

class RandomNullabilityPolicyTest {
    @Test
    fun `null probability boundaries choose expected nullability`() {
        val random = Random.Default

        expectThat(
            RandomNullabilityPolicy(nullProbability = 0.percent(), random = random).shouldGenerateNull()
        ).isFalse()

        expectThat(
            RandomNullabilityPolicy(nullProbability = 100.percent(), random = random).shouldGenerateNull()
        ).isTrue()
    }
}

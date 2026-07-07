package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class RandomFieldGeneratorTest {
    @Test
    fun `next int uses configured random stream`() {
        val random = RecordingRandom(nextIntValues = generateSequence(100) { it + 1 })
        val generator = RandomFieldGenerator(random = random)

        repeat(10) { index ->
            expectThat(generator.nextInt())
                .isEqualTo(100 + index)
        }

        expectThat(random.numNextIntCalls)
            .isEqualTo(10)
    }
}

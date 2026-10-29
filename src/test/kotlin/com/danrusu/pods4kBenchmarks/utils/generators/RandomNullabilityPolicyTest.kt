package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.percent
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class RandomNullabilityPolicyTest {
    @Test
    fun `random policy consumes the supplied stream once per decision`() {
        val random = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.75))
        val policy = RandomNullabilityPolicy(50.percent(), random)

        expectThat(random.numNextDoubleCalls).isEqualTo(0)
        expectThat(List(2) { policy.shouldGenerateNull() })
            .isEqualTo(listOf(true, false))
        expectThat(random.numNextDoubleCalls)
            .isEqualTo(2)
    }

    @Test
    fun `fresh policies with the same seed produce the same decisions`() {
        val firstPolicy = RandomNullabilityPolicy(50.percent(), TestGeneratorRngs(123).nullabilityDecisionsRng)
        val secondPolicy = RandomNullabilityPolicy(50.percent(), TestGeneratorRngs(123).nullabilityDecisionsRng)

        expectThat(List(20) { firstPolicy.shouldGenerateNull() })
            .isEqualTo(List(20) { secondPolicy.shouldGenerateNull() })
    }

    @Test
    fun `boundary probabilities still advance the decision stream`() {
        for ((nullProbability, expectedDecision) in listOf(0.percent() to false, 100.percent() to true)) {
            val random = RecordingRandom(nextDoubleValues = sequenceOf(0.5))
            val policy = RandomNullabilityPolicy(nullProbability, random)

            expectThat(policy.shouldGenerateNull())
                .isEqualTo(expectedDecision)
            expectThat(random.numNextDoubleCalls)
                .isEqualTo(1)
        }
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class NullabilityPolicyFactoryTest {
    @Test
    fun `random validates null ratio when configured`() {
        expectThrows<IllegalArgumentException> {
            NullabilityPolicyFactory.random(nullRatio = -0.1)
        }.message.isEqualTo("nullRatio (-0.1) must be between 0.0 and 1.0")

        expectThrows<IllegalArgumentException> {
            NullabilityPolicyFactory.random(nullRatio = 1.1)
        }.message.isEqualTo("nullRatio (1.1) must be between 0.0 and 1.0")
    }

    @Test
    fun `random uses only the nullability decision rng`() {
        val dataGenerationRng = RecordingRandom(nextDoubleValues = sequenceOf(0.0))
        val nullabilityDecisionsRng = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.75))
        val predicateOutcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.0))
        val generatorRngs = TestGeneratorRngs(
            dataGenerationRng = dataGenerationRng,
            nullabilityDecisionsRng = nullabilityDecisionsRng,
            predicateOutcomeRng = predicateOutcomeRng,
        )
        val policy = NullabilityPolicyFactory.random(nullRatio = 0.5).create(generatorRngs)

        expectThat(List(2) { policy.shouldGenerateNull() })
            .isEqualTo(listOf(true, false))
        expectThat(nullabilityDecisionsRng.numNextDoubleCalls)
            .isEqualTo(2)
        expectThat(dataGenerationRng.numNextDoubleCalls)
            .isEqualTo(0)
        expectThat(predicateOutcomeRng.numNextDoubleCalls)
            .isEqualTo(0)
    }

    @Test
    fun `fresh policies with the same seed produce the same decisions`() {
        val factory = NullabilityPolicyFactory.random(nullRatio = 0.5)
        val firstPolicy = factory.create(TestGeneratorRngs(masterSeed = 123))
        val secondPolicy = factory.create(TestGeneratorRngs(masterSeed = 123))

        expectThat(List(20) { firstPolicy.shouldGenerateNull() })
            .isEqualTo(List(20) { secondPolicy.shouldGenerateNull() })
    }

    @Test
    fun `boundary ratios still advance the decision stream`() {
        for ((nullRatio, expectedDecision) in listOf(0.0 to false, 1.0 to true)) {
            val random = RecordingRandom(nextDoubleValues = sequenceOf(0.5))
            val policy = NullabilityPolicyFactory.random(nullRatio).create(
                TestGeneratorRngs(nullabilityDecisionsRng = random)
            )

            expectThat(policy.shouldGenerateNull())
                .isEqualTo(expectedDecision)
            expectThat(random.numNextDoubleCalls)
                .isEqualTo(1)
        }
    }
}

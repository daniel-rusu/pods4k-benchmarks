package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class FieldGeneratorFactoryTest {
    @Test
    fun `random field factory uses data generation rng for values`() {
        val dataGenerationRng = RecordingRandom(nextIntValues = generateSequence(123) { it + 1 })
        val nullabilityDecisionsRng = RecordingRandom()
        val filterAcceptanceRng = RecordingRandom()
        val generatorRngs = TestGeneratorRngs(
            dataGenerationRng = dataGenerationRng,
            nullabilityDecisionsRng = nullabilityDecisionsRng,
            filterAcceptanceRng = filterAcceptanceRng,
        )

        val fields = FieldGeneratorFactory.withRandomFields().create(generatorRngs)

        expectThat(List(3) { fields.nextInt() })
            .isEqualTo(listOf(123, 124, 125))

        expectThat(dataGenerationRng.numNextIntCalls)
            .isEqualTo(3)

        expectThat(nullabilityDecisionsRng.numNextDoubleCalls)
            .isEqualTo(0)

        expectThat(filterAcceptanceRng.numNextDoubleCalls)
            .isEqualTo(0)
    }

    @Test
    fun `random nullable field factory keeps null decisions separate from values`() {
        val dataGenerationRng = RecordingRandom(nextIntValues = generateSequence(123) { it + 1 })
        val nullabilityDecisionsRng = RecordingRandom(nextDoubleValues = sequenceOf(0.0, 0.6, 0.0))
        val generatorRngs = TestGeneratorRngs(
            dataGenerationRng = dataGenerationRng,
            nullabilityDecisionsRng = nullabilityDecisionsRng,
        )

        val fields = FieldGeneratorFactory.withRandomNullableFields(nullRatio = 0.5).create(generatorRngs)

        expectThat(List(3) { fields.nextNullableInt() })
            .isEqualTo(listOf(null, 123, null))

        expectThat(nullabilityDecisionsRng.numNextDoubleCalls)
            .isEqualTo(3)

        expectThat(dataGenerationRng.numNextIntCalls)
            .isEqualTo(1)
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator

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

        val generator = FieldGeneratorFactory.withRandomFields().create(generatorRngs)

        expectThat(List(3) { generator.nextInt() })
            .isEqualTo(listOf(123, 124, 125))

        expectThat(dataGenerationRng.numNextIntCalls)
            .isEqualTo(3)

        expectThat(nullabilityDecisionsRng.numNextDoubleCalls)
            .isEqualTo(0)

        expectThat(filterAcceptanceRng.numNextDoubleCalls)
            .isEqualTo(0)
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.percent
import com.danrusu.pods4kBenchmarks.utils.predicates.MedianValuePredicate
import com.danrusu.pods4kBenchmarks.utils.predicates.PrimitivePredicate
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class PrimitiveGeneratorFactoryTest {
    @Test
    fun `predicate decorator forwards rngs to a fresh source and uses the predicate outcome stream`() {
        var sourceCreations = 0
        val predicates = object : PrimitivePredicate by MedianValuePredicate {
            override fun matches(value: Int): Boolean = value % 2 == 0
        }
        val factory = PrimitiveGeneratorFactory { rngs ->
            sourceCreations++
            RandomPrimitiveGenerator(rngs.dataGenerationRng)
        }.withPredicateMatchProbability(50.percent(), predicates)

        repeat(2) {
            val dataRng = RecordingRandom(nextIntValues = sequenceOf(1, 2, 4, 3, 5))
            val nullRng = RecordingRandom()
            // Require the first value to pass the predicate and the second value to fail
            val outcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.75))
            val generator = factory.create(
                TestGeneratorRngs(
                    dataGenerationRng = dataRng,
                    nullabilityDecisionsRng = nullRng,
                    predicateOutcomeRng = outcomeRng,
                )
            )

            // 2 is the first value that passes the predicate and 3 is the next value to fails the predicate
            expectThat(List(2) { generator.nextInt() }).isEqualTo(listOf(2, 3))
            // 2 outcome decisions were made since we called generator.nextInt() 2 times
            expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(2)
            // 4 values were requested in order to find 2 acceptable values
            expectThat(dataRng.numNextIntCalls).isEqualTo(4)
            // no nullability decisions were made
            expectThat(nullRng.numNextDoubleCalls).isEqualTo(0)
        }
        expectThat(sourceCreations).isEqualTo(2)
    }

    @Test
    fun `random primitive factory uses data generation rng for values`() {
        val dataGenerationRng = RecordingRandom(nextIntValues = generateSequence(123) { it + 1 })
        val nullabilityDecisionsRng = RecordingRandom()
        val predicateOutcomeRng = RecordingRandom()
        val generatorRngs = TestGeneratorRngs(
            dataGenerationRng = dataGenerationRng,
            nullabilityDecisionsRng = nullabilityDecisionsRng,
            predicateOutcomeRng = predicateOutcomeRng,
        )

        val generator = PrimitiveGeneratorFactory.randomValues().create(generatorRngs)

        expectThat(List(3) { generator.nextInt() })
            .isEqualTo(listOf(123, 124, 125))

        expectThat(dataGenerationRng.numNextIntCalls)
            .isEqualTo(3)

        expectThat(nullabilityDecisionsRng.numNextDoubleCalls)
            .isEqualTo(0)

        expectThat(predicateOutcomeRng.numNextDoubleCalls)
            .isEqualTo(0)
    }
}

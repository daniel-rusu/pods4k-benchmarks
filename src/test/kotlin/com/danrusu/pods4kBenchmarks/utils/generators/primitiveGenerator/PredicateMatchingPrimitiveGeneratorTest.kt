package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.predicates.MedianValuePredicate
import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils.MAX_GENERATION_ATTEMPTS
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message
import kotlin.random.Random

class PredicateMatchingPrimitiveGeneratorTest {
    @Test
    fun `rejected candidates do not consume additional predicate decisions`() {
        val valueRng = RecordingRandom(nextIntValues = sequenceOf(1, 2, -3, -4, 5, 6, -1))

        val matchProbability = 0.5
        // require the first value to be less than the 0 and the second value greater or equal to 0
        val outcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.75))
        val generator = PredicateMatchingPrimitiveGenerator(
            source = RandomPrimitiveGenerator(valueRng),
            predicates = MedianValuePredicate,
            matchProbability = matchProbability,
            predicateOutcomeRng = outcomeRng,
        )

        expectThat(generator.nextInt()).isEqualTo(-3)
        expectThat(generator.nextInt()).isEqualTo(5)
        // only 2 outcomes decides since we only called generator.nextInt() twice
        expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(2)
        // Sampled 5 values before finding 2 acceptable ones
        expectThat(valueRng.numNextIntCalls).isEqualTo(5)
    }

    @Test
    fun `impossible primitive outcome propagates bounded generation failure`() {
        val valueRng = RecordingRandom(
            nextIntValues = generateSequence { 1 } // always return 1 failing the median value predicate
        )
        val outcomeRng = RecordingRandom()
        val generator = PredicateMatchingPrimitiveGenerator(
            source = RandomPrimitiveGenerator(valueRng),
            predicates = MedianValuePredicate,
            matchProbability = 1.0, // Require 100% of values to pass the predicate
            predicateOutcomeRng = outcomeRng,
        )

        expectThrows<IllegalStateException> {
            generator.nextInt()
        }.message.isEqualTo("Unable to generate a value with predicate result true after $MAX_GENERATION_ATTEMPTS attempts")

        // only one outcome decision made
        expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(1)
        // stopped looking for acceptable values after the cutoff
        expectThat(valueRng.numNextIntCalls).isEqualTo(MAX_GENERATION_ATTEMPTS)
    }

    @Test
    fun `rejects invalid match probabilities`() {
        for (probability in listOf(-0.1, 1.1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            expectThrows<IllegalArgumentException> {
                PredicateMatchingPrimitiveGenerator(
                    source = RandomPrimitiveGenerator(Random(123)),
                    predicates = MedianValuePredicate,
                    matchProbability = probability,
                    predicateOutcomeRng = RecordingRandom(),
                )
            }.message.isEqualTo("matchProbability ($probability) must be between 0.0 and 1.0")
        }
    }
}

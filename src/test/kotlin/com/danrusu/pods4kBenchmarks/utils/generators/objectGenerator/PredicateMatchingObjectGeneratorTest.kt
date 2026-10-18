package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.FixedStringGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils.MAX_GENERATION_ATTEMPTS
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class PredicateMatchingObjectGeneratorTest {
    @Test
    fun `retries the source without consuming additional predicate decisions`() {
        val candidates = listOf("no", "no", "yes", "yes", "no", "no", "yes").iterator()
        var sourceCalls = 0
        val source = object : ObjectGenerator<String> {
            override val objectClass: Class<String> = String::class.java

            override fun next(): String {
                sourceCalls++
                return candidates.next()
            }
        }
        val matchProbability = 0.5
        // require the first value to pass the predicate and the second value to fail
        val outcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.6))
        val generator = PredicateMatchingObjectGenerator(
            source = source,
            predicate = { it == "yes" },
            matchProbability = matchProbability,
            predicateOutcomeRng = outcomeRng
        )

        expectThat(generator.next()).isEqualTo("yes")
        expectThat(generator.next()).isEqualTo("no")

        // outcomeRng only used twice because we only called generator.next() twice
        expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(2)
        // 5 values were retrieved from the source in order to find 2 acceptable values
        expectThat(sourceCalls).isEqualTo(5)
    }

    @Test
    fun `nullable values are supported`() {
        var sourceCalls = 0
        val candidates = listOf(null, "value1", null, "value2").iterator()
        val source = object : ObjectGenerator<String?> {
            override val objectClass: Class<String> = String::class.java

            override fun next(): String? {
                sourceCalls++
                return candidates.next()
            }
        }
        val matchProbability = 0.5
        // require first value to pass the fail the predicate and second value to pass
        val outcomeRng = RecordingRandom(nextDoubleValues = sequenceOf(0.6, 0.4))
        val generator = PredicateMatchingObjectGenerator(
            source = source,
            predicate = { it == null },
            matchProbability = matchProbability,
            predicateOutcomeRng = outcomeRng
        )

        expectThat(
            List(2) { generator.next() }
        ).isEqualTo(listOf("value1", null))

        expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(2)
        expectThat(sourceCalls).isEqualTo(3)
    }

    @Test
    fun `impossible object outcome propagates bounded generation failure`() {
        var predicateCalls = 0
        val outcomeRng = RecordingRandom()
        val generator = PredicateMatchingObjectGenerator(
            source = FixedStringGenerator("value"),
            predicate = {
                predicateCalls++
                false
            },
            matchProbability = 1.0,
            predicateOutcomeRng = outcomeRng,
        )

        expectThrows<IllegalStateException> {
            generator.next()
        }.message.isEqualTo(
            "Unable to generate a value with predicate result true after $MAX_GENERATION_ATTEMPTS attempts"
        )
        expectThat(predicateCalls).isEqualTo(MAX_GENERATION_ATTEMPTS)
        expectThat(outcomeRng.numNextDoubleCalls).isEqualTo(1)
    }

    @Test
    fun `rejects invalid match probabilities`() {
        for (probability in listOf(-0.1, 1.1, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)) {
            expectThrows<IllegalArgumentException> {
                PredicateMatchingObjectGenerator(
                    source = FixedStringGenerator(value = "value"),
                    predicate = { true },
                    matchProbability = probability,
                    predicateOutcomeRng = RecordingRandom()
                )
            }.message.isEqualTo("matchProbability ($probability) must be between 0.0 and 1.0")
        }
    }
}

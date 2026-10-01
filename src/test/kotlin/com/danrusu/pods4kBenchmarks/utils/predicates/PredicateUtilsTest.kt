package com.danrusu.pods4kBenchmarks.utils.predicates

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class PredicateUtilsTest {
    @Test
    fun `returns the first candidate with true requested outcome and stops generating`() {
        var attempts = 0
        val resultForTrueOutcome = PredicateUtils.generateMatching(
            expectedOutcome = true,
            generateCandidate = {
                attempts++
                attempts * 2
            },
            predicate = { it >= 6 },
        )
        expectThat(resultForTrueOutcome).isEqualTo(6)
        expectThat(attempts).isEqualTo(3)
    }

    @Test
    fun `returns the first candidate with false requested outcome and stops generating`() {
        var attempts = 0
        val resultForFalseOutcome = PredicateUtils.generateMatching(
            expectedOutcome = false,
            generateCandidate = {
                attempts++
                attempts * 2
            },
            predicate = { it < 6 },
        )
        expectThat(resultForFalseOutcome).isEqualTo(6)
        expectThat(attempts).isEqualTo(3)
    }

    @Test
    fun `accepts a matching candidate on the final allowed attempt`() {
        var attempts = 0
        val result = PredicateUtils.generateMatching(
            expectedOutcome = true,
            generateCandidate = {
                attempts++
                attempts * 2
            },
            predicate = { it == 2 * PredicateUtils.MAX_GENERATION_ATTEMPTS },
        )

        expectThat(result).isEqualTo(2 * PredicateUtils.MAX_GENERATION_ATTEMPTS)
        expectThat(attempts).isEqualTo(PredicateUtils.MAX_GENERATION_ATTEMPTS)
    }

    @Test
    fun `impossible outcomes fail after exactly the allowed number of attempts`() {
        for (expectedOutcome in listOf(false, true)) {
            var attempts = 0

            expectThrows<IllegalStateException> {
                PredicateUtils.generateMatching(
                    expectedOutcome = expectedOutcome,
                    generateCandidate = { ++attempts },
                    predicate = { !expectedOutcome },
                )
            }.message.isEqualTo(
                "Unable to generate a value with predicate result $expectedOutcome " +
                        "after ${PredicateUtils.MAX_GENERATION_ATTEMPTS} attempts"
            )

            expectThat(attempts).isEqualTo(PredicateUtils.MAX_GENERATION_ATTEMPTS)
        }
    }
}

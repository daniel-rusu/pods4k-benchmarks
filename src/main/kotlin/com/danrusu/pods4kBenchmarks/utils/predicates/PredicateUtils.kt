package com.danrusu.pods4kBenchmarks.utils.predicates

object PredicateUtils {
    const val MAX_GENERATION_ATTEMPTS: Int = 100

    /** Returns the first candidate whose predicate result equals [expectedOutcome]. */
    internal fun <T> generateMatching(
        expectedOutcome: Boolean,
        generateCandidate: () -> T,
        predicate: (T) -> Boolean,
    ): T {
        repeat(MAX_GENERATION_ATTEMPTS) {
            val candidate = generateCandidate()
            if (predicate(candidate) == expectedOutcome) {
                return candidate
            }
        }

        throw IllegalStateException(
            "Unable to generate a value with predicate result $expectedOutcome after $MAX_GENERATION_ATTEMPTS attempts"
        )
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.nextBoolean
import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils
import kotlin.random.Random

/**
 * Decorator that wraps an [ObjectGenerator] and rejection-samples the wrapped generator so the resulting values
 * match the specified [predicate] with the specified [matchProbability].
 */
class PredicateMatchingObjectGenerator<T>(
    /** The source generator that will be polled until an acceptable value is produced. */
    private val source: ObjectGenerator<T>,

    /** Predicate for evaluating generic values. */
    private val predicate: (T) -> Boolean,

    /** Each generated value will match the [predicate] with this probability. */
    private val matchProbability: Percent,

    /**
     * Used to determine whether the next value should pass or fail the predicate.
     *
     * IMPORTANT:
     * This should be a separate RNG stream dedicated to predicate decisions so that other data types don't change
     * predicate outcomes etc.
     */
    private val predicateOutcomeRng: Random,
) : ObjectGenerator<T> {
    override val objectClass: Class<T & Any> = source.objectClass

    override fun next(): T {
        val expectedOutcome = predicateOutcomeRng.nextBoolean(trueProbability = matchProbability)
        return PredicateUtils.generateMatching(expectedOutcome, source::next, predicate)
    }
}

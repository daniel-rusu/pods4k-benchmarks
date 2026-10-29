package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.nextBoolean
import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils
import com.danrusu.pods4kBenchmarks.utils.predicates.PrimitivePredicate
import kotlin.random.Random

/**
 * Decorator that wraps a [PrimitiveGenerator] and rejection-samples the wrapped generator so the resulting values
 * match the specified [predicates] with the specified [matchProbability].
 */
internal class PredicateMatchingPrimitiveGenerator(
    /** The source generator that will be polled until an acceptable value is produced. */
    private val source: PrimitiveGenerator,

    /** Predicate for evaluating primitive values. */
    private val predicates: PrimitivePredicate,

    /** Each generated value will match the [predicates] with this probability. */
    private val matchProbability: Percent,

    /**
     * Used to determine whether the next value should pass or fail the predicate.
     *
     * IMPORTANT:
     * This should be a separate RNG stream dedicated to predicate decisions so that other data types don't change
     * predicate outcomes etc.
     */
    private val predicateOutcomeRng: Random,
) : PrimitiveGenerator {
    override fun nextBoolean(): Boolean = next(source::nextBoolean, predicates::matches)

    override fun nextByte(): Byte = next(source::nextByte, predicates::matches)

    override fun nextChar(): Char = next(source::nextChar, predicates::matches)

    override fun nextShort(): Short = next(source::nextShort, predicates::matches)

    override fun nextInt(): Int = next(source::nextInt, predicates::matches)

    override fun nextFloat(): Float = next(source::nextFloat, predicates::matches)

    override fun nextLong(): Long = next(source::nextLong, predicates::matches)

    override fun nextDouble(): Double = next(source::nextDouble, predicates::matches)

    private fun <T> next(
        generateCandidate: () -> T,
        predicate: (T) -> Boolean,
    ): T {
        val expectedOutcome = predicateOutcomeRng.nextBoolean(trueProbability = matchProbability)
        return PredicateUtils.generateMatching(expectedOutcome, generateCandidate, predicate)
    }
}

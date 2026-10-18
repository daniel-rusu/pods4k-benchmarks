package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils
import com.danrusu.pods4kBenchmarks.utils.predicates.PrimitivePredicate
import kotlin.random.Random

/** Conditions every specialized primitive method on independently selected predicate outcomes. */
internal class PredicateMatchingPrimitiveGenerator(
    private val source: PrimitiveGenerator,
    private val predicates: PrimitivePredicate,
    private val matchProbability: Double,
    private val predicateOutcomeRng: Random,
) : PrimitiveGenerator {
    init {
        require(matchProbability in 0.0..1.0) { "matchProbability ($matchProbability) must be between 0.0 and 1.0" }
    }

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
        val expectedOutcome = predicateOutcomeRng.nextDouble() < matchProbability
        return PredicateUtils.generateMatching(expectedOutcome, generateCandidate, predicate)
    }
}
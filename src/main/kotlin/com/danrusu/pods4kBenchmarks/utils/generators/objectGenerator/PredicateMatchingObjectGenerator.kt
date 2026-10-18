package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils
import kotlin.random.Random

/** Conditions an object generator on independently selected predicate outcomes. */
class PredicateMatchingObjectGenerator<T>(
    private val source: ObjectGenerator<T>,
    private val predicate: (T) -> Boolean,
    private val matchProbability: Double,
    private val predicateOutcomeRng: Random,
) : ObjectGenerator<T> {
    init {
        require(matchProbability in 0.0..1.0) { "matchProbability ($matchProbability) must be between 0.0 and 1.0" }
    }

    override val objectClass: Class<T & Any> = source.objectClass

    override fun next(): T {
        val expectedOutcome = predicateOutcomeRng.nextDouble() < matchProbability
        return PredicateUtils.generateMatching(expectedOutcome, source::next, predicate)
    }
}
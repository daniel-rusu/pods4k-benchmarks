package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.nextBoolean
import com.danrusu.pods4kBenchmarks.utils.predicates.PredicateUtils
import kotlin.random.Random

/** Conditions an object generator on independently selected predicate outcomes. */
class PredicateMatchingObjectGenerator<T>(
    private val source: ObjectGenerator<T>,
    private val predicate: (T) -> Boolean,
    private val matchProbability: Percent,
    private val predicateOutcomeRng: Random,
) : ObjectGenerator<T> {
    override val objectClass: Class<T & Any> = source.objectClass

    override fun next(): T {
        val expectedOutcome = predicateOutcomeRng.nextBoolean(trueProbability = matchProbability)
        return PredicateUtils.generateMatching(expectedOutcome, source::next, predicate)
    }
}

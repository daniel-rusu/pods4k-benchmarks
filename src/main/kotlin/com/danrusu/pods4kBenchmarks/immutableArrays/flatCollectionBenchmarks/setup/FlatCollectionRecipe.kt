package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.PredicateMatchingObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PredicateMatchingPrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.predicates.PrimitivePredicate
import kotlin.random.Random

/** Actual generation components for one materialization; dependencies may carry mutable state. */
class FlatCollectionRecipe(
    /** Collection sizes are polled from this distribution */
    val sizeDistribution: Distribution,

    /** Collections of simple types (Byte, Boolean, Int, ...) are populated by this generator. */
    val primitiveGenerator: PrimitiveGenerator,

    /** Collections of [String] elements are populated by this generator. */
    val referenceGenerator: ObjectGenerator<String>,
)

/**
 * Creates a new recipe that wraps the original generators to produce values that match the predicates with the
 * specified [matchProbability].
 *
 * The [predicateOutcomeRng] determines whether the next value should pass or fail the predicate and the original
 * generators are polled until an acceptable value is produced.
 */
fun FlatCollectionRecipe.withPredicateMatchProbability(
    predicateOutcomeRng: Random,
    matchProbability: Percent,
    primitivePredicates: PrimitivePredicate,
    referencePredicate: (String) -> Boolean,
): FlatCollectionRecipe {
    return FlatCollectionRecipe(
        sizeDistribution = sizeDistribution,
        primitiveGenerator = PredicateMatchingPrimitiveGenerator(
            source = this.primitiveGenerator,
            predicates = primitivePredicates,
            matchProbability = matchProbability,
            predicateOutcomeRng = predicateOutcomeRng,
        ),
        referenceGenerator = PredicateMatchingObjectGenerator(
            source = this.referenceGenerator,
            predicate = referencePredicate,
            matchProbability = matchProbability,
            predicateOutcomeRng = predicateOutcomeRng,
        ),
    )
}

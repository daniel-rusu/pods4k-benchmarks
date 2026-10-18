package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.predicates.PrimitivePredicate

/**
 * Creates [PrimitiveGenerator] instances from benchmark RNG streams.
 */
fun interface PrimitiveGeneratorFactory {
    /** Creates a primitive generator using the provided RNG streams. */
    fun create(generatorRngs: GeneratorRngs): PrimitiveGenerator

    companion object {
        /** Creates a factory for non-null random primitive values. */
        fun randomValues(): PrimitiveGeneratorFactory = PrimitiveGeneratorFactory { generatorRngs ->
            RandomPrimitiveGenerator(random = generatorRngs.dataGenerationRng)
        }
    }
}

/**
 * Decorates this factory so each generated value independently matches [predicates] with probability [matchProbability].
 * The proportion of matching values in a collection is not fixed.
 */
fun PrimitiveGeneratorFactory.withPredicateMatchProbability(
    matchProbability: Double,
    predicates: PrimitivePredicate,
): PrimitiveGeneratorFactory {
    require(matchProbability in 0.0..1.0) { "matchProbability ($matchProbability) must be between 0.0 and 1.0" }

    return PrimitiveGeneratorFactory { generatorRngs ->
        PredicateMatchingPrimitiveGenerator(
            source = create(generatorRngs),
            predicates = predicates,
            matchProbability = matchProbability,
            predicateOutcomeRng = generatorRngs.predicateOutcomeRng,
        )
    }
}

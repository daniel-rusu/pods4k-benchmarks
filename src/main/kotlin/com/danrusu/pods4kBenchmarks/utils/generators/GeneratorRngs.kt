package com.danrusu.pods4kBenchmarks.utils.generators

import kotlin.random.Random

/**
 * Groups deterministic RNG streams used to construct trial data.
 *
 * Streams are separated by purpose so data values, collection sizes, nullability decisions, and predicate outcome
 * choices can evolve independently while still being created from one benchmark seed.
 *
 * Keeping the RNG streams separate is important so data attributes remain consistent across different benchmark setup
 * combinations.  E.g. String elements use the RNG stream multiple times per element; sharing the same RNG stream for
 * data and collection sizes could result in different sizes across data types making measurements no longer comparable.
 */
interface GeneratorRngs {
    /** Shared stream for generated element values, whether primitives, references, or object properties. */
    val dataGenerationRng: Random

    /** Shared stream for determining outer collection sizes. */
    val outerCollectionSizeRng: Random

    /** Shared stream for determining nested collection sizes. */
    val nestedCollectionSizeRng: Random

    /** Shared stream for deciding whether primitive values, references, or objects should be null. */
    val nullabilityDecisionsRng: Random

    /** Shared stream for deciding whether generated values should satisfy benchmark predicates. */
    val predicateOutcomeRng: Random
}

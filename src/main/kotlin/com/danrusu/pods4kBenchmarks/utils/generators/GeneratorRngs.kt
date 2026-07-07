package com.danrusu.pods4kBenchmarks.utils.generators

import kotlin.random.Random

/**
 * Groups the deterministic random streams used by generator factories.
 *
 * Streams are separated by purpose so data values, nullability decisions, and filter acceptance choices can evolve
 * independently while still being created from one benchmark seed.
 */
interface GeneratorRngs {
    /** Shared stream for generated element values, whether simple fields, references, or object fields. */
    val dataGenerationRng: Random

    /** Shared stream for deciding whether simple fields, references, or objects should be null. */
    val nullabilityDecisionsRng: Random

    /** Shared stream for deciding whether generated values should satisfy benchmark filter predicates. */
    val filterAcceptanceRng: Random

    /** Creates a new independent RNG stream split from the benchmark's master RNG. */
    fun createNewRng(): Random
}

package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import kotlin.random.Random

/**
 * Provides purpose-specific RNG streams for benchmark data generators.
 *
 * Streams must stay separated when they serve different purposes. For example, string generation consumes a different
 * number of random values than numeric generation, so sharing that stream with collection sizes, null placement, or
 * predicate outcomes would make those benchmark dimensions depend on the selected data type.
 *
 * Streams should be shared when multiple generators are doing the same kind of work in one benchmark setup. Simple
 * primitive values, references, and object properties all draw from [dataGenerationRng] so they represent one
 * deterministic sequence
 * of generated values; null placement uses [nullabilityDecisionsRng]; predicate scenarios use [predicateOutcomeRng].
 */
class BenchmarkGeneratorRngs(
    private val rngFactory: RngFactory,
) : GeneratorRngs {
    /** Shared stream for generated element values, whether primitives, references, or object properties. */
    override val dataGenerationRng: Random = rngFactory.createRng()

    /** Shared stream for deciding whether primitive values, references, or objects should be null. */
    override val nullabilityDecisionsRng: Random = rngFactory.createRng()

    /** Shared stream for deciding whether generated values should satisfy benchmark predicates. */
    override val predicateOutcomeRng: Random = rngFactory.createRng()

    /** Creates a new independent RNG stream split from the benchmark's master RNG. */
    override fun createNewRng(): Random = rngFactory.createRng()
}

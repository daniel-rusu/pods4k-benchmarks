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
 * deterministic sequence of generated values; null placement uses [nullabilityDecisionsRng]; predicate scenarios use
 * [predicateOutcomeRng]; outer collection sizes use [outerCollectionSizeRng]; inner collection sizes use
 * [nestedCollectionSizeRng].
 */
class BenchmarkGeneratorRngs(masterSeed: Long = 0L) : GeneratorRngs {
    override val dataGenerationRng: Random
    override val nullabilityDecisionsRng: Random
    override val predicateOutcomeRng: Random
    override val outerCollectionSizeRng: Random
    override val nestedCollectionSizeRng: Random

    init {
        val rngFactory = RngFactory(masterSeed)
        // Preserve this allocation order to keep existing datasets stable. Append new streams at the end.
        dataGenerationRng = rngFactory.createRng()
        nullabilityDecisionsRng = rngFactory.createRng()
        predicateOutcomeRng = rngFactory.createRng()
        outerCollectionSizeRng = rngFactory.createRng()
        nestedCollectionSizeRng = rngFactory.createRng()
    }
}

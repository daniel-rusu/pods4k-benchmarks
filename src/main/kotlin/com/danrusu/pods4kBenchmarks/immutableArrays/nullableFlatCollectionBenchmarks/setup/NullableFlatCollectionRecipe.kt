package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGenerator

/** Actual generation components for one materialization; dependencies may carry mutable state. */
class NullableFlatCollectionRecipe(
    /** Collection sizes are polled from this distribution */
    val sizeDistribution: Distribution,

    /** Collections of simple nullable types (Byte?, Boolean?, Int?, ...) are populated by this generator. */
    val primitiveGenerator: PrimitiveGenerator,

    /** Collections of nullable [String] elements are populated by this generator. */
    val referenceGenerator: ObjectGenerator<String>,

    /** Determines which elements should be null. */
    val nullabilityPolicy: NullabilityPolicy,
)

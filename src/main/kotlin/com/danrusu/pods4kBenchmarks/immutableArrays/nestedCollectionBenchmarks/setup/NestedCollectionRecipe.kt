package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGenerator

/** Actual generation components for one materialization; dependencies may carry mutable state. */
class NestedCollectionRecipe(
    /** The outer-collection sizes are polled from this distribution. */
    val outerSizeDistribution: Distribution,

    /** The nested-collection sizes are polled from this distribution. */
    val nestedSizeDistribution: Distribution,

    /** Nested collections of simple types (Byte, Boolean, Int, ...) are populated by this generator. */
    val primitiveGenerator: PrimitiveGenerator,

    /** Nested collections of [String] elements are populated by this generator. */
    val referenceGenerator: ObjectGenerator<String>,
)

package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator

/** Actual generation components for one materialization; dependencies may carry mutable state. */
class ObjectCollectionRecipe<T>(
    /** Collection sizes are polled from this distribution. */
    val sizeDistribution: Distribution,

    /** Collections are populated with elements created by this generator. */
    val objectGenerator: ObjectGenerator<T>,
)

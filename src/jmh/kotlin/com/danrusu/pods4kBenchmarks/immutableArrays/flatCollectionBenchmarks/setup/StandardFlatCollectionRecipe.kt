package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

import com.danrusu.pods4kBenchmarks.immutableArrays.setup.outerCollectionSizes
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.StringGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.RandomPrimitiveGenerator

/**
 * Constructs a standard recipe for creating flat collections of random values.
 *
 * Collections sizes follow the [outerCollectionSizes] distribution.
 */
fun standardFlatCollectionRecipe(rngs: GeneratorRngs): FlatCollectionRecipe = FlatCollectionRecipe(
    sizeDistribution = outerCollectionSizes(rngs.outerCollectionSizeRng),
    primitiveGenerator = RandomPrimitiveGenerator(rngs.dataGenerationRng),
    referenceGenerator = StringGenerator(rngs.dataGenerationRng),
)

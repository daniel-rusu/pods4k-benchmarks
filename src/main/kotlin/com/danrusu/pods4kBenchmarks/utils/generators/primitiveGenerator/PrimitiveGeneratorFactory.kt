package com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs

/**
 * Creates [PrimitiveGenerator] instances from benchmark RNG streams.
 */
fun interface PrimitiveGeneratorFactory {
    /** Creates a primitive generator using the provided RNG streams. */
    fun create(generatorRngs: GeneratorRngs): PrimitiveGenerator

    companion object {
        /** Creates a factory for non-null random primitive values. */
        fun withRandomValues(): PrimitiveGeneratorFactory = PrimitiveGeneratorFactory { generatorRngs ->
            RandomPrimitiveGenerator(random = generatorRngs.dataGenerationRng)
        }
    }
}

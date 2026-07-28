package com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.RandomNullabilityPolicy

/**
 * Creates [FieldGenerator] instances from benchmark RNG streams.
 */
fun interface FieldGeneratorFactory {
    /** Creates a field generator using the provided RNG streams. */
    fun create(generatorRngs: GeneratorRngs): FieldGenerator

    companion object {
        /** Creates a factory for non-null random simple fields. */
        fun withRandomFields(): FieldGeneratorFactory = FieldGeneratorFactory { generatorRngs ->
            RandomFieldGenerator(random = generatorRngs.dataGenerationRng)
        }

        /** Creates a factory whose nullable field methods generate null values [nullRatio] of the time. */
        fun withRandomNullableFields(nullRatio: Double): FieldGeneratorFactory =
            FieldGeneratorFactory { generatorRngs ->
                RandomFieldGenerator(
                    random = generatorRngs.dataGenerationRng,
                    nullabilityPolicy = RandomNullabilityPolicy(
                        nullRatio = nullRatio,
                        random = generatorRngs.nullabilityDecisionsRng,
                    ),
                )
            }
    }
}

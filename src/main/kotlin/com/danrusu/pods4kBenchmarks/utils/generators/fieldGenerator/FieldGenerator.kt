package com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.RandomNullabilityPolicy

/**
 * Generates simple field values for benchmark setup.
 *
 * Nullable field methods require a [NullabilityPolicy] so null placement can use a separate RNG stream from generated
 * values.
 */
abstract class FieldGenerator(
    private val nullabilityPolicy: NullabilityPolicy? = null,
) {
    /** Returns the next [Boolean] field value. */
    abstract fun nextBoolean(): Boolean

    /** Returns the next [Byte] field value. */
    abstract fun nextByte(): Byte

    /** Returns the next [Char] field value. */
    abstract fun nextChar(): Char

    /** Returns the next [Short] field value. */
    abstract fun nextShort(): Short

    /** Returns the next [Int] field value. */
    abstract fun nextInt(): Int

    /** Returns the next [Float] field value. */
    abstract fun nextFloat(): Float

    /** Returns the next [Long] field value. */
    abstract fun nextLong(): Long

    /** Returns the next [Double] field value. */
    abstract fun nextDouble(): Double

    /** Returns either null or the next [Boolean] field value. */
    fun nextNullableBoolean(): Boolean? = nullable { nextBoolean() }

    /** Returns either null or the next [Byte] field value. */
    fun nextNullableByte(): Byte? = nullable { nextByte() }

    /** Returns either null or the next [Char] field value. */
    fun nextNullableChar(): Char? = nullable { nextChar() }

    /** Returns either null or the next [Short] field value. */
    fun nextNullableShort(): Short? = nullable { nextShort() }

    /** Returns either null or the next [Int] field value. */
    fun nextNullableInt(): Int? = nullable { nextInt() }

    /** Returns either null or the next [Float] field value. */
    fun nextNullableFloat(): Float? = nullable { nextFloat() }

    /** Returns either null or the next [Long] field value. */
    fun nextNullableLong(): Long? = nullable { nextLong() }

    /** Returns either null or the next [Double] field value. */
    fun nextNullableDouble(): Double? = nullable { nextDouble() }

    private inline fun <T : Any> nullable(generateValue: () -> T): T? {
        return requireNotNull(nullabilityPolicy) {
            "A NullabilityPolicy must be configured to generate nullable fields"
        }.nullable(generateValue)
    }
}

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


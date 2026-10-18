package com.danrusu.pods4kBenchmarks.utils.generators

import kotlin.random.Random

/**
 * Decides whether a nullable generator should return null or generate a new value.
 */
abstract class NullabilityPolicy {
    /** Returns true when the next nullable value should be null. */
    abstract fun shouldGenerateNull(): Boolean

    /** Returns null when this policy chooses null, otherwise returns the generated value. */
    inline fun <T : Any> nullable(generateValue: () -> T): T? {
        return if (shouldGenerateNull()) null else generateValue()
    }
}

/** Creates fresh nullability policies from benchmark RNG streams. */
fun interface NullabilityPolicyFactory {
    /** Creates a policy using the provided RNG streams. */
    fun create(generatorRngs: GeneratorRngs): NullabilityPolicy

    companion object {
        /**
         * Creates policies that independently choose null for each value with probability [nullProbability].
         * The proportion of null values in a collection is not fixed.
         */
        fun random(nullProbability: Double): NullabilityPolicyFactory {
            require(nullProbability in 0.0..1.0) { "nullProbability ($nullProbability) must be between 0.0 and 1.0" }

            return NullabilityPolicyFactory { generatorRngs ->
                RandomNullabilityPolicy(
                    nullProbability = nullProbability,
                    random = generatorRngs.nullabilityDecisionsRng,
                )
            }
        }
    }
}

/**
 * Independently chooses null for each value with probability [nullProbability].
 * The proportion of null values in a collection is not fixed.
 */
class RandomNullabilityPolicy(
    private val nullProbability: Double,
    private val random: Random,
) : NullabilityPolicy() {
    init {
        require(nullProbability in 0.0..1.0) { "nullProbability ($nullProbability) must be between 0.0 and 1.0" }
    }

    override fun shouldGenerateNull(): Boolean = random.nextDouble() < nullProbability
}

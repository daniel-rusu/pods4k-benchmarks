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

/**
 * Randomly chooses null values according to [nullRatio].
 */
class RandomNullabilityPolicy(
    private val nullRatio: Double,
    private val random: Random,
) : NullabilityPolicy() {
    init {
        require(nullRatio in 0.0..1.0) { "nullRatio ($nullRatio) must be between 0.0 and 1.0" }
    }

    override fun shouldGenerateNull(): Boolean = random.nextDouble() < nullRatio
}

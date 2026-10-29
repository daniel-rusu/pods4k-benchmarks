package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.Percent
import com.danrusu.pods4kBenchmarks.utils.nextBoolean
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
 * Independently chooses null for each value with probability [nullProbability].
 * The proportion of null values in a collection is not fixed.
 */
class RandomNullabilityPolicy(
    private val nullProbability: Percent,
    private val random: Random,
) : NullabilityPolicy() {
    override fun shouldGenerateNull(): Boolean = random.nextBoolean(trueProbability = nullProbability)
}

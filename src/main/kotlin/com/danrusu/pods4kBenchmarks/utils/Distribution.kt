package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random
import kotlin.random.nextInt

/**
 * Represents a probability distribution defined by one or more buckets. Each bucket defines the probability of that
 * bucket being chosen along with the range of values that this bucket can generate.
 *
 * For example:
 *
 * Distribution(
 *     rng,
 *     80.percent() inRange 0..10,
 *     20.percent() inRange 100..120,
 * )
 */
class Distribution(private val random: Random, vararg buckets: Bucket) {
    private val cumulativePercentages: Array<Percent>
    private val valueRanges = Array(buckets.size) { buckets[it].values }

    /**
     * The weighted mean of all bucket ranges. It need not be a value the distribution can generate when ranges have
     * gaps; for example, equally weighted ranges `0..10` and `100..110` have a mean of 55.
     */
    val averageValue: Double

    init {
        require(buckets.isNotEmpty()) { "Distribution must contain at least one bucket" }
        var accumulatedPercentage = 0.percent()
        var weightedAverage = 0.0
        cumulativePercentages = Array(buckets.size) { index ->
            val (weight, values) = buckets[index]

            weightedAverage += weight.scale(values.computeAverageValue())
            accumulatedPercentage += weight
            accumulatedPercentage
        }
        require(accumulatedPercentage == 100.percent()) {
            "Bucket weights must add up to 100% (found $accumulatedPercentage)"
        }

        averageValue = weightedAverage
    }

    fun nextValue(): Int {
        val bucketSelection = random.nextDouble()
        // The configured distributions have few buckets, so a linear scan is cheaper than a binary search.
        val bucketIndex = cumulativePercentages.indexOfFirst { bucketSelection < it.scale(1.0) }
        return random.nextInt(valueRanges[bucketIndex])
    }

    data class Bucket(val weight: Percent, val values: IntRange) {
        init {
            require(weight > 0.percent()) { "Bucket weight ($weight) must be positive" }
            require(!values.isEmpty()) {
                "values range (${values.first}..${values.last}) cannot be empty"
            }
        }
    }

    private fun IntRange.computeAverageValue(): Double {
        return (first.toDouble() + last.toDouble()) / 2.0
    }
}

infix fun Percent.inRange(values: IntRange): Distribution.Bucket = Distribution.Bucket(weight = this, values = values)

package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.nextInt

/**
 * Represents a probability distribution defined by 1 or more buckets. Each bucket defines the probability of that
 * bucket being chosen along with the range of values that this bucket can generate.
 *
 * For example:
 *
 * Distribution(
 *     rngFactory,
 *     80.percent inRange 0..10,
 *     20.percent inRange 100..120,
 * )
 */
class Distribution(rngFactory: RngFactory, vararg buckets: Bucket) {
    private val random = rngFactory.createRng()
    private val accumulatedPercentages: IntArray
    private val boundaries = Array(buckets.size) { buckets[it].values }

    /**
     * The weighted mean of all bucket ranges. It need not be a value the distribution can generate when ranges have
     * gaps; for example, equally weighted ranges `0..10` and `100..110` have a mean of 55.
     */
    val averageValue: Double

    init {
        var accumulatedPercentage = 0
        var weightedAverage = 0.0
        accumulatedPercentages = IntArray(buckets.size)

        for (index in buckets.indices) {
            val (percentage, values) = buckets[index]
            require(percentage > 0) { "The percentage ($percentage) must be positive" }

            weightedAverage += percentage * values.computeAverageValue() / 100
            accumulatedPercentage += percentage
            accumulatedPercentages[index] = accumulatedPercentage
        }
        require(accumulatedPercentage == 100) {
            "The percentages must add up to 100 (found $accumulatedPercentage)"
        }

        averageValue = weightedAverage
    }

    fun nextValue(): Int {
        val selector = random.nextInt(100)
        // The configured distributions have few buckets, so a linear scan is cheaper than a binary search.
        val index = accumulatedPercentages.indexOfFirst { selector < it }
        val values = boundaries[index]
        return random.nextInt(values)
    }

    data class Bucket(val percentage: Int, val values: IntRange) {
        init {
            require(!values.isEmpty()) {
                "values range (${values.first}..${values.last}) cannot be empty"
            }
        }
    }

    class Percentage internal constructor(private val value: Int) {
        infix fun inRange(values: IntRange): Bucket = Bucket(percentage = value, values = values)
    }

    private fun IntRange.computeAverageValue(): Double {
        return (first.toDouble() + last.toDouble()) / 2.0
    }
}

val Int.percent: Distribution.Percentage
    get() = Distribution.Percentage(this)

interface DistributionFactory {

    /** Creates a distribution with an independent random stream from [rngFactory]. */
    fun create(rngFactory: RngFactory): Distribution

    /** Size distribution for top-level flat collections. */
    object ListSizeDistribution : DistributionFactory {
        override fun create(rngFactory: RngFactory): Distribution = Distribution(
            rngFactory,
            35.percent inRange 0..10,
            30.percent inRange 11..50,
            20.percent inRange 51..200,
            10.percent inRange 201..1_000,
            5.percent inRange 1_001..10_000,
        )
    }

    /**
     * Represents the size distribution of nested lists.
     *
     * Example scenarios:
     * - List of orders with each order containing a list of products.
     * - List of people with each person containing a list of siblings.
     * - List of cities with each city containing a list of attractions.
     */
    object NestedListSizeDistribution : DistributionFactory {
        override fun create(rngFactory: RngFactory): Distribution = Distribution(
            rngFactory,
            30.percent inRange 0..1,
            35.percent inRange 2..3,
            25.percent inRange 4..7,
            7.percent inRange 8..15,
            3.percent inRange 16..25,
        )
    }
}

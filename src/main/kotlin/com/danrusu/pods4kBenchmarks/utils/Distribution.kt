package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

/**
 * Represents a probability distribution defined by 1 or more pairs.  Each pair defines the probability of that pair
 * being chosen along with the bounds for that pair, controlling the range of values that this pair can generate.
 *
 * For example, the following distribution results in
 * - For 80% of the time, a value will be generated in the closed range [0, 10]
 * - For 20% of the time, a value will be generated in the closed range [100, 120]
 *
 * Distribution(
 *     80 to Bounds(0, 10)
 *     20 to Bounds(100, 120)
 * )
 */
internal class Distribution(vararg percentages: Pair<Int, Bounds>) {
    private val accumulatedPercentages: IntArray
    private val boundaries = Array(percentages.size) { percentages[it].second }

    /**
     * Represents the mathematical average value that this distribution will produce.
     *
     * Note that this average value itself might never be generated by this distribution when the distribution contains
     * gaps between the bounds.
     *
     * For example, the following distribution only generates values
     * - between 0 and 10
     * - or between 100 and 120
     *
     * but the average value is 26 which is not in any of the bounding intervals:
     *
     * Distribution(
     *    80 to Bounds(0, 10)
     *    20 to Bounds(100, 120)
     *)
     */
    val averageValue: Double

    init {
        var accumulatedPercentage = 0
        var accumulatedValue = 0.0
        accumulatedPercentages = IntArray(percentages.size)

        for (index in percentages.indices) {
            val (percentage, bounds) = percentages[index]
            require(percentage > 0) { "The percentage ($percentage) must be positive" }

            accumulatedValue += percentage * bounds.computeAverageValue() / 100
            accumulatedPercentage += percentage
            accumulatedPercentages[index] = accumulatedPercentage
        }
        require(accumulatedPercentage == 100) {
            "The percentages must add up to 100 (found $accumulatedPercentage)"
        }

        averageValue = accumulatedValue
    }

    fun nextValue(random: Random): Int {
        val selector = random.nextInt(100)
        // note that a binary search would perform worse here as most distributions have a small number of boundaries.
        val index = accumulatedPercentages.indexOfFirst { selector < it }
        val bounds = boundaries[index]
        return random.nextInt(from = bounds.lowerBound, until = bounds.upperBound + 1)
    }

    /**
     * Defines the boundaries (both inclusive) of allowed values.
     */
    class Bounds(val lowerBound: Int, val upperBound: Int) {
        init {
            require(lowerBound <= upperBound) {
                "lowerBound ($lowerBound) cannot be greater than the upperBound ($upperBound)"
            }
        }

        fun computeAverageValue(): Double {
            return (lowerBound.toDouble() + upperBound.toDouble()) / 2.0
        }
    }

    companion object {
        /**
         * Represents the size distribution of flat lists.
         */
        val LIST_SIZE_DISTRIBUTION = Distribution(
            35 to Bounds(lowerBound = 0, upperBound = 10), // 35% of lists have between 0 and 10 elements etc.
            30 to Bounds(lowerBound = 11, upperBound = 50),
            20 to Bounds(lowerBound = 51, upperBound = 200),
            10 to Bounds(lowerBound = 201, upperBound = 1000),
            5 to Bounds(lowerBound = 1001, upperBound = 10_000),
        )

        /**
         * Represents the size distribution of nested lists.  This isn't the same as top-level flat lists as dealing
         * with multiple larger lists is typically dealt with in a streaming manner by operating on one list at a time
         * whereas nested lists are only stored in groups when they're expected to be smaller.
         *
         * Example scenarios of nested lists
         * - List of orders with each order containing a list of products.  Most orders contain just 1 or 2 different
         * products (not to be confused with the quantity of a particular product)
         *
         * - List of people with each person containing a list of siblings.  Most people have fewer than 3 siblings.
         */
        val NESTED_LIST_SIZE_DISTRIBUTION = Distribution(
            30 to Bounds(lowerBound = 0, upperBound = 1),
            35 to Bounds(lowerBound = 2, upperBound = 3),
            25 to Bounds(lowerBound = 4, upperBound = 7),
            7 to Bounds(lowerBound = 8, upperBound = 15),
            3 to Bounds(lowerBound = 16, upperBound = 25),
        )
    }
}

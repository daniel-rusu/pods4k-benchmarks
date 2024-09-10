package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

internal class Distribution(vararg percentages: Pair<Int, Bounds>) {
    private val accumulatedPercentages: IntArray
    private val boundaries = Array(percentages.size) { percentages[it].second }

    init {
        var accumulatedPercentage = 0
        accumulatedPercentages = IntArray(percentages.size)

        for (index in percentages.indices) {
            val percentage = percentages[index].first
            require(percentage > 0) { "The percentage ($percentage) must be positive" }

            accumulatedPercentage += percentage
            accumulatedPercentages[index] = accumulatedPercentage
        }
        require(accumulatedPercentage == 100) {
            "The percentages must add up to 100 (found $accumulatedPercentage)"
        }
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
    }

    companion object {
        val LIST_SIZE_DISTRIBUTION = Distribution(
            35 to Bounds(lowerBound = 0, upperBound = 10), // 35% of lists have between 0 and 10 elements etc.
            30 to Bounds(lowerBound = 11, upperBound = 50),
            20 to Bounds(lowerBound = 51, upperBound = 200),
            10 to Bounds(lowerBound = 201, upperBound = 1000),
            5 to Bounds(lowerBound = 1001, upperBound = 10_000),
        )
    }
}

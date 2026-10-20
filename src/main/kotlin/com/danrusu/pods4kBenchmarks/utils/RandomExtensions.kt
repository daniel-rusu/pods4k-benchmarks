package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

/**
 * Returns true when a draw from `[0, 1)` is below [trueProbability] scaled to that interval.
 * Always consumes one [Random.nextDouble] call, including at 0% and 100%.
 */
fun Random.nextBoolean(trueProbability: Percent): Boolean = nextDouble() < trueProbability.scale(1.0)

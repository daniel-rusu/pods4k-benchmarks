package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.inRange
import com.danrusu.pods4kBenchmarks.utils.percent
import kotlin.random.Random

/** Workload sizes for top-level collections, bound directly to the supplied stream. */
fun outerCollectionSizes(random: Random): Distribution = Distribution(
    random,
    35.percent() inRange 0..10,
    30.percent() inRange 11..50,
    20.percent() inRange 51..200,
    10.percent() inRange 201..1_000,
    5.percent() inRange 1_001..10_000,
)

/** Workload sizes for each nested collection beneath a collection owner. */
fun nestedCollectionSizes(random: Random): Distribution = Distribution(
    random,
    30.percent() inRange 0..1,
    35.percent() inRange 2..3,
    25.percent() inRange 4..7,
    7.percent() inRange 8..15,
    3.percent() inRange 16..25,
)

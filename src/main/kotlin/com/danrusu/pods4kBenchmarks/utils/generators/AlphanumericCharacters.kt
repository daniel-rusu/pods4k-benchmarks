package com.danrusu.pods4kBenchmarks.utils.generators

import kotlin.random.Random

internal object AlphanumericCharacters {
    val values: List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val naturalOrderMedian: Char = values.sorted()[values.size / 2]

    fun random(random: Random): Char = values.random(random)
}

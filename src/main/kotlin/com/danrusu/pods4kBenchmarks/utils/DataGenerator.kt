package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

private val alphanumericCharacters = ('A'..'Z') + ('a'..'z') + ('0'..'9')

internal object DataGenerator {
    fun randomChar(random: Random): Char = alphanumericCharacters.random(random)
}

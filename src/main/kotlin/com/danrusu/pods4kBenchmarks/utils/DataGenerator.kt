package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

private val alphanumericCharacters = ('A'..'Z') + ('a'..'z') + ('0'..'9')

internal object DataGenerator {
    fun generateString(minLength: Int = 3, maxLength: Int = 10, random: Random): String {
        require(minLength >= 0) { "minLength ($minLength) cannot be negative" }
        require(minLength <= maxLength) { "minLength ($minLength) cannot be larger than maxLength($maxLength)" }

        val length = random.nextInt(from = minLength, until = maxLength + 1)
        val randomChars = CharArray(length) { alphanumericCharacters.random(random) }
        return String(randomChars)
    }
}

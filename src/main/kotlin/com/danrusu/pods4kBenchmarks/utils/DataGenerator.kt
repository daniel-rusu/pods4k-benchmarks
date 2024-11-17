package com.danrusu.pods4kBenchmarks.utils

import kotlin.random.Random

private val alphanumericCharacters = ('A'..'Z') + ('a'..'z') + ('0'..'9')

internal object DataGenerator {
    val DEFAULT_MIN_STRING_LENGTH = 3
    val DEFAULT_MAX_STRING_LENGTH = 10
    val DEFAULT_MEDIAN_STRING_LENGTH = (DEFAULT_MAX_STRING_LENGTH - DEFAULT_MIN_STRING_LENGTH) / 2

    val MEDIAN_CHARACTER = alphanumericCharacters.sorted()[alphanumericCharacters.size / 2]

    fun randomString(
        minLength: Int = DEFAULT_MIN_STRING_LENGTH,
        maxLength: Int = DEFAULT_MAX_STRING_LENGTH,
        random: Random
    ): String {
        require(minLength >= 0) { "minLength ($minLength) cannot be negative" }
        require(minLength <= maxLength) { "minLength ($minLength) cannot be larger than maxLength($maxLength)" }

        val length = random.nextInt(from = minLength, until = maxLength + 1)
        val randomChars = CharArray(length) { alphanumericCharacters.random(random) }
        return String(randomChars)
    }

    fun randomByte(random: Random): Byte {
        return random.nextInt(from = Byte.MIN_VALUE.toInt(), until = Byte.MAX_VALUE.toInt() + 1).toByte()
    }

    fun randomChar(random: Random): Char = alphanumericCharacters.random(random)

    fun randomShort(random: Random): Short {
        return random.nextInt(from = Short.MIN_VALUE.toInt(), until = Short.MAX_VALUE.toInt() + 1).toShort()
    }
}

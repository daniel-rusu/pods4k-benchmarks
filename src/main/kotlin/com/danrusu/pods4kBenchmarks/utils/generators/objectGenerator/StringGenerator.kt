package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters
import kotlin.random.Random
import kotlin.random.nextInt

/** Generates random alphanumeric strings */
class StringGenerator(
    private val random: Random,
    private val minLength: Int = 3,
    private val maxLength: Int = 10,
) : ObjectGenerator<String> {
    init {
        require(minLength >= 0) { "minLength ($minLength) cannot be negative" }
        require(minLength <= maxLength) { "minLength ($minLength) cannot be larger than maxLength ($maxLength)" }
    }

    override val objectClass: Class<String> = String::class.java

    override fun next(): String {
        val length = random.nextInt(minLength..maxLength)
        val randomChars = CharArray(length) { AlphanumericCharacters.random(random) }
        return String(randomChars)
    }
}

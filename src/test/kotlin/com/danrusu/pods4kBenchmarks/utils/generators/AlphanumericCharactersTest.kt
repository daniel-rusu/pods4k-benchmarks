package com.danrusu.pods4kBenchmarks.utils.generators

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.random.Random

class AlphanumericCharactersTest {
    @Test
    fun `random returns an alphanumeric character`() {
        expectThat(AlphanumericCharacters.random(Random(123)) in AlphanumericCharacters.values)
            .isEqualTo(true)
    }

    @Test
    fun `natural order median splits the generated character domain in half`() {
        val numValuesBeforeMedian = AlphanumericCharacters.values.count { it < AlphanumericCharacters.naturalOrderMedian }
        val numValuesAtOrAfterMedian = AlphanumericCharacters.values.count { it >= AlphanumericCharacters.naturalOrderMedian }

        expectThat(numValuesBeforeMedian)
            .isEqualTo(numValuesAtOrAfterMedian)
    }
}

package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

class RngFactoryTest {
    @Test
    fun `same master seed produces the same stream sequence`() {
        val firstFactory = RngFactory(0)
        val secondFactory = RngFactory(0)
        val firstRng = firstFactory.createRng()
        val secondRng = secondFactory.createRng()

        val firstValues = List(20) { firstRng.nextLong() }
        val secondValues = List(20) { secondRng.nextLong() }

        expectThat(firstValues)
            .isEqualTo(secondValues)
    }

    @Test
    fun `successive streams are distinct`() {
        val rngFactory = RngFactory()
        val firstRng = rngFactory.createRng()
        val secondRng = rngFactory.createRng()

        val firstValues = List(20) { firstRng.nextLong() }
        val secondValues = List(20) { secondRng.nextLong() }

        expectThat(firstValues)
            .isNotEqualTo(secondValues)
    }

}

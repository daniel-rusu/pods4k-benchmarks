package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class DistributionTest {
    @Test
    fun `bounds validation`() {
        expectThrows<IllegalArgumentException> {
            Distribution.Bounds(lowerBound = 10, upperBound = 9)
        }.message.isEqualTo("lowerBound (10) cannot be greater than the upperBound (9)")
    }

    @Test
    fun `percentages must be positive`() {
        expectThrows<IllegalArgumentException> {
            Distribution(0 to Distribution.Bounds(lowerBound = 1, upperBound = 10))
        }.message.isEqualTo("The percentage (0) must be positive")
    }

    @Test
    fun `percentages must add up to 100`() {
        expectThrows<IllegalArgumentException> {
            Distribution(
                50 to Distribution.Bounds(lowerBound = 1, upperBound = 10),
                49 to Distribution.Bounds(lowerBound = 11, upperBound = 100),
            )
        }.message.isEqualTo("The percentages must add up to 100 (found 99)")

        expectThrows<IllegalArgumentException> {
            Distribution(
                50 to Distribution.Bounds(lowerBound = 1, upperBound = 10),
                30 to Distribution.Bounds(lowerBound = 11, upperBound = 100),
                21 to Distribution.Bounds(lowerBound = 101, upperBound = 1000),
            )
        }.message.isEqualTo("The percentages must add up to 100 (found 101)")
    }
}

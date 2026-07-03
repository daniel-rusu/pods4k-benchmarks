package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class DistributionTest {
    @Test
    fun `range validation`() {
        expectThrows<IllegalArgumentException> {
            10.percent inRange 10..9
        }.message.isEqualTo("values range (10..9) cannot be empty")
    }

    @Test
    fun `range average validation`() {
        with(
            Distribution(
                RngFactory(),
                100.percent inRange Int.MAX_VALUE - 8..Int.MAX_VALUE,
            )
        ) {
            expectThat(averageValue)
                .isEqualTo((Int.MAX_VALUE - 4).toDouble())
        }
    }

    @Test
    fun `percentages must be positive`() {
        expectThrows<IllegalArgumentException> {
            Distribution(RngFactory(), 0.percent inRange 1..10)
        }.message.isEqualTo("The percentage (0) must be positive")
    }

    @Test
    fun `percentages must add up to 100`() {
        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                50.percent inRange 1..10,
                49.percent inRange 11..100,
            )
        }.message.isEqualTo("The percentages must add up to 100 (found 99)")

        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                50.percent inRange 1..10,
                30.percent inRange 11..100,
                21.percent inRange 101..1000,
            )
        }.message.isEqualTo("The percentages must add up to 100 (found 101)")
    }

    @Test
    fun `average validation`() {
        with(
            Distribution(
                RngFactory(),
                80.percent inRange 10..10,
                20.percent inRange 100..100,
            )
        ) {
            expectThat(averageValue)
                .isEqualTo(28.0)
        }

        with(
            Distribution(
                RngFactory(),
                80.percent inRange 0..10,
                20.percent inRange 100..120,
            )
        ) {
            expectThat(averageValue)
                .isEqualTo(26.0)
        }
    }

    @Test
    fun `distribution produces deterministic output for a fixed master seed`() {
        val firstDistribution = DistributionFactory.ListSizeDistribution.create(RngFactory())
        val secondDistribution = DistributionFactory.ListSizeDistribution.create(RngFactory())

        val firstValues = List(100) { firstDistribution.nextValue() }
        val secondValues = List(100) { secondDistribution.nextValue() }

        expectThat(firstValues)
            .isEqualTo(secondValues)
    }
}

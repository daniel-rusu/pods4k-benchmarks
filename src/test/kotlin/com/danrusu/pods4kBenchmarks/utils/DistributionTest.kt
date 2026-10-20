package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isIn
import strikt.assertions.message

class DistributionTest {
    @Test
    fun `rejects an empty value range`() {
        expectThrows<IllegalArgumentException> {
            10.percent() inRange 10..9
        }.message.isEqualTo("values range (10..9) cannot be empty")
    }

    @Test
    fun `range average does not overflow`() {
        with(
            Distribution(
                RngFactory(),
                100.percent() inRange Int.MAX_VALUE - 8..Int.MAX_VALUE,
            )
        ) {
            expectThat(averageValue)
                .isEqualTo((Int.MAX_VALUE - 4).toDouble())
        }
    }

    @Test
    fun `accepts a range ending at Int MAX_VALUE`() {
        val range = Int.MAX_VALUE - 8..Int.MAX_VALUE
        val distribution = Distribution(RngFactory(), 100.percent() inRange range)

        repeat(100) {
            expectThat(distribution.nextValue()).isIn(range)
        }
    }

    @Test
    fun `percentages must be positive`() {
        expectThrows<IllegalArgumentException> {
            Distribution(RngFactory(), 0.percent() inRange 1..10)
        }.message.isEqualTo("Bucket weight (0%) must be positive")
    }

    @Test
    fun `percentages must add up to 100`() {
        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                50.percent() inRange 1..10,
                49.percent() inRange 11..100,
            )
        }.message.isEqualTo("Bucket weights must add up to 100% (found 99%)")

        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                50.percent() inRange 1..10,
                30.percent() inRange 11..100,
                21.percent() inRange 101..1000,
            )
        }.message.isEqualTo("Percentage sum (101%) must not exceed 100%")
    }

    @Test
    fun `rejects an empty distribution`() {
        expectThrows<IllegalArgumentException> { Distribution(RngFactory()) }
            .message.isEqualTo("Distribution must contain at least one bucket")
    }

    @Test
    fun `direct bucket construction validates its domain rules`() {
        expectThrows<IllegalArgumentException> { Distribution.Bucket(0.percent(), 1..10) }
            .message.isEqualTo("Bucket weight (0%) must be positive")
        expectThrows<IllegalArgumentException> { Distribution.Bucket(100.percent(), 10..9) }
            .message.isEqualTo("values range (10..9) cannot be empty")
    }

    @Test
    fun `fractional bucket totals must equal 100 percent exactly`() {
        val distribution = Distribution(
            RngFactory(),
            33.33.percent() inRange 1..1,
            33.33.percent() inRange 2..2,
            33.34.percent() inRange 3..3,
        )
        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                33.33.percent() inRange 1..1,
                33.33.percent() inRange 2..2,
                33.33.percent() inRange 3..3,
            )
        }.message.isEqualTo("Bucket weights must add up to 100% (found 99.99%)")
        expectThrows<IllegalArgumentException> {
            Distribution(
                RngFactory(),
                50.percent() inRange 1..1,
                50.01.percent() inRange 2..2
            )
        }.message.isEqualTo("Percentage sum (100.01%) must not exceed 100%")
    }

    @Test
    fun `single bucket always selects its range`() {
        val distribution = Distribution(RngFactory(), 100.percent() inRange -5..-5)
        expectThat(List(20) { distribution.nextValue() }).isEqualTo(List(20) { -5 })
        expectThat(distribution.averageValue).isEqualTo(-5.0)
    }

    @Test
    fun `average value is weighted by bucket percentage`() {
        with(
            Distribution(
                RngFactory(),
                80.percent() inRange 10..10,
                20.percent() inRange 100..100,
            )
        ) {
            expectThat(averageValue)
                .isEqualTo(28.0)
        }

        with(
            Distribution(
                RngFactory(),
                80.percent() inRange 0..10,
                20.percent() inRange 100..120,
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

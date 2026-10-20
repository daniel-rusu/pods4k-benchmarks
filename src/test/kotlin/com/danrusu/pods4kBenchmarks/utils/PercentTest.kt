package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message
import java.util.*
import kotlin.math.nextDown
import kotlin.math.nextUp

class PercentTest {
    @Test
    fun `integer and double factories and extensions agree on whole percentages`() {
        for (value in listOf(0, 1, 25, 50, 99, 100)) {
            val expected = Percent.of(value)
            expectThat(value.percent()).isEqualTo(expected)
            expectThat(Percent.of(value.toDouble())).isEqualTo(expected)
            expectThat(value.toDouble().percent()).isEqualTo(expected)
            expectThat(value.toDouble().percent().hashCode()).isEqualTo(expected.hashCode())
        }
    }

    @Test
    fun `every supported hundredth round trips without truncation`() {
        for (hundredths in 0..10_000) {
            val value = hundredths / 100.0
            val percent = value.percent()
            expectThat(Percent.of(value)).isEqualTo(percent)
            // Formatting is an independent view of the exact decimal value.
            expectThat(percent.toString().removeSuffix("%").toDouble()).isEqualTo(value)
        }
        expectThat(0.01.percent().toString()).isEqualTo("0.01%")
        expectThat(0.29.percent().toString()).isEqualTo("0.29%")
        expectThat(33.33.percent().toString()).isEqualTo("33.33%")
    }

    @Test
    fun `rejects out of range integers before multiplication can overflow`() {
        for (value in listOf(-1, 101, Int.MIN_VALUE, Int.MAX_VALUE)) {
            expectThrows<IllegalArgumentException> { value.percent() }
                .message.isEqualTo("Percentage ($value) must be between 0 and 100")
        }
    }

    @Test
    fun `rejects nonfinite and out of range doubles before conversion`() {
        for (value in listOf(
            -Double.NaN,
            Double.NEGATIVE_INFINITY,
            -Double.MIN_VALUE,
            0.0.nextDown(),
            100.0.nextUp(),
            Double.MAX_VALUE,
            Double.POSITIVE_INFINITY,
        )) {
            expectThrows<IllegalArgumentException> { value.percent() }
                .message.isEqualTo("Percentage ($value) must be between 0 and 100")
        }
    }

    @Test
    fun `rejects extra precision including tiny values and adjacent doubles`() {
        for (value in listOf(
            33.333,
            0.001,
            Double.MIN_VALUE,
            0.1 + 0.2,
            0.29.nextDown(),
            0.29.nextUp(),
            33.33.nextDown(),
            33.33.nextUp(),
            100.0.nextDown(),
        )) {
            expectThrows<IllegalArgumentException> { value.percent() }.message.isEqualTo(
                "Percentage ($value) must be exactly representable with at most two decimal places"
            )
        }
    }

    @Test
    fun `negative zero is canonicalized and ordering follows numeric percentages`() {
        expectThat((-0.0).percent()).isEqualTo(0.percent())
        expectThat((-0.0).percent().hashCode()).isEqualTo(0.percent().hashCode())
        expectThat((-0.0).percent().toString()).isEqualTo("0%")
        val values = listOf(100.percent(), 0.29.percent(), 0.percent(), 33.33.percent(), 0.01.percent())
        expectThat(values.sorted()).isEqualTo(
            listOf(0.percent(), 0.01.percent(), 0.29.percent(), 33.33.percent(), 100.percent())
        )
        expectThat(50.percent().compareTo(50.0.percent())).isEqualTo(0)
    }

    @Test
    fun `addition is exact with zero identity and a bounded result`() {
        expectThat(0.1.percent() + 0.2.percent()).isEqualTo(0.3.percent())
        expectThat(33.33.percent() + 33.33.percent() + 33.34.percent()).isEqualTo(100.percent())
        for (value in listOf(0.percent(), 0.01.percent(), 33.33.percent(), 100.percent())) {
            expectThat(value + 0.percent()).isEqualTo(value)
            expectThat(0.percent() + value).isEqualTo(value)
        }
        expectThrows<IllegalArgumentException> { 100.percent() + 0.01.percent() }
            .message.isEqualTo("Percentage sum (100.01%) must not exceed 100%")
        expectThrows<IllegalArgumentException> { 100.percent() + 100.percent() }
            .message.isEqualTo("Percentage sum (200%) must not exceed 100%")
    }

    @Test
    fun `display uses decimal components independently of locale`() {
        val previous = Locale.getDefault()
        try {
            Locale.setDefault(Locale.GERMANY)
            val values = listOf(0.percent(), 2.5.percent(), 33.33.percent(), 50.percent(), 100.percent())
            expectThat(values.map { it.toString() }).isEqualTo(listOf("0%", "2.5%", "33.33%", "50%", "100%"))
        } finally {
            Locale.setDefault(previous)
        }
    }

    @Test
    fun `scale multiplies quantities by the percentage`() {
        expectThat(0.percent().scale(123.45)).isEqualTo(0.0)
        expectThat(25.percent().scale(10.0)).isEqualTo(2.5)
        expectThat(12.5.percent().scale(-2.5)).isEqualTo(-0.3125)
        expectThat(100.percent().scale(123.45)).isEqualTo(123.45)
        expectThat(0.01.percent().scale(5_000.0)).isEqualTo(0.5)
    }

    @Test
    fun `scaling large values does not overflow intermediate multiplication`() {
        for (value in listOf(Double.MAX_VALUE, -Double.MAX_VALUE)) {
            expectThat(100.percent().scale(value)).isEqualTo(value)
            expectThat(50.percent().scale(value)).isEqualTo(value / 2.0)
            expectThat(0.01.percent().scale(value)).isEqualTo(value * 0.0001)
        }
    }

    @Test
    fun `scaling the smallest nonzero values by 100 percent does not underflow`() {
        for (value in listOf(Double.MIN_VALUE, -Double.MIN_VALUE)) {
            expectThat(100.percent().scale(value)).isEqualTo(value)
        }
    }
}

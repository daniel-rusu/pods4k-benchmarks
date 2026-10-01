package com.danrusu.pods4kBenchmarks.utils.predicates

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import kotlin.math.nextDown
import kotlin.math.nextUp

class MedianValuePredicateTest {
    @Test
    fun `boolean predicate matches only true`() {
        expectThat(MedianValuePredicate.matches(false)).isFalse()
        expectThat(MedianValuePredicate.matches(true)).isTrue()
    }

    @Test
    fun `byte predicates match negative values and exclude zero`() {
        expectThat(
            MedianValuePredicate.matches(Byte.MIN_VALUE)
        ).isTrue()
        expectThat(
            MedianValuePredicate.matches((-1).toByte())
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches(0.toByte())
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(1.toByte())
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(Byte.MAX_VALUE)
        ).isFalse()
    }

    @Test
    fun `short predicates match negative values and exclude zero`() {
        expectThat(
            MedianValuePredicate.matches(Short.MIN_VALUE)
        ).isTrue()
        expectThat(
            MedianValuePredicate.matches((-1).toShort())
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches(0.toShort())
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(1.toShort())
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(Short.MAX_VALUE)
        ).isFalse()
    }

    @Test
    fun `integer predicates match negative values and exclude zero`() {
        expectThat(
            MedianValuePredicate.matches(Int.MIN_VALUE)
        ).isTrue()
        expectThat(
            MedianValuePredicate.matches(-1)
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches(0)
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(1)
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(Int.MAX_VALUE)
        ).isFalse()
    }

    @Test
    fun `long predicates match negative values and exclude zero`() {
        expectThat(
            MedianValuePredicate.matches(Long.MIN_VALUE)
        ).isTrue()
        expectThat(
            MedianValuePredicate.matches(-1L)
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches(0L)
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(1L)
        ).isFalse()
        expectThat(
            MedianValuePredicate.matches(Long.MAX_VALUE)
        ).isFalse()
    }

    @Test
    fun `floating point predicates exclude their midpoint`() {
        // floats
        expectThat(MedianValuePredicate.matches(0.5f.nextDown())).isTrue()
        expectThat(MedianValuePredicate.matches(0.5f)).isFalse()
        expectThat(MedianValuePredicate.matches(0.5f.nextUp())).isFalse()

        // doubles
        expectThat(MedianValuePredicate.matches(0.5.nextDown())).isTrue()
        expectThat(MedianValuePredicate.matches(0.5)).isFalse()
        expectThat(MedianValuePredicate.matches(0.5.nextUp())).isFalse()
    }

    @Test
    fun `character predicate excludes the natural order median`() {
        val median = AlphanumericCharacters.naturalOrderMedian
        expectThat(MedianValuePredicate.MEDIAN_CHAR).isEqualTo(median)

        expectThat(MedianValuePredicate.matches(median - 1)).isTrue()
        expectThat(MedianValuePredicate.matches(median)).isFalse()
        expectThat(MedianValuePredicate.matches(median + 1)).isFalse()
    }

    @Test
    fun `string predicate includes the median string length`() {
        expectThat(
            MedianValuePredicate.matches("a".repeat(MedianValuePredicate.MEDIAN_STRING_LENGTH - 1))
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches("a".repeat(MedianValuePredicate.MEDIAN_STRING_LENGTH))
        ).isTrue()

        expectThat(
            MedianValuePredicate.matches("a".repeat(MedianValuePredicate.MEDIAN_STRING_LENGTH + 1))
        ).isFalse()
    }
}

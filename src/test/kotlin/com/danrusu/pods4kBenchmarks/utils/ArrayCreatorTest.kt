package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.math.BigDecimal

class ArrayCreatorTest {
    @Test
    fun `create empty array`() {
        with(ArrayCreator.createArray(clazz = String::class.java, size = 0) { "" }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(0)
        }

        with(ArrayCreator.createArray(clazz = BigDecimal::class.java, size = 0) { BigDecimal.ZERO }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(BigDecimal::class.java)

            expectThat(size)
                .isEqualTo(0)
        }
    }

    @Test
    fun `create array with single element`() {
        with(ArrayCreator.createArray(clazz = String::class.java, size = 1) { it.toString() }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(1)

            expectThat(this[0])
                .isEqualTo("0")
        }

        with(ArrayCreator.createArray(clazz = BigDecimal::class.java, size = 1) { BigDecimal(it) }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(BigDecimal::class.java)

            expectThat(size)
                .isEqualTo(1)

            expectThat(this[0])
                .isEqualTo(BigDecimal(0))
        }
    }

    @Test
    fun `create array with multiple elements`() {
        with(ArrayCreator.createArray(clazz = String::class.java, size = 3) { it.toString() }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(3)

            expectThat(this[0])
                .isEqualTo("0")

            expectThat(this[1])
                .isEqualTo("1")

            expectThat(this[2])
                .isEqualTo("2")
        }
    }
}

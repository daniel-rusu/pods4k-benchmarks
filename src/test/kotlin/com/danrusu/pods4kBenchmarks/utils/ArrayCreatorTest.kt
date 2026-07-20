package com.danrusu.pods4kBenchmarks.utils

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.math.BigDecimal

class ArrayCreatorTest {
    @Test
    fun `create empty array`() {
        with(ArrayCreator.createArray(componentClass = String::class.java, size = 0) { "" }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(0)
        }

        with(ArrayCreator.createArray(componentClass = BigDecimal::class.java, size = 0) { BigDecimal.ZERO }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(BigDecimal::class.java)

            expectThat(size)
                .isEqualTo(0)
        }
    }

    @Test
    fun `create array with single element`() {
        with(ArrayCreator.createArray(componentClass = String::class.java, size = 1) { it.toString() }) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(1)

            expectThat(this[0])
                .isEqualTo("0")
        }

        with(ArrayCreator.createArray(componentClass = BigDecimal::class.java, size = 1) { BigDecimal(it) }) {
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
        with(ArrayCreator.createArray(componentClass = String::class.java, size = 3) { it.toString() }) {
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

    @Test
    fun `can create arrays of nullable elements`() {
        val arrayOfNullableStrings: Array<String?> = ArrayCreator.createArray(String::class.java, size = 3) {
            if (it % 2 == 0) null else it.toString()
        }

        with(arrayOfNullableStrings) {
            expectThat(this::class.java.componentType)
                .isEqualTo(String::class.java)

            expectThat(size)
                .isEqualTo(3)

            expectThat(this[0])
                .isNull()

            expectThat(this[1])
                .isEqualTo("1")

            expectThat(this[2])
                .isNull()
        }
    }

    @Test
    fun `create nested array with the element array as its runtime component type`() {
        val nestedArray: Array<Array<String>> =
            ArrayCreator.createNestedArrays(String::class.java, size = 2) { outerIndex ->
                ArrayCreator.createArray(String::class.java, size = outerIndex + 1) { innerIndex ->
                    "$outerIndex-$innerIndex"
                }
            }

        expectThat(nestedArray::class.java.componentType)
            .isEqualTo(Array<String>::class.java)

        expectThat(nestedArray.map { it.toList() })
            .isEqualTo(listOf(listOf("0-0"), listOf("1-0", "1-1")))
    }
}

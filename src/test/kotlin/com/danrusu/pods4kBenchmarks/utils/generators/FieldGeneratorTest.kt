package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.AlwaysNullPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.CountingFieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.NeverNullPolicy
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import strikt.assertions.message

class FieldGeneratorTest {
    @Test
    fun `nullable field methods require a nullability policy`() {
        val fields = CountingFieldGenerator(nullabilityPolicy = null)

        expectThrows<IllegalArgumentException> {
            fields.nextNullableInt()
        }.message.isEqualTo("A NullabilityPolicy must be configured to generate nullable fields")
    }

    @Test
    fun `nullable field methods return null without advancing value generation`() {
        val fields = CountingFieldGenerator(nullabilityPolicy = AlwaysNullPolicy)

        expectThat(fields.nextNullableInt())
            .isNull()

        expectThat(fields.numNextIntCalls)
            .isEqualTo(0)
    }

    @Test
    fun `nullable field methods advance value generation when value is non-null`() {
        val fields = CountingFieldGenerator(nullabilityPolicy = NeverNullPolicy)

        expectThat(fields.nextNullableInt())
            .isEqualTo(1)

        expectThat(fields.numNextIntCalls)
            .isEqualTo(1)
    }
}

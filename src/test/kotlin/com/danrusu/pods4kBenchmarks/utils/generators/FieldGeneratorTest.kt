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
        val generator = CountingFieldGenerator(nullabilityPolicy = null)

        expectThrows<IllegalArgumentException> {
            generator.nextNullableInt()
        }.message.isEqualTo("A NullabilityPolicy must be configured to generate nullable fields")
    }

    @Test
    fun `nullable field methods return null without advancing value generation`() {
        val generator = CountingFieldGenerator(nullabilityPolicy = AlwaysNullPolicy)

        expectThat(generator.nextNullableInt())
            .isNull()

        expectThat(generator.numNextIntCalls)
            .isEqualTo(0)
    }

    @Test
    fun `nullable field methods advance value generation when value is non-null`() {
        val generator = CountingFieldGenerator(nullabilityPolicy = NeverNullPolicy)

        expectThat(generator.nextNullableInt())
            .isEqualTo(1)

        expectThat(generator.numNextIntCalls)
            .isEqualTo(1)
    }
}

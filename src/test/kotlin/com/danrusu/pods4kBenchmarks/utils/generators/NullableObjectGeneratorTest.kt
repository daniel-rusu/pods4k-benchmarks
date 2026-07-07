package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.AlwaysNullPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.NeverNullPolicy
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

class NullableObjectGeneratorTest {
    @Test
    fun `returns null without advancing delegate when policy chooses null`() {
        val delegate = CountingObjectGenerator()
        val generator = NullableObjectGenerator(
            delegate = delegate,
            nullabilityPolicy = AlwaysNullPolicy,
        )

        expectThat(generator.next())
            .isNull()

        expectThat(generator.objectClass)
            .isEqualTo(Int::class.javaObjectType)

        expectThat(delegate.numNextCalls)
            .isEqualTo(0)
    }

    @Test
    fun `returns delegate value when policy chooses non-null`() {
        val delegate = CountingObjectGenerator()
        val generator = NullableObjectGenerator(
            delegate = delegate,
            nullabilityPolicy = NeverNullPolicy,
        )

        expectThat(generator.next())
            .isEqualTo(1)

        expectThat(delegate.numNextCalls)
            .isEqualTo(1)
    }
}

private class CountingObjectGenerator : ObjectGenerator<Int> {
    var numNextCalls = 0
        private set

    override val objectClass: Class<Int> = Int::class.javaObjectType

    override fun next(): Int {
        numNextCalls++
        return numNextCalls
    }
}

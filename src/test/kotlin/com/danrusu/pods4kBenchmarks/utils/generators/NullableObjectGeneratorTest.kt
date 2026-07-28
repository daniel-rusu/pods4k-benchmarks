package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.AlwaysNullPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.NeverNullPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isIn
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

    @Test
    fun `preserves the delegate value class`() {
        val generator = NullableObjectGenerator(
            delegate = CountingObjectGenerator(),
            nullabilityPolicy = AlwaysNullPolicy,
        )

        expectThat(generator.objectClass)
            .isEqualTo(Int::class.javaObjectType)
    }

    @Test
    fun `nullable string generator is deterministic with fixed rng factory seed`() {
        val firstGenerator = ObjectGeneratorFactory.randomStrings()
            .nullable(nullRatio = 0.5)
            .create(TestGeneratorRngs(masterSeed = 123))
        val secondGenerator = ObjectGeneratorFactory.randomStrings()
            .nullable(nullRatio = 0.5)
            .create(TestGeneratorRngs(masterSeed = 123))

        val firstValues = List(20) { firstGenerator.next() }
        val secondValues = List(20) { secondGenerator.next() }

        expectThat(firstGenerator.objectClass)
            .isEqualTo(String::class.java)

        expectThat(firstValues)
            .isEqualTo(secondValues)

        val numNulls = firstValues.count { it == null }

        expectThat(numNulls)
            .isIn(1..19)
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

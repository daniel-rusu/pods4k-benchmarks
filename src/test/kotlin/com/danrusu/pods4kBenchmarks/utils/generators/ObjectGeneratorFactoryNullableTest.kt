package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.TestGeneratorRngs
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isIn

class ObjectGeneratorFactoryNullableTest {
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

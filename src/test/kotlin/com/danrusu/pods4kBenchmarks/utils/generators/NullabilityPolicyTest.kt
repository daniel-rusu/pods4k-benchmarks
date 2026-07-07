package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.AlwaysNullPolicy
import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.NeverNullPolicy
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

class NullabilityPolicyTest {
    @Test
    fun `nullable returns null without generating a value when policy chooses null`() {
        var numGeneratedValues = 0

        repeat(10) {
            expectThat(
                AlwaysNullPolicy.nullable { numGeneratedValues++ }
            ).isNull()
        }

        expectThat(numGeneratedValues)
            .isEqualTo(0)
    }

    @Test
    fun `nullable returns generated value when policy chooses non-null`() {
        for (index in 0..10) {
            expectThat(
                NeverNullPolicy.nullable { index }
            ).isEqualTo(index)
        }
    }
}

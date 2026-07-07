package com.danrusu.pods4kBenchmarks.utils.generators.fixtures

import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy

internal object AlwaysNullPolicy : NullabilityPolicy() {
    override fun shouldGenerateNull(): Boolean = true
}

internal object NeverNullPolicy : NullabilityPolicy() {
    override fun shouldGenerateNull(): Boolean = false
}

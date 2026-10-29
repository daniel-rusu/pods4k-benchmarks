package com.danrusu.pods4kBenchmarks.utils.generators.fixtures

import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicy

internal class RecordingNullabilityPolicy(decisions: List<Boolean>) : NullabilityPolicy() {
    private val decisions = decisions.iterator()
    var numDecisions = 0
        private set

    override fun shouldGenerateNull(): Boolean {
        numDecisions++
        return decisions.next()
    }
}

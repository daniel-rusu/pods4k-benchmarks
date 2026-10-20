package com.danrusu.pods4kBenchmarks.utils

import com.danrusu.pods4kBenchmarks.utils.generators.fixtures.RecordingRandom
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue
import kotlin.math.nextDown
import kotlin.math.nextUp

class RandomExtensionsTest {
    @Test
    fun `draws below 50 percent are true and draws at or above it are false`() {
        val random = RecordingRandom(nextDoubleValues = sequenceOf(0.25, 0.5, 0.75))

        expectThat(List(3) { random.nextBoolean(trueProbability = 50.percent()) })
            .isEqualTo(listOf(true, false, false))
        expectThat(random.numNextDoubleCalls).isEqualTo(3)
    }

    @Test
    fun `fractional probabilities compare strictly at the nearest Double boundary`() {
        for ((probability, threshold) in listOf(
            0.01.percent() to 0.0001,
            0.29.percent() to 0.0029,
            33.33.percent() to 0.3333,
            98.percent() to 0.98,
        )) {
            val random = RecordingRandom(
                nextDoubleValues = sequenceOf(threshold.nextDown(), threshold, threshold.nextUp()),
            )

            expectThat(List(3) { random.nextBoolean(trueProbability = probability) })
                .describedAs("decisions below, at, and above $probability")
                .isEqualTo(listOf(true, false, false))
            expectThat(random.numNextDoubleCalls).isEqualTo(3)
        }
    }

    @Test
    fun `endpoint probabilities consume one Double draw for every decision`() {
        for (draw in listOf(0.0, 0.5)) {
            val random = RecordingRandom(nextDoubleValues = sequenceOf(draw, draw))

            expectThat(random.nextBoolean(trueProbability = 0.percent())).isFalse()
            expectThat(random.numNextDoubleCalls).isEqualTo(1)
            expectThat(random.nextBoolean(trueProbability = 100.percent())).isTrue()
            expectThat(random.numNextDoubleCalls).isEqualTo(2)
        }
    }

    @Test
    fun `each decision uses its requested probability`() {
        val random = RecordingRandom(nextDoubleValues = sequenceOf(0.5, 0.5))

        expectThat(random.nextBoolean(trueProbability = 25.percent())).isFalse()
        expectThat(random.nextBoolean(trueProbability = 75.percent())).isTrue()
        expectThat(random.numNextDoubleCalls).isEqualTo(2)
    }
}

package com.danrusu.pods4kBenchmarks.utils.generators.fixtures

import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import kotlin.random.Random

internal class TestGeneratorRngs(
    masterSeed: Long = 0L,
    dataGenerationRng: Random? = null,
    nullabilityDecisionsRng: Random? = null,
    filterAcceptanceRng: Random? = null,
) : GeneratorRngs {
    private val rngFactory = RngFactory(masterSeed)

    override val dataGenerationRng: Random = dataGenerationRng ?: rngFactory.createRng()

    override val nullabilityDecisionsRng: Random = nullabilityDecisionsRng ?: rngFactory.createRng()

    override val filterAcceptanceRng: Random = filterAcceptanceRng ?: rngFactory.createRng()

    override fun createNewRng(): Random = rngFactory.createRng()
}

internal class RecordingRandom(
    nextIntValues: Sequence<Int> = generateSequence(0) { it + 1 },
    nextDoubleValues: Sequence<Double> = generateSequence(0.0) { it },
) : Random() {
    private val nextIntIterator = nextIntValues.iterator()
    private val nextDoubleIterator = nextDoubleValues.iterator()

    var numNextIntCalls = 0
        private set

    var numNextDoubleCalls = 0
        private set

    override fun nextBits(bitCount: Int): Int = 0

    override fun nextInt(): Int {
        numNextIntCalls++
        return nextIntIterator.next()
    }

    override fun nextDouble(): Double {
        numNextDoubleCalls++
        return nextDoubleIterator.next()
    }
}

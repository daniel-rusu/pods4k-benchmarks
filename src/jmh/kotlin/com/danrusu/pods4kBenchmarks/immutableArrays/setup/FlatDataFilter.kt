package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import kotlin.random.Random

/**
 * Filters data such that all values less than the median will be accepted and all other values will be rejected.
 *
 * This strategy is chosen to minimize filtering overhead so that the condition becomes as cheap as possible in order
 * to minimize benchmarking overhead.
 */
@Suppress("NOTHING_TO_INLINE") // to avoid any overhead on benchmarks
object FlatDataFilter {
    private const val MIN_STRING_LENGTH = 3
    private const val MAX_STRING_LENGTH = 10
    private val alphanumericCharacters = (('A'..'Z') + ('a'..'z') + ('0'..'9')).sorted()

    const val MEDIAN_STRING_LENGTH: Int = (MAX_STRING_LENGTH - MIN_STRING_LENGTH) / 2
    const val PASSING_BOOLEAN: Boolean = true
    const val MEDIAN_BYTE: Byte = 0
    val MEDIAN_CHAR: Char = alphanumericCharacters[alphanumericCharacters.size / 2]
    const val MEDIAN_SHORT: Short = 0
    const val MEDIAN_INT: Int = 0
    const val MEDIAN_FLOAT: Float = 0.5f
    const val MEDIAN_LONG: Long = 0L
    const val MEDIAN_DOUBLE: Double = 0.5

    inline fun shouldAccept(value: String): Boolean = value.length <= MEDIAN_STRING_LENGTH

    inline fun shouldAccept(value: Boolean): Boolean = value == PASSING_BOOLEAN

    inline fun shouldAccept(value: Byte): Boolean = value < MEDIAN_BYTE

    inline fun shouldAccept(value: Char): Boolean = value < MEDIAN_CHAR

    inline fun shouldAccept(value: Short): Boolean = value < MEDIAN_SHORT

    inline fun shouldAccept(value: Int): Boolean = value < MEDIAN_INT

    inline fun shouldAccept(value: Float): Boolean = value < MEDIAN_FLOAT

    inline fun shouldAccept(value: Long): Boolean = value < MEDIAN_LONG

    inline fun shouldAccept(value: Double): Boolean = value < MEDIAN_DOUBLE

    /**
     * Creates a [FlatDataProducerFactory] that produces values which will be accepted with a ratio of [acceptRatio].
     *
     * acceptRatio = (# of accepted values) / (# of total values)
     */
    fun createDataProducerFactory(acceptRatio: Double): FlatDataProducerFactory = object : FlatDataProducerFactory {
        init {
            require(acceptRatio in 0.0..1.0)
        }

        override fun create(rngFactory: RngFactory): FlatDataProducer = FilteredFlatDataProducer(
            acceptRatio = acceptRatio,
            acceptanceRandom = rngFactory.createRng(),
            dataRandom = rngFactory.createRng(),
        )

        override fun createFieldGenerator(generatorRngs: GeneratorRngs): FieldGenerator = FilteredFieldGenerator(
            acceptRatio = acceptRatio,
            acceptanceRandom = generatorRngs.filterAcceptanceRng,
            dataRandom = generatorRngs.dataGenerationRng,
        )

        override fun createStringGenerator(generatorRngs: GeneratorRngs): ObjectGenerator<String> =
            FilteredStringGenerator(
                acceptRatio = acceptRatio,
                acceptanceRandom = generatorRngs.filterAcceptanceRng,
                dataRandom = generatorRngs.dataGenerationRng,
            )
    }

    /** Produces data that the [FlatDataFilter] will accept [acceptRatio] amount of the time */
    private class FilteredFlatDataProducer(
        private val acceptRatio: Double,
        private val acceptanceRandom: Random,
        private val dataRandom: Random,
    ) : FlatDataProducer {
        override fun nextReference(): String = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = {
                val length = dataRandom.nextInt(from = MIN_STRING_LENGTH, until = MAX_STRING_LENGTH + 1)
                val randomChars = CharArray(length) { alphanumericCharacters.random(dataRandom) }
                String(randomChars)
            },
        )

        override fun nextBoolean(): Boolean = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextBoolean() },
        )

        override fun nextByte(): Byte = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { DataGenerator.randomByte(dataRandom) },
        )

        override fun nextChar(): Char = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { alphanumericCharacters.random(dataRandom) },
        )

        override fun nextShort(): Short = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { DataGenerator.randomShort(dataRandom) },
        )

        override fun nextInt(): Int = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextInt() },
        )

        override fun nextFloat(): Float = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextFloat() },
        )

        override fun nextLong(): Long = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextLong() },
        )

        override fun nextDouble(): Double = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextDouble() },
        )
    }

    private class FilteredFieldGenerator(
        private val acceptRatio: Double,
        private val acceptanceRandom: Random,
        private val dataRandom: Random,
    ) : FieldGenerator() {
        override fun nextBoolean(): Boolean = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextBoolean() },
        )

        override fun nextByte(): Byte = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { DataGenerator.randomByte(dataRandom) },
        )

        override fun nextChar(): Char = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { alphanumericCharacters.random(dataRandom) },
        )

        override fun nextShort(): Short = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { DataGenerator.randomShort(dataRandom) },
        )

        override fun nextInt(): Int = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextInt() },
        )

        override fun nextFloat(): Float = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextFloat() },
        )

        override fun nextLong(): Long = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextLong() },
        )

        override fun nextDouble(): Double = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { dataRandom.nextDouble() },
        )
    }

    private class FilteredStringGenerator(
        private val acceptRatio: Double,
        private val acceptanceRandom: Random,
        private val dataRandom: Random,
    ) : ObjectGenerator<String> {
        override val objectClass: Class<String> = String::class.java

        override fun next(): String = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = {
                val length = dataRandom.nextInt(from = MIN_STRING_LENGTH, until = MAX_STRING_LENGTH + 1)
                val randomChars = CharArray(length) { alphanumericCharacters.random(dataRandom) }
                String(randomChars)
            },
        )
    }
}

/**
 * Creates values that will be accepted [acceptRatio] proportion of the time.
 *
 * Data Generation Strategy:
 * 1. Randomly determine whether the new value should be accepted
 * 2. Repeatedly generate random values until a value is found that will be accepted / rejected based on the
 * previous step.
 *
 * Since the acceptance is based on the median value, with half of the potential values passing, the second step
 * is expected to terminate in a very short amount of iterations since each iteration has a 50% chance of
 * finding a valid value.  The probability of not finding a valid value after N attempts is equal to (1/2)^N
 * which drops off exponentially.  Note that this is true regardless of the acceptance ratio as a low acceptance
 * ratio just means that most of the generated values will be above the median so they'll be rejected.  So the
 * acceptance ratio doesn't represent the rarity of the generated values but rather how often values will be
 * below the median.
 *
 * An [acceptRatio] of 0.0 is safe because every supported data type can produce values that fail its
 * acceptance predicate.
 */
private inline fun <T> generateAppropriateValue(
    acceptRatio: Double,
    acceptanceRandom: Random,
    accept: (T) -> Boolean,
    generate: () -> T,
): T {
    val shouldBeAccepted = acceptanceRandom.nextDouble() < acceptRatio

    var value = generate()
    while (accept(value) != shouldBeAccepted) {
        value = generate()
    }
    return value
}

package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlin.random.Random

private const val MAX_VALUE_GENERATION_ATTEMPTS = 100

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

    const val MEDIAN_STRING_LENGTH: Int = (MIN_STRING_LENGTH + MAX_STRING_LENGTH) / 2
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
     * Creates a [FieldGeneratorFactory] that produces values which will be accepted with a ratio of [acceptRatio].
     *
     * acceptRatio = (# of accepted values) / (# of total values)
     */
    fun createFieldGeneratorFactory(
        acceptRatio: Double,
        sourceFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
    ): FieldGeneratorFactory = object : FieldGeneratorFactory {
        init {
            require(acceptRatio in 0.0..1.0)
        }

        override fun create(generatorRngs: GeneratorRngs): FieldGenerator = FilteredFieldGenerator(
            acceptRatio = acceptRatio,
            acceptanceRandom = generatorRngs.filterAcceptanceRng,
            source = sourceFactory.create(generatorRngs),
        )
    }

    /**
     * Creates an [ObjectGeneratorFactory] that produces strings which will be accepted with a ratio of [acceptRatio].
     *
     * acceptRatio = (# of accepted values) / (# of total values)
     */
    fun createStringGeneratorFactory(
        acceptRatio: Double,
        sourceFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(
            minLength = MIN_STRING_LENGTH,
            maxLength = MAX_STRING_LENGTH,
        ),
    ): ObjectGeneratorFactory<String> =
        object : ObjectGeneratorFactory<String>() {
            init {
                require(acceptRatio in 0.0..1.0)
            }

            override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<String> = FilteredStringGenerator(
                acceptRatio = acceptRatio,
                acceptanceRandom = generatorRngs.filterAcceptanceRng,
                source = sourceFactory.create(generatorRngs),
            )
        }

    private class FilteredFieldGenerator(
        private val acceptRatio: Double,
        private val acceptanceRandom: Random,
        private val source: FieldGenerator,
    ) : FieldGenerator() {
        override fun nextBoolean(): Boolean = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextBoolean() },
        )

        override fun nextByte(): Byte = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextByte() },
        )

        override fun nextChar(): Char = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextChar() },
        )

        override fun nextShort(): Short = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextShort() },
        )

        override fun nextInt(): Int = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextInt() },
        )

        override fun nextFloat(): Float = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextFloat() },
        )

        override fun nextLong(): Long = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextLong() },
        )

        override fun nextDouble(): Double = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.nextDouble() },
        )
    }

    private class FilteredStringGenerator(
        private val acceptRatio: Double,
        private val acceptanceRandom: Random,
        private val source: ObjectGenerator<String>,
    ) : ObjectGenerator<String> {
        override val objectClass: Class<String> = source.objectClass

        override fun next(): String = generateAppropriateValue(
            acceptRatio = acceptRatio,
            acceptanceRandom = acceptanceRandom,
            accept = { shouldAccept(it) },
            generate = { source.next() },
        )
    }
}

/**
 * Creates values that will be accepted [acceptRatio] proportion of the time.
 *
 * Data Generation Strategy:
 * 1. Randomly determine whether the new value should be accepted
 * 2. Repeatedly ask the source generator for values until a value is found that will be accepted / rejected based on
 * the previous step.
 *
 * Default source generators usually produce acceptable values quickly, but custom source generators may never produce
 * values on one side of the predicate. The retry limit fails benchmark setup loudly instead of spinning forever.
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

    repeat(MAX_VALUE_GENERATION_ATTEMPTS) {
        val value = generate()
        if (accept(value) == shouldBeAccepted) {
            return value
        }
    }

    throw IllegalStateException(
        "Unable to generate a value with accepted=$shouldBeAccepted after " +
            "$MAX_VALUE_GENERATION_ATTEMPTS attempts. Check that the source generator can produce matching values."
    )
}

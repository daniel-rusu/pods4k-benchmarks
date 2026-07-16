package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.generators.AlphanumericCharacters
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlin.random.Random

private const val MAX_VALUE_GENERATION_ATTEMPTS = 100

/**
 * Cheap predicates near each generator's midpoint, plus factories that control how often those predicates pass.
 */
@Suppress("NOTHING_TO_INLINE") // to avoid any overhead on benchmarks
object FlatDataFilter {
    private const val MIN_STRING_LENGTH = 3
    private const val MAX_STRING_LENGTH = 10

    const val MEDIAN_STRING_LENGTH: Int = (MIN_STRING_LENGTH + MAX_STRING_LENGTH) / 2
    const val PASSING_BOOLEAN: Boolean = true
    const val MEDIAN_BYTE: Byte = 0
    val MEDIAN_CHAR: Char = AlphanumericCharacters.naturalOrderMedian
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

    /** Creates a [FieldGeneratorFactory] that produces values which pass the predicate [acceptRatio] of the time */
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

    /** Creates an [ObjectGeneratorFactory] that produces strings which pass the predicate [acceptRatio] of the time */
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
        acceptRatio: Double,
        acceptanceRandom: Random,
        private val source: FieldGenerator,
    ) : FieldGenerator() {
        private val sampler = FilterAcceptanceSampler(acceptRatio, acceptanceRandom)

        override fun nextBoolean(): Boolean = sampler.nextValueMatching(::shouldAccept, source::nextBoolean)

        override fun nextByte(): Byte = sampler.nextValueMatching(::shouldAccept, source::nextByte)

        override fun nextChar(): Char = sampler.nextValueMatching(::shouldAccept, source::nextChar)

        override fun nextShort(): Short = sampler.nextValueMatching(::shouldAccept, source::nextShort)

        override fun nextInt(): Int = sampler.nextValueMatching(::shouldAccept, source::nextInt)

        override fun nextFloat(): Float = sampler.nextValueMatching(::shouldAccept, source::nextFloat)

        override fun nextLong(): Long = sampler.nextValueMatching(::shouldAccept, source::nextLong)

        override fun nextDouble(): Double = sampler.nextValueMatching(::shouldAccept, source::nextDouble)
    }

    private class FilteredStringGenerator(
        acceptRatio: Double,
        acceptanceRandom: Random,
        private val source: ObjectGenerator<String>,
    ) : ObjectGenerator<String> {
        private val sampler = FilterAcceptanceSampler(acceptRatio, acceptanceRandom)

        override val objectClass: Class<String> = source.objectClass

        override fun next(): String = sampler.nextValueMatching(::shouldAccept, source::next)
    }
}

/**
 * Chooses the desired predicate result using [acceptRatio], then samples values until one matches. The retry limit
 * reports incompatible custom generators instead of allowing benchmark setup to spin forever.
 */
private class FilterAcceptanceSampler(
    private val acceptRatio: Double,
    private val random: Random,
) {
    fun <T> nextValueMatching(
        accept: (T) -> Boolean,
        generate: () -> T,
    ): T {
        val shouldBeAccepted = random.nextDouble() < acceptRatio

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
}

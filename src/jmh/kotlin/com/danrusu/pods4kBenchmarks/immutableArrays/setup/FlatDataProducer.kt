package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.RandomFieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.StringGenerator
import kotlin.random.Random

interface FlatDataProducer {
    /** Returns the next reference element */
    fun nextReference(): String

    /** Returns the next [Boolean] element */
    fun nextBoolean(): Boolean

    /** Returns the next [Byte] element */
    fun nextByte(): Byte

    /** Returns the next [Char] element */
    fun nextChar(): Char

    /** Returns the next [Short] element */
    fun nextShort(): Short

    /** Returns the next [Int] element */
    fun nextInt(): Int

    /** Returns the next [Float] element */
    fun nextFloat(): Float

    /** Returns the next [Long] element */
    fun nextLong(): Long

    /** Returns the next [Double] element */
    fun nextDouble(): Double
}

interface FlatDataProducerFactory {
    fun create(rngFactory: RngFactory): FlatDataProducer

    fun createFieldGenerator(generatorRngs: GeneratorRngs): FieldGenerator

    fun createStringGenerator(generatorRngs: GeneratorRngs): ObjectGenerator<String>

    object RandomDataProducerFactory : FlatDataProducerFactory {
        override fun create(rngFactory: RngFactory): FlatDataProducer = RandomFlatDataProducer(rngFactory.createRng())

        override fun createFieldGenerator(generatorRngs: GeneratorRngs): FieldGenerator =
            RandomFieldGenerator(generatorRngs.dataGenerationRng)

        override fun createStringGenerator(generatorRngs: GeneratorRngs): ObjectGenerator<String> =
            StringGenerator(generatorRngs.dataGenerationRng)
    }
}

private class RandomFlatDataProducer(
    private val random: Random,
) : FlatDataProducer {
    override fun nextReference(): String = DataGenerator.randomString(random = random)

    override fun nextBoolean(): Boolean = random.nextBoolean()

    override fun nextByte(): Byte = DataGenerator.randomByte(random)

    override fun nextChar(): Char = DataGenerator.randomChar(random)

    override fun nextShort(): Short = DataGenerator.randomShort(random)

    override fun nextInt(): Int = random.nextInt()

    override fun nextFloat(): Float = random.nextFloat()

    override fun nextLong(): Long = random.nextLong()

    override fun nextDouble(): Double = random.nextDouble()
}

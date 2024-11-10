package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

interface FlatDataProducer {
    /** Indicates that a new collection is about to be created that will store [size] number of elements */
    fun startNewCollection(size: Int)

    /** Returns the next reference element that will be the [index]th element into the collection being created */
    fun nextReference(index: Int, random: Random): String

    /** Returns the next [Boolean] element that will be the [index]th element into the collection being created */
    fun nextBoolean(index: Int, random: Random): Boolean

    /** Returns the next [Byte] element that will be the [index]th element into the collection being created */
    fun nextByte(index: Int, random: Random): Byte

    /** Returns the next [Char] element that will be the [index]th element into the collection being created */
    fun nextChar(index: Int, random: Random): Char

    /** Returns the next [Short] element that will be the [index]th element into the collection being created */
    fun nextShort(index: Int, random: Random): Short

    /** Returns the next [Int] element that will be the [index]th element into the collection being created */
    fun nextInt(index: Int, random: Random): Int

    /** Returns the next [Float] element that will be the [index]th element into the collection being created */
    fun nextFloat(index: Int, random: Random): Float

    /** Returns the next [Long] element that will be the [index]th element into the collection being created */
    fun nextLong(index: Int, random: Random): Long

    /** Returns the next [Double] element that will be the [index]th element into the collection being created */
    fun nextDouble(index: Int, random: Random): Double

    object RandomDataProducer : FlatDataProducer {
        override fun startNewCollection(size: Int) {}

        override fun nextReference(index: Int, random: Random): String = DataGenerator.randomString(random = random)

        override fun nextBoolean(index: Int, random: Random): Boolean = random.nextBoolean()

        override fun nextByte(index: Int, random: Random): Byte = DataGenerator.randomByte(random)

        override fun nextChar(index: Int, random: Random): Char = DataGenerator.randomChar(random)

        override fun nextShort(index: Int, random: Random): Short = DataGenerator.randomShort(random)

        override fun nextInt(index: Int, random: Random): Int = random.nextInt()

        override fun nextFloat(index: Int, random: Random): Float = random.nextFloat()

        override fun nextLong(index: Int, random: Random): Long = random.nextLong()

        override fun nextDouble(index: Int, random: Random): Double = random.nextDouble()
    }
}

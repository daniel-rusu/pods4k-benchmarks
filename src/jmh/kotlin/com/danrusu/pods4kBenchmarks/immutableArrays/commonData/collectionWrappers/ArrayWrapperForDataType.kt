package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import kotlin.random.Random

private val EMPTY_ARRAY: Array<String> = emptyArray()
private val EMPTY_BOOLEAN_ARRAY: BooleanArray = booleanArrayOf()
private val EMPTY_BYTE_ARRAY: ByteArray = byteArrayOf()
private val EMPTY_CHAR_ARRAY: CharArray = charArrayOf()
private val EMPTY_SHORT_ARRAY: ShortArray = shortArrayOf()
private val EMPTY_INT_ARRAY: IntArray = intArrayOf()
private val EMPTY_FLOAT_ARRAY: FloatArray = floatArrayOf()
private val EMPTY_LONG_ARRAY: LongArray = longArrayOf()
private val EMPTY_DOUBLE_ARRAY: DoubleArray = doubleArrayOf()

/**
 * Creates and stores a single array of the specified data type by using the appropriate provided factory.
 *
 * For example, if the data type is [DataType.BOOLEAN], then the createBooleanArray(random, size) factory will be
 * called.  All the other array variables will be empty.
 *
 * IMPORTANT:
 * This class needs to have the same general structure as [ListWrapperForDataType] and
 * [ImmutableArrayWrapperForDataType] so that they have the same memory footprint since they're compared against each
 * other in benchmarks that measure the performance of the underlying collections.
 */
class ArrayWrapperForDataType(
    val size: Int,
    random: Random,
    dataType: DataType,
    dataProducer: FlatDataProducer,
) {
    var referenceArray: Array<String> = EMPTY_ARRAY
        private set

    var booleanArray: BooleanArray = EMPTY_BOOLEAN_ARRAY
        private set

    var byteArray: ByteArray = EMPTY_BYTE_ARRAY
        private set

    var charArray: CharArray = EMPTY_CHAR_ARRAY
        private set

    var shortArray: ShortArray = EMPTY_SHORT_ARRAY
        private set

    var intArray: IntArray = EMPTY_INT_ARRAY
        private set

    var floatArray: FloatArray = EMPTY_FLOAT_ARRAY
        private set

    var longArray: LongArray = EMPTY_LONG_ARRAY
        private set

    var doubleArray: DoubleArray = EMPTY_DOUBLE_ARRAY
        private set

    init {
        dataProducer.startNewCollection(size)
        when (dataType) {
            DataType.REFERENCE -> {
                referenceArray = Array(size) { dataProducer.nextReference(it, random) }
            }

            DataType.BOOLEAN -> {
                booleanArray = BooleanArray(size) { dataProducer.nextBoolean(it, random) }
            }

            DataType.BYTE -> {
                byteArray = ByteArray(size) { dataProducer.nextByte(it, random) }
            }

            DataType.CHAR -> {
                charArray = CharArray(size) { dataProducer.nextChar(it, random) }
            }

            DataType.SHORT -> {
                shortArray = ShortArray(size) { dataProducer.nextShort(it, random) }
            }

            DataType.INT -> {
                intArray = IntArray(size) { dataProducer.nextInt(it, random) }
            }

            DataType.FLOAT -> {
                floatArray = FloatArray(size) { dataProducer.nextFloat(it, random) }
            }

            DataType.LONG -> {
                longArray = LongArray(size) { dataProducer.nextLong(it, random) }
            }

            DataType.DOUBLE -> {
                doubleArray = DoubleArray(size) { dataProducer.nextDouble(it, random) }
            }
        }
    }

    fun copyData(): FlatDataProducer = object : FlatDataProducer {
        override fun startNewCollection(size: Int) {
            require(size == this@ArrayWrapperForDataType.size)
        }

        override fun nextReference(index: Int, random: Random): String = referenceArray[index]

        override fun nextBoolean(index: Int, random: Random): Boolean = booleanArray[index]

        override fun nextByte(index: Int, random: Random): Byte = byteArray[index]

        override fun nextChar(index: Int, random: Random): Char = charArray[index]

        override fun nextShort(index: Int, random: Random): Short = shortArray[index]

        override fun nextInt(index: Int, random: Random): Int = intArray[index]

        override fun nextFloat(index: Int, random: Random): Float = floatArray[index]

        override fun nextLong(index: Int, random: Random): Long = longArray[index]

        override fun nextDouble(index: Int, random: Random): Double = doubleArray[index]
    }
}

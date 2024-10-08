package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
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
    init {
        // IMPORTANT: This init block is defined above the properties because the data producer needs to be notified
        // that a new collection is about to be created before the properties are initialized.
        dataProducer.startNewCollection(size)
    }

    val referenceArray: Array<String> = when (dataType) {
        DataType.REFERENCE -> Array(size) { dataProducer.nextReference(it, random) }
        else -> EMPTY_ARRAY
    }
    val booleanArray: BooleanArray = when (dataType) {
        DataType.BOOLEAN -> BooleanArray(size) { dataProducer.nextBoolean(it, random) }
        else -> EMPTY_BOOLEAN_ARRAY
    }
    val byteArray: ByteArray = when (dataType) {
        DataType.BYTE -> ByteArray(size) { dataProducer.nextByte(it, random) }
        else -> EMPTY_BYTE_ARRAY
    }
    val charArray: CharArray = when (dataType) {
        DataType.CHAR -> CharArray(size) { dataProducer.nextChar(it, random) }
        else -> EMPTY_CHAR_ARRAY
    }
    val shortArray: ShortArray = when (dataType) {
        DataType.SHORT -> ShortArray(size) { dataProducer.nextShort(it, random) }
        else -> EMPTY_SHORT_ARRAY
    }
    val intArray: IntArray = when (dataType) {
        DataType.INT -> IntArray(size) { dataProducer.nextInt(it, random) }
        else -> EMPTY_INT_ARRAY
    }
    val floatArray: FloatArray = when (dataType) {
        DataType.FLOAT -> FloatArray(size) { dataProducer.nextFloat(it, random) }
        else -> EMPTY_FLOAT_ARRAY
    }
    val longArray: LongArray = when (dataType) {
        DataType.LONG -> LongArray(size) { dataProducer.nextLong(it, random) }
        else -> EMPTY_LONG_ARRAY
    }
    val doubleArray: DoubleArray = when (dataType) {
        DataType.DOUBLE -> DoubleArray(size) { dataProducer.nextDouble(it, random) }
        else -> EMPTY_DOUBLE_ARRAY
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

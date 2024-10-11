package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import kotlin.random.Random

/** Represents a wrapper class that stores a single array of the appropriate [DataType] */
sealed class ArrayWrapper(val size: Int) : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer
        ): ArrayWrapper {
            dataProducer.startNewCollection(size)

            return when (dataType) {
                DataType.REFERENCE -> ReferenceArrayWrapper(random, size, dataProducer)
                DataType.BOOLEAN -> BooleanArrayWrapper(random, size, dataProducer)
                DataType.BYTE -> ByteArrayWrapper(random, size, dataProducer)
                DataType.CHAR -> CharArrayWrapper(random, size, dataProducer)
                DataType.SHORT -> ShortArrayWrapper(random, size, dataProducer)
                DataType.INT -> IntArrayWrapper(random, size, dataProducer)
                DataType.FLOAT -> FloatArrayWrapper(random, size, dataProducer)
                DataType.LONG -> LongArrayWrapper(random, size, dataProducer)
                DataType.DOUBLE -> DoubleArrayWrapper(random, size, dataProducer)
            }
        }
    }

    fun copyData(): FlatDataProducer = object : FlatDataProducer {
        override fun startNewCollection(size: Int) {
            require(size == this@ArrayWrapper.size)
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

class ReferenceArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val referenceArray: Array<String> = Array(size) { dataProducer.nextReference(it, random) }
}

class BooleanArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val booleanArray: BooleanArray = BooleanArray(size) { dataProducer.nextBoolean(it, random) }
}

class ByteArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val byteArray: ByteArray = ByteArray(size) { dataProducer.nextByte(it, random) }
}

class CharArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val charArray: CharArray = CharArray(size) { dataProducer.nextChar(it, random) }
}

class ShortArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val shortArray: ShortArray = ShortArray(size) { dataProducer.nextShort(it, random) }
}

class IntArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val intArray: IntArray = IntArray(size) { dataProducer.nextInt(it, random) }
}

class FloatArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val floatArray: FloatArray = FloatArray(size) { dataProducer.nextFloat(it, random) }
}

class LongArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val longArray: LongArray = LongArray(size) { dataProducer.nextLong(it, random) }
}

class DoubleArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper(size) {
    override val doubleArray: DoubleArray = DoubleArray(size) { dataProducer.nextDouble(it, random) }
}

package com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.SHORT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution

/** Represents a wrapper class that stores a single array of the appropriate [DataType] */
sealed class ArrayWrapper : CollectionWrapper() {
    companion object {
        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): Array<out ArrayWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ReferenceArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            BOOLEAN -> Array(count) { BooleanArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            BYTE -> Array(count) { ByteArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            CHAR -> Array(count) { CharArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            SHORT -> Array(count) { ShortArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            INT -> Array(count) { IntArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            FLOAT -> Array(count) { FloatArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            LONG -> Array(count) { LongArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            DOUBLE -> Array(count) { DoubleArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
        }
    }
}

class ReferenceArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val referenceArray: Array<String> = Array(size) { dataProducer.nextReference() }
}

class BooleanArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val booleanArray: BooleanArray = BooleanArray(size) { dataProducer.nextBoolean() }
}

class ByteArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val byteArray: ByteArray = ByteArray(size) { dataProducer.nextByte() }
}

class CharArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val charArray: CharArray = CharArray(size) { dataProducer.nextChar() }
}

class ShortArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val shortArray: ShortArray = ShortArray(size) { dataProducer.nextShort() }
}

class IntArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val intArray: IntArray = IntArray(size) { dataProducer.nextInt() }
}

class FloatArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val floatArray: FloatArray = FloatArray(size) { dataProducer.nextFloat() }
}

class LongArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val longArray: LongArray = LongArray(size) { dataProducer.nextLong() }
}

class DoubleArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    override val doubleArray: DoubleArray = DoubleArray(size) { dataProducer.nextDouble() }
}

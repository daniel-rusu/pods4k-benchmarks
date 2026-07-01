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
sealed class ListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            size: Int,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): ListWrapper = when (dataType) {
            REFERENCE -> ReferenceListWrapper(size, dataProducer)
            BOOLEAN -> BooleanListWrapper(size, dataProducer)
            BYTE -> ByteListWrapper(size, dataProducer)
            CHAR -> CharListWrapper(size, dataProducer)
            SHORT -> ShortListWrapper(size, dataProducer)
            INT -> IntListWrapper(size, dataProducer)
            FLOAT -> FloatListWrapper(size, dataProducer)
            LONG -> LongListWrapper(size, dataProducer)
            DOUBLE -> DoubleListWrapper(size, dataProducer)
        }

        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): Array<ListWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ReferenceListWrapper(sizeDistribution.nextValue(), dataProducer) }
            BOOLEAN -> Array(count) { BooleanListWrapper(sizeDistribution.nextValue(), dataProducer) }
            BYTE -> Array(count) { ByteListWrapper(sizeDistribution.nextValue(), dataProducer) }
            CHAR -> Array(count) { CharListWrapper(sizeDistribution.nextValue(), dataProducer) }
            SHORT -> Array(count) { ShortListWrapper(sizeDistribution.nextValue(), dataProducer) }
            INT -> Array(count) { IntListWrapper(sizeDistribution.nextValue(), dataProducer) }
            FLOAT -> Array(count) { FloatListWrapper(sizeDistribution.nextValue(), dataProducer) }
            LONG -> Array(count) { LongListWrapper(sizeDistribution.nextValue(), dataProducer) }
            DOUBLE -> Array(count) { DoubleListWrapper(sizeDistribution.nextValue(), dataProducer) }
        }
    }
}

class ReferenceListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val referenceList: List<String> = createList(listSize) { dataProducer.nextReference() }
}

class BooleanListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val booleanList: List<Boolean> = createList(listSize) { dataProducer.nextBoolean() }
}

class ByteListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val byteList: List<Byte> = createList(listSize) { dataProducer.nextByte() }
}

class CharListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val charList: List<Char> = createList(listSize) { dataProducer.nextChar() }
}

class ShortListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val shortList: List<Short> = createList(listSize) { dataProducer.nextShort() }
}

class IntListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val intList: List<Int> = createList(listSize) { dataProducer.nextInt() }
}

class FloatListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val floatList: List<Float> = createList(listSize) { dataProducer.nextFloat() }
}

class LongListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val longList: List<Long> = createList(listSize) { dataProducer.nextLong() }
}

class DoubleListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val doubleList: List<Double> = createList(listSize) { dataProducer.nextDouble() }
}

private inline fun <T> createList(listSize: Int, initializer: () -> T): List<T> {
    val result = ArrayList<T>(listSize)
    repeat(listSize) { result.add(initializer()) }
    return result
}

package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import kotlin.random.Random

/** Represents a wrapper class that stores a single array of the appropriate [DataType] */
sealed class ListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer
        ): ListWrapper {
            dataProducer.startNewCollection(size)

            return when (dataType) {
                DataType.REFERENCE -> ReferenceListWrapper(random, size, dataProducer)
                DataType.BOOLEAN -> BooleanListWrapper(random, size, dataProducer)
                DataType.BYTE -> ByteListWrapper(random, size, dataProducer)
                DataType.CHAR -> CharListWrapper(random, size, dataProducer)
                DataType.SHORT -> ShortListWrapper(random, size, dataProducer)
                DataType.INT -> IntListWrapper(random, size, dataProducer)
                DataType.FLOAT -> FloatListWrapper(random, size, dataProducer)
                DataType.LONG -> LongListWrapper(random, size, dataProducer)
                DataType.DOUBLE -> DoubleListWrapper(random, size, dataProducer)
            }
        }
    }
}

class ReferenceListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val referenceList: List<String> = (0..<size).map { dataProducer.nextReference(it, random) }
}

class BooleanListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val booleanList: List<Boolean> = (0..<size).map { dataProducer.nextBoolean(it, random) }
}

class ByteListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val byteList: List<Byte> = (0..<size).map { dataProducer.nextByte(it, random) }
}

class CharListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val charList: List<Char> = (0..<size).map { dataProducer.nextChar(it, random) }
}

class ShortListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val shortList: List<Short> = (0..<size).map { dataProducer.nextShort(it, random) }
}

class IntListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val intList: List<Int> = (0..<size).map { dataProducer.nextInt(it, random) }
}

class FloatListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val floatList: List<Float> = (0..<size).map { dataProducer.nextFloat(it, random) }
}

class LongListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val longList: List<Long> = (0..<size).map { dataProducer.nextLong(it, random) }
}

class DoubleListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    override val doubleList: List<Double> = (0..<size).map { dataProducer.nextDouble(it, random) }
}

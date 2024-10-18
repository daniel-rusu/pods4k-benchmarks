package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.SHORT
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
        ): ListWrapper = when (dataType) {
            REFERENCE -> ReferenceListWrapper(random, size, dataProducer)
            BOOLEAN -> BooleanListWrapper(random, size, dataProducer)
            BYTE -> ByteListWrapper(random, size, dataProducer)
            CHAR -> CharListWrapper(random, size, dataProducer)
            SHORT -> ShortListWrapper(random, size, dataProducer)
            INT -> IntListWrapper(random, size, dataProducer)
            FLOAT -> FloatListWrapper(random, size, dataProducer)
            LONG -> LongListWrapper(random, size, dataProducer)
            DOUBLE -> DoubleListWrapper(random, size, dataProducer)
        }
    }
}

class ReferenceListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val referenceList: List<String> = (0..<size).map { dataProducer.nextReference(it, random) }
}

class BooleanListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val booleanList: List<Boolean> = (0..<size).map { dataProducer.nextBoolean(it, random) }
}

class ByteListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val byteList: List<Byte> = (0..<size).map { dataProducer.nextByte(it, random) }
}

class CharListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val charList: List<Char> = (0..<size).map { dataProducer.nextChar(it, random) }
}

class ShortListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val shortList: List<Short> = (0..<size).map { dataProducer.nextShort(it, random) }
}

class IntListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val intList: List<Int> = (0..<size).map { dataProducer.nextInt(it, random) }
}

class FloatListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val floatList: List<Float> = (0..<size).map { dataProducer.nextFloat(it, random) }
}

class LongListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val longList: List<Long> = (0..<size).map { dataProducer.nextLong(it, random) }
}

class DoubleListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val doubleList: List<Double> = (0..<size).map { dataProducer.nextDouble(it, random) }
}

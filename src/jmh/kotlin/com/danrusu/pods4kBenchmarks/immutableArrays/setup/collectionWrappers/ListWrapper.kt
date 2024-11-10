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

    override val referenceList: List<String> = createList(size) { dataProducer.nextReference(it, random) }
}

class BooleanListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val booleanList: List<Boolean> = createList(size) { dataProducer.nextBoolean(it, random) }
}

class ByteListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val byteList: List<Byte> = createList(size) { dataProducer.nextByte(it, random) }
}

class CharListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val charList: List<Char> = createList(size) { dataProducer.nextChar(it, random) }
}

class ShortListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val shortList: List<Short> = createList(size) { dataProducer.nextShort(it, random) }
}

class IntListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val intList: List<Int> = createList(size) { dataProducer.nextInt(it, random) }
}

class FloatListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val floatList: List<Float> = createList(size) { dataProducer.nextFloat(it, random) }
}

class LongListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val longList: List<Long> = createList(size) { dataProducer.nextLong(it, random) }
}

class DoubleListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val doubleList: List<Double> = createList(size) { dataProducer.nextDouble(it, random) }
}

/**
 * Creates a perfectly-sized list of the specified [size] with elements provided by the specified [initializer]
 */
private inline fun <T> createList(size: Int, initializer: (index: Int) -> T): List<T> {
    return ArrayList<T>(size).apply {
        repeat(size) { add(initializer(it)) }
    }
}

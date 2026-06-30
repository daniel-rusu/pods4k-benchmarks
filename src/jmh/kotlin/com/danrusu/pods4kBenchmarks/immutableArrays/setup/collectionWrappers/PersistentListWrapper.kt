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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random

/** Represents a wrapper class that stores a single persistent list of the appropriate [DataType] */
sealed class PersistentListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer,
        ): PersistentListWrapper = when (dataType) {
            REFERENCE -> PersistentReferenceListWrapper(random, size, dataProducer)
            BOOLEAN -> PersistentBooleanListWrapper(random, size, dataProducer)
            BYTE -> PersistentByteListWrapper(random, size, dataProducer)
            CHAR -> PersistentCharListWrapper(random, size, dataProducer)
            SHORT -> PersistentShortListWrapper(random, size, dataProducer)
            INT -> PersistentIntListWrapper(random, size, dataProducer)
            FLOAT -> PersistentFloatListWrapper(random, size, dataProducer)
            LONG -> PersistentLongListWrapper(random, size, dataProducer)
            DOUBLE -> PersistentDoubleListWrapper(random, size, dataProducer)
        }
    }
}

class PersistentReferenceListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentReferenceList.size

    override val persistentReferenceList: PersistentList<String> =
        createPersistentList(size) { dataProducer.nextReference(it, random) }
}

class PersistentBooleanListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentBooleanList.size

    override val persistentBooleanList: PersistentList<Boolean> =
        createPersistentList(size) { dataProducer.nextBoolean(it, random) }
}

class PersistentByteListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentByteList.size

    override val persistentByteList: PersistentList<Byte> =
        createPersistentList(size) { dataProducer.nextByte(it, random) }
}

class PersistentCharListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentCharList.size

    override val persistentCharList: PersistentList<Char> =
        createPersistentList(size) { dataProducer.nextChar(it, random) }
}

class PersistentShortListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentShortList.size

    override val persistentShortList: PersistentList<Short> =
        createPersistentList(size) { dataProducer.nextShort(it, random) }
}

class PersistentIntListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentIntList.size

    override val persistentIntList: PersistentList<Int> =
        createPersistentList(size) { dataProducer.nextInt(it, random) }
}

class PersistentFloatListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentFloatList.size

    override val persistentFloatList: PersistentList<Float> =
        createPersistentList(size) { dataProducer.nextFloat(it, random) }
}

class PersistentLongListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentLongList.size

    override val persistentLongList: PersistentList<Long> =
        createPersistentList(size) { dataProducer.nextLong(it, random) }
}

class PersistentDoubleListWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = persistentDoubleList.size

    override val persistentDoubleList: PersistentList<Double> =
        createPersistentList(size) { dataProducer.nextDouble(it, random) }
}

private inline fun <T> createPersistentList(
    size: Int,
    crossinline initializer: (index: Int) -> T,
): PersistentList<T> {
    val builder = persistentListOf<T>().builder()
    repeat(size) { builder.add(initializer(it)) }
    return builder.build()
}

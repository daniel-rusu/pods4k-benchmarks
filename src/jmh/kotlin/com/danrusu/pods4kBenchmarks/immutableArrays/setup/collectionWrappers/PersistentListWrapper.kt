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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/** Represents a wrapper class that stores a single persistent list of the appropriate [DataType] */
sealed class PersistentListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            size: Int,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): PersistentListWrapper = when (dataType) {
            REFERENCE -> PersistentReferenceListWrapper(size, dataProducer)
            BOOLEAN -> PersistentBooleanListWrapper(size, dataProducer)
            BYTE -> PersistentByteListWrapper(size, dataProducer)
            CHAR -> PersistentCharListWrapper(size, dataProducer)
            SHORT -> PersistentShortListWrapper(size, dataProducer)
            INT -> PersistentIntListWrapper(size, dataProducer)
            FLOAT -> PersistentFloatListWrapper(size, dataProducer)
            LONG -> PersistentLongListWrapper(size, dataProducer)
            DOUBLE -> PersistentDoubleListWrapper(size, dataProducer)
        }

        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): Array<PersistentListWrapper> = when (dataType) {
            REFERENCE -> Array(count) { PersistentReferenceListWrapper(sizeDistribution.nextValue(), dataProducer) }
            BOOLEAN -> Array(count) { PersistentBooleanListWrapper(sizeDistribution.nextValue(), dataProducer) }
            BYTE -> Array(count) { PersistentByteListWrapper(sizeDistribution.nextValue(), dataProducer) }
            CHAR -> Array(count) { PersistentCharListWrapper(sizeDistribution.nextValue(), dataProducer) }
            SHORT -> Array(count) { PersistentShortListWrapper(sizeDistribution.nextValue(), dataProducer) }
            INT -> Array(count) { PersistentIntListWrapper(sizeDistribution.nextValue(), dataProducer) }
            FLOAT -> Array(count) { PersistentFloatListWrapper(sizeDistribution.nextValue(), dataProducer) }
            LONG -> Array(count) { PersistentLongListWrapper(sizeDistribution.nextValue(), dataProducer) }
            DOUBLE -> Array(count) { PersistentDoubleListWrapper(sizeDistribution.nextValue(), dataProducer) }
        }
    }
}

class PersistentReferenceListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentReferenceList: PersistentList<String> = createPersistentList(listSize) {
        dataProducer.nextReference()
    }
}

class PersistentBooleanListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentBooleanList: PersistentList<Boolean> = createPersistentList(listSize) {
        dataProducer.nextBoolean()
    }
}

class PersistentByteListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentByteList: PersistentList<Byte> = createPersistentList(listSize) {
        dataProducer.nextByte()
    }
}

class PersistentCharListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentCharList: PersistentList<Char> = createPersistentList(listSize) {
        dataProducer.nextChar()
    }
}

class PersistentShortListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentShortList: PersistentList<Short> = createPersistentList(listSize) {
        dataProducer.nextShort()
    }
}

class PersistentIntListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentIntList: PersistentList<Int> = createPersistentList(listSize) {
        dataProducer.nextInt()
    }
}

class PersistentFloatListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentFloatList: PersistentList<Float> = createPersistentList(listSize) {
        dataProducer.nextFloat()
    }
}

class PersistentLongListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentLongList: PersistentList<Long> = createPersistentList(listSize) {
        dataProducer.nextLong()
    }
}

class PersistentDoubleListWrapper(
    listSize: Int,
    dataProducer: FlatDataProducer,
) : PersistentListWrapper() {
    override val persistentDoubleList: PersistentList<Double> = createPersistentList(listSize) {
        dataProducer.nextDouble()
    }
}

private inline fun <T> createPersistentList(listSize: Int, initializer: () -> T): PersistentList<T> {
    val builder = persistentListOf<T>().builder()
    repeat(listSize) { builder.add(initializer()) }
    return builder.build()
}

package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.FlatDataProducer
import kotlin.random.Random

/** Represents a wrapper class that stores a single immutable array of the appropriate [DataType] */
sealed class ImmutableArrayWrapper : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer
        ): ImmutableArrayWrapper {
            dataProducer.startNewCollection(size)

            return when (dataType) {
                DataType.REFERENCE -> ImmutableReferenceArrayWrapper(random, size, dataProducer)
                DataType.BOOLEAN -> ImmutableBooleanArrayWrapper(random, size, dataProducer)
                DataType.BYTE -> ImmutableByteArrayWrapper(random, size, dataProducer)
                DataType.CHAR -> ImmutableCharArrayWrapper(random, size, dataProducer)
                DataType.SHORT -> ImmutableShortArrayWrapper(random, size, dataProducer)
                DataType.INT -> ImmutableIntArrayWrapper(random, size, dataProducer)
                DataType.FLOAT -> ImmutableFloatArrayWrapper(random, size, dataProducer)
                DataType.LONG -> ImmutableLongArrayWrapper(random, size, dataProducer)
                DataType.DOUBLE -> ImmutableDoubleArrayWrapper(random, size, dataProducer)
            }
        }
    }
}

class ImmutableReferenceArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableReferenceArray: ImmutableArray<String> =
        ImmutableArray(size) { dataProducer.nextReference(it, random) }
}

class ImmutableBooleanArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableBooleanArray: ImmutableBooleanArray =
        ImmutableBooleanArray(size) { dataProducer.nextBoolean(it, random) }
}

class ImmutableByteArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableByteArray: ImmutableByteArray =
        ImmutableByteArray(size) { dataProducer.nextByte(it, random) }
}

class ImmutableCharArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableCharArray: ImmutableCharArray =
        ImmutableCharArray(size) { dataProducer.nextChar(it, random) }
}

class ImmutableShortArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableShortArray: ImmutableShortArray =
        ImmutableShortArray(size) { dataProducer.nextShort(it, random) }
}

class ImmutableIntArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableIntArray: ImmutableIntArray = ImmutableIntArray(size) { dataProducer.nextInt(it, random) }
}

class ImmutableFloatArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableFloatArray: ImmutableFloatArray =
        ImmutableFloatArray(size) { dataProducer.nextFloat(it, random) }
}

class ImmutableLongArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableLongArray: ImmutableLongArray =
        ImmutableLongArray(size) { dataProducer.nextLong(it, random) }
}

class ImmutableDoubleArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableDoubleArray: ImmutableDoubleArray =
        ImmutableDoubleArray(size) { dataProducer.nextDouble(it, random) }
}

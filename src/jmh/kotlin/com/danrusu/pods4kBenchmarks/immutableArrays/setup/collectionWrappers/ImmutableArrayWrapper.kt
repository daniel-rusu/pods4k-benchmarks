package com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
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

/** Represents a wrapper class that stores a single immutable array of the appropriate [DataType] */
sealed class ImmutableArrayWrapper : CollectionWrapper() {
    companion object {
        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            dataProducer: FlatDataProducer,
        ): Array<ImmutableArrayWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ImmutableReferenceArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            BOOLEAN -> Array(count) { ImmutableBooleanArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            BYTE -> Array(count) { ImmutableByteArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            CHAR -> Array(count) { ImmutableCharArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            SHORT -> Array(count) { ImmutableShortArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            INT -> Array(count) { ImmutableIntArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            FLOAT -> Array(count) { ImmutableFloatArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            LONG -> Array(count) { ImmutableLongArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
            DOUBLE -> Array(count) { ImmutableDoubleArrayWrapper(sizeDistribution.nextValue(), dataProducer) }
        }
    }
}

class ImmutableReferenceArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableReferenceArray: ImmutableArray<String> = ImmutableArray(size) {
        dataProducer.nextReference()
    }
}

class ImmutableBooleanArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableBooleanArray: ImmutableBooleanArray = ImmutableBooleanArray(size) {
        dataProducer.nextBoolean()
    }
}

class ImmutableByteArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableByteArray: ImmutableByteArray = ImmutableByteArray(size) {
        dataProducer.nextByte()
    }
}

class ImmutableCharArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableCharArray: ImmutableCharArray = ImmutableCharArray(size) {
        dataProducer.nextChar()
    }
}

class ImmutableShortArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableShortArray: ImmutableShortArray = ImmutableShortArray(size) {
        dataProducer.nextShort()
    }
}

class ImmutableIntArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableIntArray: ImmutableIntArray = ImmutableIntArray(size) {
        dataProducer.nextInt()
    }
}

class ImmutableFloatArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableFloatArray: ImmutableFloatArray = ImmutableFloatArray(size) {
        dataProducer.nextFloat()
    }
}

class ImmutableLongArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableLongArray: ImmutableLongArray = ImmutableLongArray(size) {
        dataProducer.nextLong()
    }
}

class ImmutableDoubleArrayWrapper(
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    override val immutableDoubleArray: ImmutableDoubleArray = ImmutableDoubleArray(size) {
        dataProducer.nextDouble()
    }
}

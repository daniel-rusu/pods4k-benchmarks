package com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.benchmarkParameters.DataType.SHORT
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.dataProducers.FlatDataProducer
import kotlin.random.Random

/** Represents a wrapper class that stores a single immutable array of the appropriate [DataType] */
sealed class ImmutableArrayWrapper : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer
        ): ImmutableArrayWrapper = when (dataType) {
            REFERENCE -> ImmutableReferenceArrayWrapper(random, size, dataProducer)
            BOOLEAN -> ImmutableBooleanArrayWrapper(random, size, dataProducer)
            BYTE -> ImmutableByteArrayWrapper(random, size, dataProducer)
            CHAR -> ImmutableCharArrayWrapper(random, size, dataProducer)
            SHORT -> ImmutableShortArrayWrapper(random, size, dataProducer)
            INT -> ImmutableIntArrayWrapper(random, size, dataProducer)
            FLOAT -> ImmutableFloatArrayWrapper(random, size, dataProducer)
            LONG -> ImmutableLongArrayWrapper(random, size, dataProducer)
            DOUBLE -> ImmutableDoubleArrayWrapper(random, size, dataProducer)

        }
    }
}

class ImmutableReferenceArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableReferenceArray: ImmutableArray<String> =
        ImmutableArray(size) { dataProducer.nextReference(it, random) }
}

class ImmutableBooleanArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableBooleanArray: ImmutableBooleanArray =
        ImmutableBooleanArray(size) { dataProducer.nextBoolean(it, random) }
}

class ImmutableByteArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableByteArray: ImmutableByteArray =
        ImmutableByteArray(size) { dataProducer.nextByte(it, random) }
}

class ImmutableCharArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableCharArray: ImmutableCharArray =
        ImmutableCharArray(size) { dataProducer.nextChar(it, random) }
}

class ImmutableShortArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableShortArray: ImmutableShortArray =
        ImmutableShortArray(size) { dataProducer.nextShort(it, random) }
}

class ImmutableIntArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableIntArray: ImmutableIntArray = ImmutableIntArray(size) { dataProducer.nextInt(it, random) }
}

class ImmutableFloatArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableFloatArray: ImmutableFloatArray =
        ImmutableFloatArray(size) { dataProducer.nextFloat(it, random) }
}

class ImmutableLongArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableLongArray: ImmutableLongArray =
        ImmutableLongArray(size) { dataProducer.nextLong(it, random) }
}

class ImmutableDoubleArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ImmutableArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val immutableDoubleArray: ImmutableDoubleArray =
        ImmutableDoubleArray(size) { dataProducer.nextDouble(it, random) }
}

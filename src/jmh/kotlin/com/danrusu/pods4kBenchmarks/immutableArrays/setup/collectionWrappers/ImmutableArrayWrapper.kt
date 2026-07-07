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
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator

/** Represents a wrapper class that stores a single immutable array of the appropriate [DataType] */
sealed class ImmutableArrayWrapper : CollectionWrapper() {
    companion object {
        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<ImmutableArrayWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ImmutableReferenceArrayWrapper(sizeDistribution.nextValue(), references) }
            BOOLEAN -> Array(count) { ImmutableBooleanArrayWrapper(sizeDistribution.nextValue(), fields) }
            BYTE -> Array(count) { ImmutableByteArrayWrapper(sizeDistribution.nextValue(), fields) }
            CHAR -> Array(count) { ImmutableCharArrayWrapper(sizeDistribution.nextValue(), fields) }
            SHORT -> Array(count) { ImmutableShortArrayWrapper(sizeDistribution.nextValue(), fields) }
            INT -> Array(count) { ImmutableIntArrayWrapper(sizeDistribution.nextValue(), fields) }
            FLOAT -> Array(count) { ImmutableFloatArrayWrapper(sizeDistribution.nextValue(), fields) }
            LONG -> Array(count) { ImmutableLongArrayWrapper(sizeDistribution.nextValue(), fields) }
            DOUBLE -> Array(count) { ImmutableDoubleArrayWrapper(sizeDistribution.nextValue(), fields) }
        }
    }
}

class ImmutableReferenceArrayWrapper(
    size: Int,
    references: ObjectGenerator<String>,
) : ImmutableArrayWrapper() {
    override val immutableReferenceArray: ImmutableArray<String> = ImmutableArray(size) {
        references.next()
    }
}

class ImmutableBooleanArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableBooleanArray: ImmutableBooleanArray = ImmutableBooleanArray(size) {
        fields.nextBoolean()
    }
}

class ImmutableByteArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableByteArray: ImmutableByteArray = ImmutableByteArray(size) {
        fields.nextByte()
    }
}

class ImmutableCharArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableCharArray: ImmutableCharArray = ImmutableCharArray(size) {
        fields.nextChar()
    }
}

class ImmutableShortArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableShortArray: ImmutableShortArray = ImmutableShortArray(size) {
        fields.nextShort()
    }
}

class ImmutableIntArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableIntArray: ImmutableIntArray = ImmutableIntArray(size) {
        fields.nextInt()
    }
}

class ImmutableFloatArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableFloatArray: ImmutableFloatArray = ImmutableFloatArray(size) {
        fields.nextFloat()
    }
}

class ImmutableLongArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableLongArray: ImmutableLongArray = ImmutableLongArray(size) {
        fields.nextLong()
    }
}

class ImmutableDoubleArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ImmutableArrayWrapper() {
    override val immutableDoubleArray: ImmutableDoubleArray = ImmutableDoubleArray(size) {
        fields.nextDouble()
    }
}

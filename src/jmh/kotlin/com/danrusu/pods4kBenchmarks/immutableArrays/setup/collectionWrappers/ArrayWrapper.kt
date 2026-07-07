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
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator

/** Represents a wrapper class that stores a single array of the appropriate [DataType] */
sealed class ArrayWrapper : CollectionWrapper() {
    companion object {
        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<out ArrayWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ReferenceArrayWrapper(sizeDistribution.nextValue(), references) }
            BOOLEAN -> Array(count) { BooleanArrayWrapper(sizeDistribution.nextValue(), fields) }
            BYTE -> Array(count) { ByteArrayWrapper(sizeDistribution.nextValue(), fields) }
            CHAR -> Array(count) { CharArrayWrapper(sizeDistribution.nextValue(), fields) }
            SHORT -> Array(count) { ShortArrayWrapper(sizeDistribution.nextValue(), fields) }
            INT -> Array(count) { IntArrayWrapper(sizeDistribution.nextValue(), fields) }
            FLOAT -> Array(count) { FloatArrayWrapper(sizeDistribution.nextValue(), fields) }
            LONG -> Array(count) { LongArrayWrapper(sizeDistribution.nextValue(), fields) }
            DOUBLE -> Array(count) { DoubleArrayWrapper(sizeDistribution.nextValue(), fields) }
        }
    }
}

class ReferenceArrayWrapper(
    size: Int,
    references: ObjectGenerator<String>,
) : ArrayWrapper() {
    override val referenceArray: Array<String> = Array(size) { references.next() }
}

class BooleanArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val booleanArray: BooleanArray = BooleanArray(size) { fields.nextBoolean() }
}

class ByteArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val byteArray: ByteArray = ByteArray(size) { fields.nextByte() }
}

class CharArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val charArray: CharArray = CharArray(size) { fields.nextChar() }
}

class ShortArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val shortArray: ShortArray = ShortArray(size) { fields.nextShort() }
}

class IntArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val intArray: IntArray = IntArray(size) { fields.nextInt() }
}

class FloatArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val floatArray: FloatArray = FloatArray(size) { fields.nextFloat() }
}

class LongArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val longArray: LongArray = LongArray(size) { fields.nextLong() }
}

class DoubleArrayWrapper(
    size: Int,
    fields: FieldGenerator,
) : ArrayWrapper() {
    override val doubleArray: DoubleArray = DoubleArray(size) { fields.nextDouble() }
}

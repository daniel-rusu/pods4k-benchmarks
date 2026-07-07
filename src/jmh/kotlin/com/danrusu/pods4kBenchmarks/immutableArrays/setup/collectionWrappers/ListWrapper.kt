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
sealed class ListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            size: Int,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): ListWrapper = when (dataType) {
            REFERENCE -> ReferenceListWrapper(size, references)
            BOOLEAN -> BooleanListWrapper(size, fields)
            BYTE -> ByteListWrapper(size, fields)
            CHAR -> CharListWrapper(size, fields)
            SHORT -> ShortListWrapper(size, fields)
            INT -> IntListWrapper(size, fields)
            FLOAT -> FloatListWrapper(size, fields)
            LONG -> LongListWrapper(size, fields)
            DOUBLE -> DoubleListWrapper(size, fields)
        }

        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<ListWrapper> = when (dataType) {
            REFERENCE -> Array(count) { ReferenceListWrapper(sizeDistribution.nextValue(), references) }
            BOOLEAN -> Array(count) { BooleanListWrapper(sizeDistribution.nextValue(), fields) }
            BYTE -> Array(count) { ByteListWrapper(sizeDistribution.nextValue(), fields) }
            CHAR -> Array(count) { CharListWrapper(sizeDistribution.nextValue(), fields) }
            SHORT -> Array(count) { ShortListWrapper(sizeDistribution.nextValue(), fields) }
            INT -> Array(count) { IntListWrapper(sizeDistribution.nextValue(), fields) }
            FLOAT -> Array(count) { FloatListWrapper(sizeDistribution.nextValue(), fields) }
            LONG -> Array(count) { LongListWrapper(sizeDistribution.nextValue(), fields) }
            DOUBLE -> Array(count) { DoubleListWrapper(sizeDistribution.nextValue(), fields) }
        }
    }
}

class ReferenceListWrapper(
    listSize: Int,
    references: ObjectGenerator<String>,
) : ListWrapper() {
    override val referenceList: List<String> = createList(listSize) { references.next() }
}

class BooleanListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val booleanList: List<Boolean> = createList(listSize) { fields.nextBoolean() }
}

class ByteListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val byteList: List<Byte> = createList(listSize) { fields.nextByte() }
}

class CharListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val charList: List<Char> = createList(listSize) { fields.nextChar() }
}

class ShortListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val shortList: List<Short> = createList(listSize) { fields.nextShort() }
}

class IntListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val intList: List<Int> = createList(listSize) { fields.nextInt() }
}

class FloatListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val floatList: List<Float> = createList(listSize) { fields.nextFloat() }
}

class LongListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val longList: List<Long> = createList(listSize) { fields.nextLong() }
}

class DoubleListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : ListWrapper() {
    override val doubleList: List<Double> = createList(listSize) { fields.nextDouble() }
}

private inline fun <T> createList(listSize: Int, initializer: () -> T): List<T> {
    val result = ArrayList<T>(listSize)
    repeat(listSize) { result.add(initializer()) }
    return result
}

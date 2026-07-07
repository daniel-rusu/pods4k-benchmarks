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
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

/** Represents a wrapper class that stores a single persistent list of the appropriate [DataType] */
sealed class PersistentListWrapper : CollectionWrapper() {
    companion object {
        fun create(
            size: Int,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): PersistentListWrapper = when (dataType) {
            REFERENCE -> PersistentReferenceListWrapper(size, references)
            BOOLEAN -> PersistentBooleanListWrapper(size, fields)
            BYTE -> PersistentByteListWrapper(size, fields)
            CHAR -> PersistentCharListWrapper(size, fields)
            SHORT -> PersistentShortListWrapper(size, fields)
            INT -> PersistentIntListWrapper(size, fields)
            FLOAT -> PersistentFloatListWrapper(size, fields)
            LONG -> PersistentLongListWrapper(size, fields)
            DOUBLE -> PersistentDoubleListWrapper(size, fields)
        }

        fun createWrappers(
            count: Int,
            sizeDistribution: Distribution,
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String>,
        ): Array<PersistentListWrapper> = when (dataType) {
            REFERENCE -> Array(count) { PersistentReferenceListWrapper(sizeDistribution.nextValue(), references) }
            BOOLEAN -> Array(count) { PersistentBooleanListWrapper(sizeDistribution.nextValue(), fields) }
            BYTE -> Array(count) { PersistentByteListWrapper(sizeDistribution.nextValue(), fields) }
            CHAR -> Array(count) { PersistentCharListWrapper(sizeDistribution.nextValue(), fields) }
            SHORT -> Array(count) { PersistentShortListWrapper(sizeDistribution.nextValue(), fields) }
            INT -> Array(count) { PersistentIntListWrapper(sizeDistribution.nextValue(), fields) }
            FLOAT -> Array(count) { PersistentFloatListWrapper(sizeDistribution.nextValue(), fields) }
            LONG -> Array(count) { PersistentLongListWrapper(sizeDistribution.nextValue(), fields) }
            DOUBLE -> Array(count) { PersistentDoubleListWrapper(sizeDistribution.nextValue(), fields) }
        }
    }
}

class PersistentReferenceListWrapper(
    listSize: Int,
    references: ObjectGenerator<String>,
) : PersistentListWrapper() {
    override val persistentReferenceList: PersistentList<String> = createPersistentList(listSize) {
        references.next()
    }
}

class PersistentBooleanListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentBooleanList: PersistentList<Boolean> = createPersistentList(listSize) {
        fields.nextBoolean()
    }
}

class PersistentByteListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentByteList: PersistentList<Byte> = createPersistentList(listSize) {
        fields.nextByte()
    }
}

class PersistentCharListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentCharList: PersistentList<Char> = createPersistentList(listSize) {
        fields.nextChar()
    }
}

class PersistentShortListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentShortList: PersistentList<Short> = createPersistentList(listSize) {
        fields.nextShort()
    }
}

class PersistentIntListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentIntList: PersistentList<Int> = createPersistentList(listSize) {
        fields.nextInt()
    }
}

class PersistentFloatListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentFloatList: PersistentList<Float> = createPersistentList(listSize) {
        fields.nextFloat()
    }
}

class PersistentLongListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentLongList: PersistentList<Long> = createPersistentList(listSize) {
        fields.nextLong()
    }
}

class PersistentDoubleListWrapper(
    listSize: Int,
    fields: FieldGenerator,
) : PersistentListWrapper() {
    override val persistentDoubleList: PersistentList<Double> = createPersistentList(listSize) {
        fields.nextDouble()
    }
}

private inline fun <T> createPersistentList(listSize: Int, initializer: () -> T): PersistentList<T> {
    val builder = persistentListOf<T>().builder()
    repeat(listSize) { builder.add(initializer()) }
    return builder.build()
}

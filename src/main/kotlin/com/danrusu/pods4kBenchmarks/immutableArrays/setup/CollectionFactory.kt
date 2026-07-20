package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object CollectionFactory {
    fun <T> createList(
        size: Int,
        dataType: DataType,
        fields: FieldGenerator,
        references: ObjectGenerator<T>
    ): ArrayList<*> = when (dataType) {
        DataType.REFERENCE -> createList(size) { references.next() }
        DataType.BOOLEAN -> createList(size) { fields.nextBoolean() }
        DataType.BYTE -> createList(size) { fields.nextByte() }
        DataType.CHAR -> createList(size) { fields.nextChar() }
        DataType.SHORT -> createList(size) { fields.nextShort() }
        DataType.INT -> createList(size) { fields.nextInt() }
        DataType.FLOAT -> createList(size) { fields.nextFloat() }
        DataType.LONG -> createList(size) { fields.nextLong() }
        DataType.DOUBLE -> createList(size) { fields.nextDouble() }
    }

    inline fun <T> createList(size: Int, crossinline initializer: () -> T): ArrayList<T> {
        val result = ArrayList<T>(size)
        repeat(size) { result.add(initializer()) }
        return result
    }

    fun <T> createPersistentList(
        size: Int,
        dataType: DataType,
        fields: FieldGenerator,
        references: ObjectGenerator<T>
    ): PersistentList<*> = when (dataType) {
        DataType.REFERENCE -> createPersistentList(size) { references.next() }
        DataType.BOOLEAN -> createPersistentList(size) { fields.nextBoolean() }
        DataType.BYTE -> createPersistentList(size) { fields.nextByte() }
        DataType.CHAR -> createPersistentList(size) { fields.nextChar() }
        DataType.SHORT -> createPersistentList(size) { fields.nextShort() }
        DataType.INT -> createPersistentList(size) { fields.nextInt() }
        DataType.FLOAT -> createPersistentList(size) { fields.nextFloat() }
        DataType.LONG -> createPersistentList(size) { fields.nextLong() }
        DataType.DOUBLE -> createPersistentList(size) { fields.nextDouble() }
    }

    inline fun <T> createPersistentList(size: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(size) { builder.add(initializer()) }
        return builder.build()
    }

    fun <R> createImmutableArray(
        size: Int,
        dataType: DataType,
        fields: FieldGenerator,
        references: ObjectGenerator<R>
    ): Any = when (dataType) {
        DataType.REFERENCE -> ImmutableArray(size) { references.next() }
        DataType.BOOLEAN -> ImmutableBooleanArray(size) { fields.nextBoolean() }
        DataType.BYTE -> ImmutableByteArray(size) { fields.nextByte() }
        DataType.CHAR -> ImmutableCharArray(size) { fields.nextChar() }
        DataType.SHORT -> ImmutableShortArray(size) { fields.nextShort() }
        DataType.INT -> ImmutableIntArray(size) { fields.nextInt() }
        DataType.FLOAT -> ImmutableFloatArray(size) { fields.nextFloat() }
        DataType.LONG -> ImmutableLongArray(size) { fields.nextLong() }
        DataType.DOUBLE -> ImmutableDoubleArray(size) { fields.nextDouble() }
    }

    fun getCollectionClass(collectionType: CollectionType, dataType: DataType): Class<*>? = when (collectionType) {
        CollectionType.LIST -> ArrayList::class.java
        CollectionType.PERSISTENT_LIST -> PersistentList::class.java
        CollectionType.ARRAY -> when (dataType) {
            DataType.REFERENCE -> Array::class.java
            DataType.BOOLEAN -> BooleanArray::class.java
            DataType.BYTE -> ByteArray::class.java
            DataType.CHAR -> CharArray::class.java
            DataType.SHORT -> ShortArray::class.java
            DataType.INT -> IntArray::class.java
            DataType.FLOAT -> FloatArray::class.java
            DataType.LONG -> LongArray::class.java
            DataType.DOUBLE -> DoubleArray::class.java
        }

        CollectionType.IMMUTABLE_ARRAY -> when (dataType) {
            DataType.REFERENCE -> ImmutableArray::class.java
            DataType.BOOLEAN -> ImmutableBooleanArray::class.java
            DataType.BYTE -> ImmutableByteArray::class.java
            DataType.CHAR -> ImmutableCharArray::class.java
            DataType.SHORT -> ImmutableShortArray::class.java
            DataType.INT -> ImmutableIntArray::class.java
            DataType.FLOAT -> ImmutableFloatArray::class.java
            DataType.LONG -> ImmutableLongArray::class.java
            DataType.DOUBLE -> ImmutableDoubleArray::class.java
        }
    }
}

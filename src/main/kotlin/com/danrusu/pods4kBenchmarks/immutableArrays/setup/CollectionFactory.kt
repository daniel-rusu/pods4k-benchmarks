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
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object CollectionFactory {
    fun <T> createCollection(
        size: Int,
        collectionType: CollectionType,
        dataType: DataType,
        fieldGenerator: FieldGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (collectionType) {
        CollectionType.LIST -> createList(size, dataType, fieldGenerator, referenceGenerator)
        CollectionType.PERSISTENT_LIST -> createPersistentList(size, dataType, fieldGenerator, referenceGenerator)
        CollectionType.ARRAY -> createArray(size, dataType, fieldGenerator, referenceGenerator)
        CollectionType.IMMUTABLE_ARRAY -> createImmutableArray(size, dataType, fieldGenerator, referenceGenerator)
    }

    fun <T> createCollection(
        size: Int,
        collectionType: CollectionType,
        elementClass: Class<T & Any>,
        initializer: () -> T,
    ): Any = when (collectionType) {
        CollectionType.LIST -> createList(size) { initializer() }
        CollectionType.PERSISTENT_LIST -> createPersistentList(size) { initializer() }
        CollectionType.ARRAY -> ArrayCreator.createArray(elementClass, size) { initializer() }
        CollectionType.IMMUTABLE_ARRAY -> ImmutableArray(size) { initializer() }
    }

    fun <T> createList(
        size: Int,
        dataType: DataType,
        fieldGenerator: FieldGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): List<*> = when (dataType) {
        DataType.REFERENCE -> createList(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> createList(size) { fieldGenerator.nextBoolean() }
        DataType.BYTE -> createList(size) { fieldGenerator.nextByte() }
        DataType.CHAR -> createList(size) { fieldGenerator.nextChar() }
        DataType.SHORT -> createList(size) { fieldGenerator.nextShort() }
        DataType.INT -> createList(size) { fieldGenerator.nextInt() }
        DataType.FLOAT -> createList(size) { fieldGenerator.nextFloat() }
        DataType.LONG -> createList(size) { fieldGenerator.nextLong() }
        DataType.DOUBLE -> createList(size) { fieldGenerator.nextDouble() }
    }

    inline fun <T> createList(size: Int, crossinline initializer: () -> T): List<T> {
        val result = ArrayList<T>(size)
        repeat(size) { result.add(initializer()) }
        return result
    }

    fun <T> createPersistentList(
        size: Int,
        dataType: DataType,
        fieldGenerator: FieldGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): PersistentList<*> = when (dataType) {
        DataType.REFERENCE -> createPersistentList(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> createPersistentList(size) { fieldGenerator.nextBoolean() }
        DataType.BYTE -> createPersistentList(size) { fieldGenerator.nextByte() }
        DataType.CHAR -> createPersistentList(size) { fieldGenerator.nextChar() }
        DataType.SHORT -> createPersistentList(size) { fieldGenerator.nextShort() }
        DataType.INT -> createPersistentList(size) { fieldGenerator.nextInt() }
        DataType.FLOAT -> createPersistentList(size) { fieldGenerator.nextFloat() }
        DataType.LONG -> createPersistentList(size) { fieldGenerator.nextLong() }
        DataType.DOUBLE -> createPersistentList(size) { fieldGenerator.nextDouble() }
    }

    inline fun <T> createPersistentList(size: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(size) { builder.add(initializer()) }
        return builder.build()
    }

    fun <T> createArray(
        size: Int,
        dataType: DataType,
        fieldGenerator: FieldGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (dataType) {
        DataType.REFERENCE -> ArrayCreator.createArray(referenceGenerator.objectClass, size) { referenceGenerator.next() }
        DataType.BOOLEAN -> BooleanArray(size) { fieldGenerator.nextBoolean() }
        DataType.BYTE -> ByteArray(size) { fieldGenerator.nextByte() }
        DataType.CHAR -> CharArray(size) { fieldGenerator.nextChar() }
        DataType.SHORT -> ShortArray(size) { fieldGenerator.nextShort() }
        DataType.INT -> IntArray(size) { fieldGenerator.nextInt() }
        DataType.FLOAT -> FloatArray(size) { fieldGenerator.nextFloat() }
        DataType.LONG -> LongArray(size) { fieldGenerator.nextLong() }
        DataType.DOUBLE -> DoubleArray(size) { fieldGenerator.nextDouble() }
    }

    fun <T> createImmutableArray(
        size: Int,
        dataType: DataType,
        fieldGenerator: FieldGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (dataType) {
        DataType.REFERENCE -> ImmutableArray(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> ImmutableBooleanArray(size) { fieldGenerator.nextBoolean() }
        DataType.BYTE -> ImmutableByteArray(size) { fieldGenerator.nextByte() }
        DataType.CHAR -> ImmutableCharArray(size) { fieldGenerator.nextChar() }
        DataType.SHORT -> ImmutableShortArray(size) { fieldGenerator.nextShort() }
        DataType.INT -> ImmutableIntArray(size) { fieldGenerator.nextInt() }
        DataType.FLOAT -> ImmutableFloatArray(size) { fieldGenerator.nextFloat() }
        DataType.LONG -> ImmutableLongArray(size) { fieldGenerator.nextLong() }
        DataType.DOUBLE -> ImmutableDoubleArray(size) { fieldGenerator.nextDouble() }
    }

    fun resolveCollectionClass(
        collectionType: CollectionType,
        dataType: DataType,
        referenceElementClass: Class<*>,
    ): Class<*> = when (collectionType) {
        CollectionType.LIST -> List::class.java
        CollectionType.PERSISTENT_LIST -> PersistentList::class.java
        CollectionType.ARRAY -> when (dataType) {
            DataType.REFERENCE -> referenceElementClass.arrayType()
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

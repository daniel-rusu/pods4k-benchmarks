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
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGenerator
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object CollectionFactory {
    fun <T> createCollection(
        size: Int,
        collectionType: CollectionType,
        dataType: DataType,
        primitiveGenerator: PrimitiveGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (collectionType) {
        CollectionType.LIST -> createList(size, dataType, primitiveGenerator, referenceGenerator)
        CollectionType.PERSISTENT_LIST -> createPersistentList(size, dataType, primitiveGenerator, referenceGenerator)
        CollectionType.ARRAY -> createArray(size, dataType, primitiveGenerator, referenceGenerator)
        CollectionType.IMMUTABLE_ARRAY -> createImmutableArray(size, dataType, primitiveGenerator, referenceGenerator)
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
        primitiveGenerator: PrimitiveGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): List<*> = when (dataType) {
        DataType.REFERENCE -> createList(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> createList(size) { primitiveGenerator.nextBoolean() }
        DataType.BYTE -> createList(size) { primitiveGenerator.nextByte() }
        DataType.CHAR -> createList(size) { primitiveGenerator.nextChar() }
        DataType.SHORT -> createList(size) { primitiveGenerator.nextShort() }
        DataType.INT -> createList(size) { primitiveGenerator.nextInt() }
        DataType.FLOAT -> createList(size) { primitiveGenerator.nextFloat() }
        DataType.LONG -> createList(size) { primitiveGenerator.nextLong() }
        DataType.DOUBLE -> createList(size) { primitiveGenerator.nextDouble() }
    }

    inline fun <T> createList(size: Int, crossinline initializer: () -> T): List<T> {
        val result = ArrayList<T>(size)
        repeat(size) { result.add(initializer()) }
        return result
    }

    fun <T> createPersistentList(
        size: Int,
        dataType: DataType,
        primitiveGenerator: PrimitiveGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): PersistentList<*> = when (dataType) {
        DataType.REFERENCE -> createPersistentList(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> createPersistentList(size) { primitiveGenerator.nextBoolean() }
        DataType.BYTE -> createPersistentList(size) { primitiveGenerator.nextByte() }
        DataType.CHAR -> createPersistentList(size) { primitiveGenerator.nextChar() }
        DataType.SHORT -> createPersistentList(size) { primitiveGenerator.nextShort() }
        DataType.INT -> createPersistentList(size) { primitiveGenerator.nextInt() }
        DataType.FLOAT -> createPersistentList(size) { primitiveGenerator.nextFloat() }
        DataType.LONG -> createPersistentList(size) { primitiveGenerator.nextLong() }
        DataType.DOUBLE -> createPersistentList(size) { primitiveGenerator.nextDouble() }
    }

    inline fun <T> createPersistentList(size: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(size) { builder.add(initializer()) }
        return builder.build()
    }

    fun <T> createArray(
        size: Int,
        dataType: DataType,
        primitiveGenerator: PrimitiveGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (dataType) {
        DataType.REFERENCE -> ArrayCreator.createArray(referenceGenerator.objectClass, size) { referenceGenerator.next() }
        DataType.BOOLEAN -> BooleanArray(size) { primitiveGenerator.nextBoolean() }
        DataType.BYTE -> ByteArray(size) { primitiveGenerator.nextByte() }
        DataType.CHAR -> CharArray(size) { primitiveGenerator.nextChar() }
        DataType.SHORT -> ShortArray(size) { primitiveGenerator.nextShort() }
        DataType.INT -> IntArray(size) { primitiveGenerator.nextInt() }
        DataType.FLOAT -> FloatArray(size) { primitiveGenerator.nextFloat() }
        DataType.LONG -> LongArray(size) { primitiveGenerator.nextLong() }
        DataType.DOUBLE -> DoubleArray(size) { primitiveGenerator.nextDouble() }
    }

    fun <T> createImmutableArray(
        size: Int,
        dataType: DataType,
        primitiveGenerator: PrimitiveGenerator,
        referenceGenerator: ObjectGenerator<T>,
    ): Any = when (dataType) {
        DataType.REFERENCE -> ImmutableArray(size) { referenceGenerator.next() }
        DataType.BOOLEAN -> ImmutableBooleanArray(size) { primitiveGenerator.nextBoolean() }
        DataType.BYTE -> ImmutableByteArray(size) { primitiveGenerator.nextByte() }
        DataType.CHAR -> ImmutableCharArray(size) { primitiveGenerator.nextChar() }
        DataType.SHORT -> ImmutableShortArray(size) { primitiveGenerator.nextShort() }
        DataType.INT -> ImmutableIntArray(size) { primitiveGenerator.nextInt() }
        DataType.FLOAT -> ImmutableFloatArray(size) { primitiveGenerator.nextFloat() }
        DataType.LONG -> ImmutableLongArray(size) { primitiveGenerator.nextLong() }
        DataType.DOUBLE -> ImmutableDoubleArray(size) { primitiveGenerator.nextDouble() }
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

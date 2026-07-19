package com.danrusu.pods4kBenchmarks.immutableArrays.setup

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
}

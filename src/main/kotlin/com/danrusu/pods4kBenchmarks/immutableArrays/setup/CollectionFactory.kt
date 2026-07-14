package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

object CollectionFactory {
    inline fun <T> createList(size: Int, crossinline initializer: () -> T): ArrayList<T> {
        val result = ArrayList<T>(size)
        repeat(size) { result.add(initializer()) }
        return result
    }

    inline fun <T> createPersistentList(size: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(size) { builder.add(initializer()) }
        return builder.build()
    }
}

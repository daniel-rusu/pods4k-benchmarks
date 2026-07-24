package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message

class CollectionBatchTest {
    @Test
    fun `returns collections using the requested representation and element type`() {
        val collections = arrayOf(arrayListOf(1, 2), arrayListOf(3))
        val batch = CollectionBatch(
            collectionType = CollectionType.LIST,
            logicalElementClass = Int::class.javaObjectType,
            collections = collections,
        )

        expectThat(
            batch.getCollections<ArrayList<Int>>(
                expectedCollectionType = CollectionType.LIST,
                expectedLogicalElementClass = Int::class.javaObjectType,
            )
        )
            .isEqualTo(collections)
    }

    @Test
    fun `rejects the wrong collection representation with a descriptive error`() {
        val batch = CollectionBatch(
            collectionType = CollectionType.LIST,
            logicalElementClass = Int::class.javaObjectType,
            collections = arrayOf(arrayListOf(1)),
        )

        expectThrows<IllegalStateException> {
            batch.getCollections<ArrayList<Int>>(
                expectedCollectionType = CollectionType.PERSISTENT_LIST,
                expectedLogicalElementClass = Int::class.javaObjectType,
            )
        }.message.isEqualTo("Requested PERSISTENT_LIST data, but the batch contains LIST data")
    }

    @Test
    fun `rejects the wrong element type with a descriptive error`() {
        val batch = CollectionBatch(
            collectionType = CollectionType.LIST,
            logicalElementClass = Int::class.javaObjectType,
            collections = arrayOf(arrayListOf(1)),
        )

        expectThrows<IllegalStateException> {
            batch.getCollections<ArrayList<Long>>(
                expectedCollectionType = CollectionType.LIST,
                expectedLogicalElementClass = Long::class.javaObjectType,
            )
        }.message.isEqualTo(
            "Requested logical element class java.lang.Long, but the batch contains java.lang.Integer"
        )
    }
}

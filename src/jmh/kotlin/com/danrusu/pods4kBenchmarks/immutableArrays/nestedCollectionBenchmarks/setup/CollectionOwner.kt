package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

/**
 * Models an object that owns a nested collection, such as a person with friends or an order with products.
 */
class CollectionOwner<out C>(
    val nestedCollection: C,
)

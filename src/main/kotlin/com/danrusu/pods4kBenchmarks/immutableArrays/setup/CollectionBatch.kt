package com.danrusu.pods4kBenchmarks.immutableArrays.setup

/**
 * One materialized batch of collections for a benchmark trial.
 *
 * [logicalElementClass] identifies the values operated on by the benchmark. For flat collections this is the immediate
 * collection element class. For nested collections it is the innermost value class beneath the owner and nested
 * collection; for example, it is `Boolean` for `List<CollectionOwner<List<Boolean>>>`.
 *
 * [getCollections] validates [collectionType] and the requested logical element class before casting. The outer array's
 * component type provides a final guard against incompatible representation casts.
 */
@PublishedApi
internal class CollectionBatch(
    private val collectionType: CollectionType,
    private val logicalElementClass: Class<*>,
    private val collections: Array<*>,
) {
    /** Returns the stored collections after validating the representation and logical element class. */
    @PublishedApi
    internal fun <C> getCollections(
        expectedCollectionType: CollectionType,
        expectedLogicalElementClass: Class<*>,
    ): Array<C> {
        check(collectionType == expectedCollectionType) {
            "Requested $expectedCollectionType data, but the batch contains $collectionType data"
        }

        check(logicalElementClass === expectedLogicalElementClass) {
            "Requested logical element class ${expectedLogicalElementClass.name}, " +
                    "but the batch contains ${this.logicalElementClass.name}"
        }

        @Suppress("UNCHECKED_CAST")
        return collections as Array<C>
    }
}

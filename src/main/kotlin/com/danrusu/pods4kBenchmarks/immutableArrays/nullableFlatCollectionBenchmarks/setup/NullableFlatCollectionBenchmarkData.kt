package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBatch
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.resolveElementClass
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized nullable collections for one [CollectionType]/[DataType] trial.
 *
 * Nullable primitive values use boxed representations, so all data types share the same four reference-collection
 * accessors. Each accessor's type parameter is the non-null type underlying the returned nullable elements.
 */
class NullableFlatCollectionBenchmarkData private constructor(
    @PublishedApi internal val batch: CollectionBatch,
) {
    inline fun <reified T : Any> lists(): Array<List<T?>> {
        return batch.getCollections(CollectionType.LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> persistentLists(): Array<PersistentList<T?>> {
        return batch.getCollections(CollectionType.PERSISTENT_LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> arrays(): Array<Array<T?>> {
        return batch.getCollections(CollectionType.ARRAY, T::class.javaObjectType)
    }

    inline fun <reified T : Any> immutableArrays(): Array<ImmutableArray<T?>> {
        return batch.getCollections(CollectionType.IMMUTABLE_ARRAY, T::class.javaObjectType)
    }

    companion object {
        /** Materializes data from the supplied recipe for one flat benchmark parameter combination. */
        @Suppress("UNCHECKED_CAST")
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            recipe: NullableFlatCollectionRecipe,
        ): NullableFlatCollectionBenchmarkData {
            val primitiveGenerator = recipe.primitiveGenerator
            val referenceGenerator = recipe.referenceGenerator
            val nullabilityPolicy = recipe.nullabilityPolicy
            val elementClass = dataType.resolveElementClass(referenceGenerator.objectClass) as Class<Any>
            val collectionClass = CollectionFactory.resolveCollectionClass(
                collectionType = collectionType,
                dataType = DataType.REFERENCE,
                referenceElementClass = elementClass,
            )

            val batch = CollectionBatch.create(
                collectionType = collectionType,
                logicalElementClass = elementClass,
                collectionClass = collectionClass,
                numCollections = numCollections,
                sizeDistribution = recipe.sizeDistribution,
            ) { size ->
                CollectionFactory.createCollection(size, collectionType, elementClass) {
                    when (dataType) {
                        DataType.REFERENCE -> nullabilityPolicy.nullable { referenceGenerator.next() }
                        DataType.BOOLEAN -> nullabilityPolicy.nullable { primitiveGenerator.nextBoolean() }
                        DataType.BYTE -> nullabilityPolicy.nullable { primitiveGenerator.nextByte() }
                        DataType.CHAR -> nullabilityPolicy.nullable { primitiveGenerator.nextChar() }
                        DataType.SHORT -> nullabilityPolicy.nullable { primitiveGenerator.nextShort() }
                        DataType.INT -> nullabilityPolicy.nullable { primitiveGenerator.nextInt() }
                        DataType.FLOAT -> nullabilityPolicy.nullable { primitiveGenerator.nextFloat() }
                        DataType.LONG -> nullabilityPolicy.nullable { primitiveGenerator.nextLong() }
                        DataType.DOUBLE -> nullabilityPolicy.nullable { primitiveGenerator.nextDouble() }
                    }
                }
            }
            return NullableFlatCollectionBenchmarkData(batch)
        }
    }
}

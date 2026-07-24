package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBatch
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.resolveElementClass
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
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
    inline fun <reified T : Any> listData(): Array<ArrayList<T?>> {
        return batch.getCollections(CollectionType.LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> persistentListData(): Array<PersistentList<T?>> {
        return batch.getCollections(CollectionType.PERSISTENT_LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> arrayData(): Array<Array<T?>> {
        return batch.getCollections(CollectionType.ARRAY, T::class.javaObjectType)
    }

    inline fun <reified T : Any> immutableArrayData(): Array<ImmutableArray<T?>> {
        return batch.getCollections(CollectionType.IMMUTABLE_ARRAY, T::class.javaObjectType)
    }

    companion object {
        /** Creates deterministic nullable data for one flat benchmark parameter combination. */
        @Suppress("UNCHECKED_CAST")
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            fieldGeneratorFactory: FieldGeneratorFactory,
            referenceGeneratorFactory: ObjectGeneratorFactory<String?>,
        ): NullableFlatCollectionBenchmarkData {
            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val fields = fieldGeneratorFactory.create(generatorRngs)
            val references = referenceGeneratorFactory.create(generatorRngs)
            val elementClass = dataType.resolveElementClass(references.objectClass) as Class<Any>
            val collectionClass = CollectionFactory.getCollectionClass(
                collectionType,
                DataType.REFERENCE, // All collections will store references because the primitive values are boxed
                elementClass,
            )

            return NullableFlatCollectionBenchmarkData(
                batch = CollectionBatch.create(
                    collectionType,
                    elementClass,
                    collectionClass,
                    numCollections,
                    sizeDistribution
                ) { size ->
                    CollectionFactory.createCollection(size, collectionType, elementClass) {
                        when (dataType) {
                            DataType.REFERENCE -> references.next()
                            DataType.BOOLEAN -> fields.nextNullableBoolean()
                            DataType.BYTE -> fields.nextNullableByte()
                            DataType.CHAR -> fields.nextNullableChar()
                            DataType.SHORT -> fields.nextNullableShort()
                            DataType.INT -> fields.nextNullableInt()
                            DataType.FLOAT -> fields.nextNullableFloat()
                            DataType.LONG -> fields.nextNullableLong()
                            DataType.DOUBLE -> fields.nextNullableDouble()
                        }
                    }
                },
            )
        }
    }
}

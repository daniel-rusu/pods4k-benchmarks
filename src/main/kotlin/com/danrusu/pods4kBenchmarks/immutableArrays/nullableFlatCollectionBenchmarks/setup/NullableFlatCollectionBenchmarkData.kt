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
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
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
            val fieldGenerator = fieldGeneratorFactory.create(generatorRngs)
            val referenceGenerator = referenceGeneratorFactory.create(generatorRngs)
            val elementClass = dataType.resolveElementClass(referenceGenerator.objectClass) as Class<Any>
            val collectionClass = CollectionFactory.resolveCollectionClass(
                collectionType = collectionType,
                dataType = DataType.REFERENCE,
                referenceElementClass = elementClass,
            )

            return NullableFlatCollectionBenchmarkData(
                batch = CollectionBatch.create(
                    collectionType = collectionType,
                    logicalElementClass = elementClass,
                    collectionClass = collectionClass,
                    numCollections = numCollections,
                    sizeDistribution = sizeDistribution,
                ) { size ->
                    CollectionFactory.createCollection(size, collectionType, elementClass) {
                        when (dataType) {
                            DataType.REFERENCE -> referenceGenerator.next()
                            DataType.BOOLEAN -> fieldGenerator.nextNullableBoolean()
                            DataType.BYTE -> fieldGenerator.nextNullableByte()
                            DataType.CHAR -> fieldGenerator.nextNullableChar()
                            DataType.SHORT -> fieldGenerator.nextNullableShort()
                            DataType.INT -> fieldGenerator.nextNullableInt()
                            DataType.FLOAT -> fieldGenerator.nextNullableFloat()
                            DataType.LONG -> fieldGenerator.nextNullableLong()
                            DataType.DOUBLE -> fieldGenerator.nextNullableDouble()
                        }
                    }
                },
            )
        }
    }
}

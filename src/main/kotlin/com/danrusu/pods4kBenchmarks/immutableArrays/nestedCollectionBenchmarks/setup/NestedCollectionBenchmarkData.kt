package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
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
 * Materialized parent and nested collections for one [CollectionType]/[DataType] trial.
 *
 * Every parent collection contains [CollectionOwner] values, and each owner contains a nested collection. The parent and
 * nested collections use the same [CollectionType]. Accessor type parameters identify the innermost [DataType] values,
 * not the intermediate owner or collection types.
 */
class NestedCollectionBenchmarkData private constructor(
    @PublishedApi internal val batch: CollectionBatch,
) {
    inline fun <reified T : Any> lists(): Array<List<CollectionOwner<List<T>>>> {
        return batch.getCollections(CollectionType.LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> persistentLists(): Array<PersistentList<CollectionOwner<PersistentList<T>>>> {
        return batch.getCollections(CollectionType.PERSISTENT_LIST, T::class.javaObjectType)
    }

    val referenceArrays: Array<Array<CollectionOwner<Array<String>>>>
        get() = batch.getCollections(CollectionType.ARRAY, String::class.java)

    val booleanArrays: Array<Array<CollectionOwner<BooleanArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Boolean::class.javaObjectType)

    val byteArrays: Array<Array<CollectionOwner<ByteArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Byte::class.javaObjectType)

    val charArrays: Array<Array<CollectionOwner<CharArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Char::class.javaObjectType)

    val shortArrays: Array<Array<CollectionOwner<ShortArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Short::class.javaObjectType)

    val intArrays: Array<Array<CollectionOwner<IntArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Int::class.javaObjectType)

    val floatArrays: Array<Array<CollectionOwner<FloatArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Float::class.javaObjectType)

    val longArrays: Array<Array<CollectionOwner<LongArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Long::class.javaObjectType)

    val doubleArrays: Array<Array<CollectionOwner<DoubleArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Double::class.javaObjectType)

    val immutableReferenceArrays: Array<ImmutableArray<CollectionOwner<ImmutableArray<String>>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, String::class.java)

    val immutableBooleanArrays: Array<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Boolean::class.javaObjectType)

    val immutableByteArrays: Array<ImmutableArray<CollectionOwner<ImmutableByteArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Byte::class.javaObjectType)

    val immutableCharArrays: Array<ImmutableArray<CollectionOwner<ImmutableCharArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Char::class.javaObjectType)

    val immutableShortArrays: Array<ImmutableArray<CollectionOwner<ImmutableShortArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Short::class.javaObjectType)

    val immutableIntArrays: Array<ImmutableArray<CollectionOwner<ImmutableIntArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Int::class.javaObjectType)

    val immutableFloatArrays: Array<ImmutableArray<CollectionOwner<ImmutableFloatArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Float::class.javaObjectType)

    val immutableLongArrays: Array<ImmutableArray<CollectionOwner<ImmutableLongArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Long::class.javaObjectType)

    val immutableDoubleArrays: Array<ImmutableArray<CollectionOwner<ImmutableDoubleArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Double::class.javaObjectType)

    companion object {
        /** Creates deterministic data for one nested benchmark parameter combination. */
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            topLevelSizeDistributionFactory: DistributionFactory,
            nestedCollectionSizeDistributionFactory: DistributionFactory,
            nestedFieldGeneratorFactory: FieldGeneratorFactory,
            nestedReferenceGeneratorFactory: ObjectGeneratorFactory<String>,
        ): NestedCollectionBenchmarkData {
            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val topLevelSizeDistribution = topLevelSizeDistributionFactory.create(rngFactory)
            val nestedSizeDistribution = nestedCollectionSizeDistributionFactory.create(rngFactory)
            val nestedFieldGenerator = nestedFieldGeneratorFactory.create(generatorRngs)
            val nestedReferenceGenerator = nestedReferenceGeneratorFactory.create(generatorRngs)

            val topLevelCollectionClass = CollectionFactory.resolveCollectionClass(
                collectionType = collectionType,
                dataType = DataType.REFERENCE,
                referenceElementClass = CollectionOwner::class.java,
            )

            return NestedCollectionBenchmarkData(
                batch = CollectionBatch.create(
                    collectionType = collectionType,
                    logicalElementClass = dataType.resolveElementClass(nestedReferenceGenerator.objectClass),
                    collectionClass = topLevelCollectionClass,
                    numCollections = numCollections,
                    sizeDistribution = topLevelSizeDistribution,
                ) { topLevelSize ->
                    CollectionFactory.createCollection(topLevelSize, collectionType, CollectionOwner::class.java) {
                        CollectionOwner(
                            CollectionFactory.createCollection(
                                size = nestedSizeDistribution.nextValue(),
                                collectionType = collectionType,
                                dataType = dataType,
                                fieldGenerator = nestedFieldGenerator,
                                referenceGenerator = nestedReferenceGenerator,
                            )
                        )
                    }
                },
            )
        }
    }
}

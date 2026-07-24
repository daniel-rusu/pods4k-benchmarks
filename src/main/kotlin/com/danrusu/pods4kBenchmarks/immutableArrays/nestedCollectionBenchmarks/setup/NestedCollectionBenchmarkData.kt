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
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
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
    inline fun <reified T : Any> listData(): Array<ArrayList<CollectionOwner<ArrayList<T>>>> {
        return batch.getCollections(CollectionType.LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> persistentListData(): Array<PersistentList<CollectionOwner<PersistentList<T>>>> {
        return batch.getCollections(CollectionType.PERSISTENT_LIST, T::class.javaObjectType)
    }

    val referenceArrayData: Array<Array<CollectionOwner<Array<String>>>>
        get() = batch.getCollections(CollectionType.ARRAY, String::class.java)

    val booleanArrayData: Array<Array<CollectionOwner<BooleanArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Boolean::class.javaObjectType)

    val byteArrayData: Array<Array<CollectionOwner<ByteArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Byte::class.javaObjectType)

    val charArrayData: Array<Array<CollectionOwner<CharArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Char::class.javaObjectType)

    val shortArrayData: Array<Array<CollectionOwner<ShortArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Short::class.javaObjectType)

    val intArrayData: Array<Array<CollectionOwner<IntArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Int::class.javaObjectType)

    val floatArrayData: Array<Array<CollectionOwner<FloatArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Float::class.javaObjectType)

    val longArrayData: Array<Array<CollectionOwner<LongArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Long::class.javaObjectType)

    val doubleArrayData: Array<Array<CollectionOwner<DoubleArray>>>
        get() = batch.getCollections(CollectionType.ARRAY, Double::class.javaObjectType)

    val immutableReferenceArrayData: Array<ImmutableArray<CollectionOwner<ImmutableArray<String>>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, String::class.java)

    val immutableBooleanArrayData: Array<ImmutableArray<CollectionOwner<ImmutableBooleanArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Boolean::class.javaObjectType)

    val immutableByteArrayData: Array<ImmutableArray<CollectionOwner<ImmutableByteArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Byte::class.javaObjectType)

    val immutableCharArrayData: Array<ImmutableArray<CollectionOwner<ImmutableCharArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Char::class.javaObjectType)

    val immutableShortArrayData: Array<ImmutableArray<CollectionOwner<ImmutableShortArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Short::class.javaObjectType)

    val immutableIntArrayData: Array<ImmutableArray<CollectionOwner<ImmutableIntArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Int::class.javaObjectType)

    val immutableFloatArrayData: Array<ImmutableArray<CollectionOwner<ImmutableFloatArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Float::class.javaObjectType)

    val immutableLongArrayData: Array<ImmutableArray<CollectionOwner<ImmutableLongArray>>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Long::class.javaObjectType)

    val immutableDoubleArrayData: Array<ImmutableArray<CollectionOwner<ImmutableDoubleArray>>>
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
            require(numCollections > 0) { "numCollections must be positive" }

            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val topLevelSizeDistribution = topLevelSizeDistributionFactory.create(rngFactory)
            val nestedSizeDistribution = nestedCollectionSizeDistributionFactory.create(rngFactory)
            val fields = nestedFieldGeneratorFactory.create(generatorRngs)
            val references = nestedReferenceGeneratorFactory.create(generatorRngs)

            // data = Array<TopLevelCollection<CollectionOwner<NestedCollection<DataType>>
            // where TopLevelCollection is ArrayList, PersistentList, Array, or ImmutableArray
            // and NestedCollection is ArrayList, PersistentList, Array, ImmutableArray, or primitive array variants such as BooleanArray, ImmutableBooleanArray, etc.
            @Suppress("UNCHECKED_CAST")
            val data = ArrayCreator.createArray(
                componentClass = CollectionFactory.getCollectionClass(
                    collectionType = collectionType,
                    dataType = DataType.REFERENCE,
                    referenceElementClass = CollectionOwner::class.java
                ) as Class<Any>,
                size = numCollections,
            ) {
                CollectionFactory.createCollection(
                    size = topLevelSizeDistribution.nextValue(),
                    collectionType = collectionType,
                    elementClass = CollectionOwner::class.java,
                ) {
                    CollectionOwner(
                        CollectionFactory.createCollection(
                            nestedSizeDistribution.nextValue(),
                            collectionType,
                            dataType,
                            fields,
                            references
                        )
                    )
                }
            }

            return NestedCollectionBenchmarkData(
                batch = CollectionBatch(
                    collectionType = collectionType,
                    logicalElementClass = dataType.resolveElementClass(references.objectClass),
                    collections = data,
                ),
            )
        }
    }
}

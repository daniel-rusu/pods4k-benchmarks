package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup

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
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized collections for one [CollectionType]/[DataType] trial.
 *
 * Provides statically typed access to lists, persistent lists, JVM arrays, and immutable arrays. JVM and immutable array
 * accessors preserve primitive-specialized representations for primitive [DataType] values.
 */
class FlatCollectionBenchmarkData private constructor(
    @PublishedApi internal val batch: CollectionBatch,
) {
    inline fun <reified T : Any> lists(): Array<List<T>> {
        return batch.getCollections(CollectionType.LIST, T::class.javaObjectType)
    }

    inline fun <reified T : Any> persistentLists(): Array<PersistentList<T>> {
        return batch.getCollections(CollectionType.PERSISTENT_LIST, T::class.javaObjectType)
    }

    val referenceArrays: Array<Array<String>>
        get() = batch.getCollections(CollectionType.ARRAY, String::class.java)

    val booleanArrays: Array<BooleanArray>
        get() = batch.getCollections(CollectionType.ARRAY, Boolean::class.javaObjectType)

    val byteArrays: Array<ByteArray>
        get() = batch.getCollections(CollectionType.ARRAY, Byte::class.javaObjectType)

    val charArrays: Array<CharArray>
        get() = batch.getCollections(CollectionType.ARRAY, Char::class.javaObjectType)

    val shortArrays: Array<ShortArray>
        get() = batch.getCollections(CollectionType.ARRAY, Short::class.javaObjectType)

    val intArrays: Array<IntArray>
        get() = batch.getCollections(CollectionType.ARRAY, Int::class.javaObjectType)

    val floatArrays: Array<FloatArray>
        get() = batch.getCollections(CollectionType.ARRAY, Float::class.javaObjectType)

    val longArrays: Array<LongArray>
        get() = batch.getCollections(CollectionType.ARRAY, Long::class.javaObjectType)

    val doubleArrays: Array<DoubleArray>
        get() = batch.getCollections(CollectionType.ARRAY, Double::class.javaObjectType)

    val immutableReferenceArrays: Array<ImmutableArray<String>>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, String::class.java)

    val immutableBooleanArrays: Array<ImmutableBooleanArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Boolean::class.javaObjectType)

    val immutableByteArrays: Array<ImmutableByteArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Byte::class.javaObjectType)

    val immutableCharArrays: Array<ImmutableCharArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Char::class.javaObjectType)

    val immutableShortArrays: Array<ImmutableShortArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Short::class.javaObjectType)

    val immutableIntArrays: Array<ImmutableIntArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Int::class.javaObjectType)

    val immutableFloatArrays: Array<ImmutableFloatArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Float::class.javaObjectType)

    val immutableLongArrays: Array<ImmutableLongArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Long::class.javaObjectType)

    val immutableDoubleArrays: Array<ImmutableDoubleArray>
        get() = batch.getCollections(CollectionType.IMMUTABLE_ARRAY, Double::class.javaObjectType)

    companion object {
        /** Creates deterministic data for one flat benchmark parameter combination. */
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            fieldGeneratorFactory: FieldGeneratorFactory,
            referenceGeneratorFactory: ObjectGeneratorFactory<String>,
        ): FlatCollectionBenchmarkData {
            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val fields = fieldGeneratorFactory.create(generatorRngs)
            val references = referenceGeneratorFactory.create(generatorRngs)
            val collectionClass = CollectionFactory.getCollectionClass(
                collectionType = collectionType,
                dataType = dataType,
                referenceElementClass = references.objectClass,
            )
            return FlatCollectionBenchmarkData(
                batch = CollectionBatch.create(
                    collectionType = collectionType,
                    logicalElementClass = dataType.resolveElementClass(references.objectClass),
                    collectionClass = collectionClass,
                    numCollections = numCollections,
                    sizeDistribution = sizeDistribution,
                ) { size ->
                    CollectionFactory.createCollection(size, collectionType, dataType, fields, references)
                },
            )
        }
    }
}

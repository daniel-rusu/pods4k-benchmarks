package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.SHORT
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.NullabilityPolicyFactory
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.infra.Blackhole

/**
 * Base state for flat-collection benchmarks over nullable reference and boxed primitive elements.
 *
 * Each trial materializes [numCollections] collections for one [CollectionType]/[DataType] combination. Subclasses
 * pass equivalent statically typed operations for all four representations to [transformEachCollection].
 */
abstract class NullableFlatCollectionBenchmark(
    /** Number of distinct collections processed by each benchmark invocation. */
    private val numCollections: Int,
    /** Controls generated collection sizes. */
    private val sizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Creates non-null boxed primitive element values. */
    private val primitiveGeneratorFactory: PrimitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
    /** Creates non-null reference element values. */
    private val referenceGeneratorFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(),
    /** Creates the policy that owns null placement for every data type. */
    private val nullabilityPolicyFactory: NullabilityPolicyFactory,
) : CollectionBenchmark() {
    @PublishedApi
    internal lateinit var data: NullableFlatCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupBenchmarkData() {
        data = NullableFlatCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = numCollections,
            sizeDistributionFactory = sizeDistributionFactory,
            primitiveGeneratorFactory = primitiveGeneratorFactory,
            referenceGeneratorFactory = referenceGeneratorFactory,
            nullabilityPolicyFactory = nullabilityPolicyFactory,
        )
    }

    /**
     * Applies the transform selected by [collectionType] and [dataType] to every collection and consumes each result.
     */
    protected inline fun transformEachCollection(
        bh: Blackhole,
        transformList: (List<String?>) -> Any?,
        transformBooleanList: (List<Boolean?>) -> Any?,
        transformByteList: (List<Byte?>) -> Any?,
        transformCharList: (List<Char?>) -> Any?,
        transformShortList: (List<Short?>) -> Any?,
        transformIntList: (List<Int?>) -> Any?,
        transformFloatList: (List<Float?>) -> Any?,
        transformLongList: (List<Long?>) -> Any?,
        transformDoubleList: (List<Double?>) -> Any?,
        transformPersistentList: (PersistentList<String?>) -> Any?,
        transformPersistentBooleanList: (PersistentList<Boolean?>) -> Any?,
        transformPersistentByteList: (PersistentList<Byte?>) -> Any?,
        transformPersistentCharList: (PersistentList<Char?>) -> Any?,
        transformPersistentShortList: (PersistentList<Short?>) -> Any?,
        transformPersistentIntList: (PersistentList<Int?>) -> Any?,
        transformPersistentFloatList: (PersistentList<Float?>) -> Any?,
        transformPersistentLongList: (PersistentList<Long?>) -> Any?,
        transformPersistentDoubleList: (PersistentList<Double?>) -> Any?,
        transformArray: (Array<String?>) -> Any?,
        transformBooleanArray: (Array<Boolean?>) -> Any?,
        transformByteArray: (Array<Byte?>) -> Any?,
        transformCharArray: (Array<Char?>) -> Any?,
        transformShortArray: (Array<Short?>) -> Any?,
        transformIntArray: (Array<Int?>) -> Any?,
        transformFloatArray: (Array<Float?>) -> Any?,
        transformLongArray: (Array<Long?>) -> Any?,
        transformDoubleArray: (Array<Double?>) -> Any?,
        transformImmutableArray: (ImmutableArray<String?>) -> Any?,
        transformImmutableBooleanArray: (ImmutableArray<Boolean?>) -> Any?,
        transformImmutableByteArray: (ImmutableArray<Byte?>) -> Any?,
        transformImmutableCharArray: (ImmutableArray<Char?>) -> Any?,
        transformImmutableShortArray: (ImmutableArray<Short?>) -> Any?,
        transformImmutableIntArray: (ImmutableArray<Int?>) -> Any?,
        transformImmutableFloatArray: (ImmutableArray<Float?>) -> Any?,
        transformImmutableLongArray: (ImmutableArray<Long?>) -> Any?,
        transformImmutableDoubleArray: (ImmutableArray<Double?>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> for (collection in data.lists<String>()) bh.consume(transformList(collection))
                BOOLEAN -> for (collection in data.lists<Boolean>()) bh.consume(transformBooleanList(collection))
                BYTE -> for (collection in data.lists<Byte>()) bh.consume(transformByteList(collection))
                CHAR -> for (collection in data.lists<Char>()) bh.consume(transformCharList(collection))
                SHORT -> for (collection in data.lists<Short>()) bh.consume(transformShortList(collection))
                INT -> for (collection in data.lists<Int>()) bh.consume(transformIntList(collection))
                FLOAT -> for (collection in data.lists<Float>()) bh.consume(transformFloatList(collection))
                LONG -> for (collection in data.lists<Long>()) bh.consume(transformLongList(collection))
                DOUBLE -> for (collection in data.lists<Double>()) bh.consume(transformDoubleList(collection))
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> for (collection in data.persistentLists<String>()) bh.consume(transformPersistentList(collection))
                BOOLEAN -> for (collection in data.persistentLists<Boolean>()) bh.consume(transformPersistentBooleanList(collection))
                BYTE -> for (collection in data.persistentLists<Byte>()) bh.consume(transformPersistentByteList(collection))
                CHAR -> for (collection in data.persistentLists<Char>()) bh.consume(transformPersistentCharList(collection))
                SHORT -> for (collection in data.persistentLists<Short>()) bh.consume(transformPersistentShortList(collection))
                INT -> for (collection in data.persistentLists<Int>()) bh.consume(transformPersistentIntList(collection))
                FLOAT -> for (collection in data.persistentLists<Float>()) bh.consume(transformPersistentFloatList(collection))
                LONG -> for (collection in data.persistentLists<Long>()) bh.consume(transformPersistentLongList(collection))
                DOUBLE -> for (collection in data.persistentLists<Double>()) bh.consume(transformPersistentDoubleList(collection))
            }

            ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.arrays<String>()) bh.consume(transformArray(collection))
                BOOLEAN -> for (collection in data.arrays<Boolean>()) bh.consume(transformBooleanArray(collection))
                BYTE -> for (collection in data.arrays<Byte>()) bh.consume(transformByteArray(collection))
                CHAR -> for (collection in data.arrays<Char>()) bh.consume(transformCharArray(collection))
                SHORT -> for (collection in data.arrays<Short>()) bh.consume(transformShortArray(collection))
                INT -> for (collection in data.arrays<Int>()) bh.consume(transformIntArray(collection))
                FLOAT -> for (collection in data.arrays<Float>()) bh.consume(transformFloatArray(collection))
                LONG -> for (collection in data.arrays<Long>()) bh.consume(transformLongArray(collection))
                DOUBLE -> for (collection in data.arrays<Double>()) bh.consume(transformDoubleArray(collection))
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.immutableArrays<String>()) bh.consume(transformImmutableArray(collection))
                BOOLEAN -> for (collection in data.immutableArrays<Boolean>()) bh.consume(transformImmutableBooleanArray(collection))
                BYTE -> for (collection in data.immutableArrays<Byte>()) bh.consume(transformImmutableByteArray(collection))
                CHAR -> for (collection in data.immutableArrays<Char>()) bh.consume(transformImmutableCharArray(collection))
                SHORT -> for (collection in data.immutableArrays<Short>()) bh.consume(transformImmutableShortArray(collection))
                INT -> for (collection in data.immutableArrays<Int>()) bh.consume(transformImmutableIntArray(collection))
                FLOAT -> for (collection in data.immutableArrays<Float>()) bh.consume(transformImmutableFloatArray(collection))
                LONG -> for (collection in data.immutableArrays<Long>()) bh.consume(transformImmutableLongArray(collection))
                DOUBLE -> for (collection in data.immutableArrays<Double>()) bh.consume(transformImmutableDoubleArray(collection))
            }
        }
    }
}

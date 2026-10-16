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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
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
import com.danrusu.pods4kBenchmarks.utils.generators.primitiveGenerator.PrimitiveGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.infra.Blackhole

/**
 * Base state for operations on nested collections, such as orders that each contain products.
 *
 * Here, "collection" includes [List], [PersistentList], [Array], and [ImmutableArray] representations rather than only
 * implementations of the Kotlin [Collection] interface.
 */
abstract class NestedCollectionBenchmark(
    /** Number of distinct top-level collections processed by each benchmark invocation. */
    private val numCollections: Int,
    /** Controls the sizes of the parent collections that will be generated. */
    private val topLevelSizeDistributionFactory: DistributionFactory =
        DistributionFactory.ListSizeDistribution,
    /** Controls the sizes of the nested collections that will be generated. */
    private val nestedCollectionSizeDistributionFactory: DistributionFactory =
        DistributionFactory.NestedListSizeDistribution,
    /** Creates simple primitive generators for primitive nested collection elements. */
    private val nestedPrimitiveGeneratorFactory: PrimitiveGeneratorFactory = PrimitiveGeneratorFactory.randomValues(),
    /** Creates reference generators for nested collection elements. */
    private val nestedReferenceGeneratorFactory: ObjectGeneratorFactory<String> = ObjectGeneratorFactory.randomStrings(),
) : CollectionBenchmark() {
    @PublishedApi
    internal lateinit var data: NestedCollectionBenchmarkData

    @Setup(Level.Trial)
    fun setupBenchmarkData() {
        data = NestedCollectionBenchmarkData.create(
            collectionType = collectionType,
            dataType = dataType,
            numCollections = numCollections,
            topLevelSizeDistributionFactory = topLevelSizeDistributionFactory,
            nestedCollectionSizeDistributionFactory = nestedCollectionSizeDistributionFactory,
            nestedPrimitiveGeneratorFactory = nestedPrimitiveGeneratorFactory,
            nestedReferenceGeneratorFactory = nestedReferenceGeneratorFactory,
        )
    }

    /** Performs the associated operation on every top-level collection and consumes each result. */
    protected inline fun transformEachCollection(
        bh: Blackhole,

        // lists
        transformNestedLists: (List<CollectionOwner<List<String>>>) -> Any?,
        transformNestedBooleanLists: (List<CollectionOwner<List<Boolean>>>) -> Any?,
        transformNestedByteLists: (List<CollectionOwner<List<Byte>>>) -> Any?,
        transformNestedCharLists: (List<CollectionOwner<List<Char>>>) -> Any?,
        transformNestedShortLists: (List<CollectionOwner<List<Short>>>) -> Any?,
        transformNestedIntLists: (List<CollectionOwner<List<Int>>>) -> Any?,
        transformNestedFloatLists: (List<CollectionOwner<List<Float>>>) -> Any?,
        transformNestedLongLists: (List<CollectionOwner<List<Long>>>) -> Any?,
        transformNestedDoubleLists: (List<CollectionOwner<List<Double>>>) -> Any?,

        // persistent lists
        transformNestedPersistentLists: (PersistentList<CollectionOwner<PersistentList<String>>>) -> Any?,
        transformNestedPersistentBooleanLists: (PersistentList<CollectionOwner<PersistentList<Boolean>>>) -> Any?,
        transformNestedPersistentByteLists: (PersistentList<CollectionOwner<PersistentList<Byte>>>) -> Any?,
        transformNestedPersistentCharLists: (PersistentList<CollectionOwner<PersistentList<Char>>>) -> Any?,
        transformNestedPersistentShortLists: (PersistentList<CollectionOwner<PersistentList<Short>>>) -> Any?,
        transformNestedPersistentIntLists: (PersistentList<CollectionOwner<PersistentList<Int>>>) -> Any?,
        transformNestedPersistentFloatLists: (PersistentList<CollectionOwner<PersistentList<Float>>>) -> Any?,
        transformNestedPersistentLongLists: (PersistentList<CollectionOwner<PersistentList<Long>>>) -> Any?,
        transformNestedPersistentDoubleLists: (PersistentList<CollectionOwner<PersistentList<Double>>>) -> Any?,

        // arrays
        transformNestedArrays: (Array<CollectionOwner<Array<String>>>) -> Any?,
        transformNestedBooleanArrays: (Array<CollectionOwner<BooleanArray>>) -> Any?,
        transformNestedByteArrays: (Array<CollectionOwner<ByteArray>>) -> Any?,
        transformNestedCharArrays: (Array<CollectionOwner<CharArray>>) -> Any?,
        transformNestedShortArrays: (Array<CollectionOwner<ShortArray>>) -> Any?,
        transformNestedIntArrays: (Array<CollectionOwner<IntArray>>) -> Any?,
        transformNestedFloatArrays: (Array<CollectionOwner<FloatArray>>) -> Any?,
        transformNestedLongArrays: (Array<CollectionOwner<LongArray>>) -> Any?,
        transformNestedDoubleArrays: (Array<CollectionOwner<DoubleArray>>) -> Any?,

        // immutable arrays
        transformNestedImmutableArrays: (ImmutableArray<CollectionOwner<ImmutableArray<String>>>) -> Any?,
        transformNestedImmutableBooleanArrays: (ImmutableArray<CollectionOwner<ImmutableBooleanArray>>) -> Any?,
        transformNestedImmutableByteArrays: (ImmutableArray<CollectionOwner<ImmutableByteArray>>) -> Any?,
        transformNestedImmutableCharArrays: (ImmutableArray<CollectionOwner<ImmutableCharArray>>) -> Any?,
        transformNestedImmutableShortArrays: (ImmutableArray<CollectionOwner<ImmutableShortArray>>) -> Any?,
        transformNestedImmutableIntArrays: (ImmutableArray<CollectionOwner<ImmutableIntArray>>) -> Any?,
        transformNestedImmutableFloatArrays: (ImmutableArray<CollectionOwner<ImmutableFloatArray>>) -> Any?,
        transformNestedImmutableLongArrays: (ImmutableArray<CollectionOwner<ImmutableLongArray>>) -> Any?,
        transformNestedImmutableDoubleArrays: (ImmutableArray<CollectionOwner<ImmutableDoubleArray>>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> when (dataType) {
                REFERENCE -> for (collection in data.lists<String>()) bh.consume(transformNestedLists(collection))
                BOOLEAN -> for (collection in data.lists<Boolean>()) bh.consume(transformNestedBooleanLists(collection))
                BYTE -> for (collection in data.lists<Byte>()) bh.consume(transformNestedByteLists(collection))
                CHAR -> for (collection in data.lists<Char>()) bh.consume(transformNestedCharLists(collection))
                SHORT -> for (collection in data.lists<Short>()) bh.consume(transformNestedShortLists(collection))
                INT -> for (collection in data.lists<Int>()) bh.consume(transformNestedIntLists(collection))
                FLOAT -> for (collection in data.lists<Float>()) bh.consume(transformNestedFloatLists(collection))
                LONG -> for (collection in data.lists<Long>()) bh.consume(transformNestedLongLists(collection))
                DOUBLE -> for (collection in data.lists<Double>()) bh.consume(transformNestedDoubleLists(collection))
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> for (collection in data.persistentLists<String>()) bh.consume(transformNestedPersistentLists(collection))
                BOOLEAN -> for (collection in data.persistentLists<Boolean>()) bh.consume(transformNestedPersistentBooleanLists(collection))
                BYTE -> for (collection in data.persistentLists<Byte>()) bh.consume(transformNestedPersistentByteLists(collection))
                CHAR -> for (collection in data.persistentLists<Char>()) bh.consume(transformNestedPersistentCharLists(collection))
                SHORT -> for (collection in data.persistentLists<Short>()) bh.consume(transformNestedPersistentShortLists(collection))
                INT -> for (collection in data.persistentLists<Int>()) bh.consume(transformNestedPersistentIntLists(collection))
                FLOAT -> for (collection in data.persistentLists<Float>()) bh.consume(transformNestedPersistentFloatLists(collection))
                LONG -> for (collection in data.persistentLists<Long>()) bh.consume(transformNestedPersistentLongLists(collection))
                DOUBLE -> for (collection in data.persistentLists<Double>()) bh.consume(transformNestedPersistentDoubleLists(collection))
            }

            ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.referenceArrays) bh.consume(transformNestedArrays(collection))
                BOOLEAN -> for (collection in data.booleanArrays) bh.consume(transformNestedBooleanArrays(collection))
                BYTE -> for (collection in data.byteArrays) bh.consume(transformNestedByteArrays(collection))
                CHAR -> for (collection in data.charArrays) bh.consume(transformNestedCharArrays(collection))
                SHORT -> for (collection in data.shortArrays) bh.consume(transformNestedShortArrays(collection))
                INT -> for (collection in data.intArrays) bh.consume(transformNestedIntArrays(collection))
                FLOAT -> for (collection in data.floatArrays) bh.consume(transformNestedFloatArrays(collection))
                LONG -> for (collection in data.longArrays) bh.consume(transformNestedLongArrays(collection))
                DOUBLE -> for (collection in data.doubleArrays) bh.consume(transformNestedDoubleArrays(collection))
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> for (collection in data.immutableReferenceArrays) bh.consume(transformNestedImmutableArrays(collection))
                BOOLEAN -> for (collection in data.immutableBooleanArrays) bh.consume(transformNestedImmutableBooleanArrays(collection))
                BYTE -> for (collection in data.immutableByteArrays) bh.consume(transformNestedImmutableByteArrays(collection))
                CHAR -> for (collection in data.immutableCharArrays) bh.consume(transformNestedImmutableCharArrays(collection))
                SHORT -> for (collection in data.immutableShortArrays) bh.consume(transformNestedImmutableShortArrays(collection))
                INT -> for (collection in data.immutableIntArrays) bh.consume(transformNestedImmutableIntArrays(collection))
                FLOAT -> for (collection in data.immutableFloatArrays) bh.consume(transformNestedImmutableFloatArrays(collection))
                LONG -> for (collection in data.immutableLongArrays) bh.consume(transformNestedImmutableLongArrays(collection))
                DOUBLE -> for (collection in data.immutableDoubleArrays) bh.consume(transformNestedImmutableDoubleArrays(collection))
            }
        }
    }

}

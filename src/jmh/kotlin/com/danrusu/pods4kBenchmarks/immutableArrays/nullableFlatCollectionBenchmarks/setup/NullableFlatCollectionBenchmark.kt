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
import com.danrusu.pods4kBenchmarks.utils.generators.fieldGenerator.FieldGeneratorFactory
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
    /** Creates nullable boxed primitive element values. */
    private val fieldGeneratorFactory: FieldGeneratorFactory,
    /** Creates nullable reference element values. */
    private val referenceGeneratorFactory: ObjectGeneratorFactory<String?>,
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
            fieldGeneratorFactory = fieldGeneratorFactory,
            referenceGeneratorFactory = referenceGeneratorFactory,
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
                REFERENCE -> data.lists<String>().forEach { bh.consume(transformList(it)) }
                BOOLEAN -> data.lists<Boolean>().forEach { bh.consume(transformBooleanList(it)) }
                BYTE -> data.lists<Byte>().forEach { bh.consume(transformByteList(it)) }
                CHAR -> data.lists<Char>().forEach { bh.consume(transformCharList(it)) }
                SHORT -> data.lists<Short>().forEach { bh.consume(transformShortList(it)) }
                INT -> data.lists<Int>().forEach { bh.consume(transformIntList(it)) }
                FLOAT -> data.lists<Float>().forEach { bh.consume(transformFloatList(it)) }
                LONG -> data.lists<Long>().forEach { bh.consume(transformLongList(it)) }
                DOUBLE -> data.lists<Double>().forEach { bh.consume(transformDoubleList(it)) }
            }

            PERSISTENT_LIST -> when (dataType) {
                REFERENCE -> data.persistentLists<String>().forEach { bh.consume(transformPersistentList(it)) }
                BOOLEAN -> data.persistentLists<Boolean>().forEach { bh.consume(transformPersistentBooleanList(it)) }
                BYTE -> data.persistentLists<Byte>().forEach { bh.consume(transformPersistentByteList(it)) }
                CHAR -> data.persistentLists<Char>().forEach { bh.consume(transformPersistentCharList(it)) }
                SHORT -> data.persistentLists<Short>().forEach { bh.consume(transformPersistentShortList(it)) }
                INT -> data.persistentLists<Int>().forEach { bh.consume(transformPersistentIntList(it)) }
                FLOAT -> data.persistentLists<Float>().forEach { bh.consume(transformPersistentFloatList(it)) }
                LONG -> data.persistentLists<Long>().forEach { bh.consume(transformPersistentLongList(it)) }
                DOUBLE -> data.persistentLists<Double>().forEach { bh.consume(transformPersistentDoubleList(it)) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> data.arrays<String>().forEach { bh.consume(transformArray(it)) }
                BOOLEAN -> data.arrays<Boolean>().forEach { bh.consume(transformBooleanArray(it)) }
                BYTE -> data.arrays<Byte>().forEach { bh.consume(transformByteArray(it)) }
                CHAR -> data.arrays<Char>().forEach { bh.consume(transformCharArray(it)) }
                SHORT -> data.arrays<Short>().forEach { bh.consume(transformShortArray(it)) }
                INT -> data.arrays<Int>().forEach { bh.consume(transformIntArray(it)) }
                FLOAT -> data.arrays<Float>().forEach { bh.consume(transformFloatArray(it)) }
                LONG -> data.arrays<Long>().forEach { bh.consume(transformLongArray(it)) }
                DOUBLE -> data.arrays<Double>().forEach { bh.consume(transformDoubleArray(it)) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> data.immutableArrays<String>().forEach { bh.consume(transformImmutableArray(it)) }
                BOOLEAN -> data.immutableArrays<Boolean>().forEach { bh.consume(transformImmutableBooleanArray(it)) }
                BYTE -> data.immutableArrays<Byte>().forEach { bh.consume(transformImmutableByteArray(it)) }
                CHAR -> data.immutableArrays<Char>().forEach { bh.consume(transformImmutableCharArray(it)) }
                SHORT -> data.immutableArrays<Short>().forEach { bh.consume(transformImmutableShortArray(it)) }
                INT -> data.immutableArrays<Int>().forEach { bh.consume(transformImmutableIntArray(it)) }
                FLOAT -> data.immutableArrays<Float>().forEach { bh.consume(transformImmutableFloatArray(it)) }
                LONG -> data.immutableArrays<Long>().forEach { bh.consume(transformImmutableLongArray(it)) }
                DOUBLE -> data.immutableArrays<Double>().forEach { bh.consume(transformImmutableDoubleArray(it)) }
            }
        }
    }
}

package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.infra.Blackhole

/**
 * Represents a benchmark that measures the performance of operating on collections that store objects (not the 8 base
 * types).  Note that the term collection is used loosely to represent a [List], [Array], or [ImmutableArray] rather
 * than the actual [Collection] interface.
 *
 * Benchmarks are parameterized by each of the [CollectionType] values.
 *
 * Subclasses should create 9 benchmark methods for measuring the performance of an operation for each of the 8 base
 * types (like Boolean, Int, etc.) plus the most common reference type, String.  The benchmark methods should call
 * [transformEachCollection] providing the operations to be performed for each collection type.
 *
 * For example, if [numCollections] is 500, type is [CollectionType.LIST], and data type is [DataType.BOOLEAN], then 500
 * List<Boolean> collections will be created.  When the subclass calls [transformEachCollection], the provided boolean
 * collection transform will be called for each of the 500 collections.
 */
@State(Scope.Benchmark)
abstract class ObjectCollectionBenchmark<T>(
    /**
     * The number of collections to benchmark against in order to avoid repeating the operation being measured from
     * being performed on the same collection repeatedly. This number should be sufficiently large, like 1000, to
     * avoid misleading results from optimizations like caching etc.
     */
    private val numCollections: Int,
    /** Controls the sizes of the collections that will be generated */
    private val sizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Creates object generators for the element data that the collections will contain. */
    private val objectGeneratorFactory: ObjectGeneratorFactory<T>,
) {
    /**
     * Repeat the benchmark for each of the 8 base data types plus a String reference type.
     */
    @Param
    protected lateinit var collectionType: CollectionType

    @PublishedApi
    internal lateinit var data: ObjectCollectionBenchmarkData<T>

    @Setup(Level.Trial)
    fun setupCollections() {
        data = ObjectCollectionBenchmarkData.create(
            collectionType = collectionType,
            numCollections = numCollections,
            sizeDistributionFactory = sizeDistributionFactory,
            objectGeneratorFactory = objectGeneratorFactory,
        )
    }

    /**
     * Loops through all the collections of the current [collectionType] and performs the associated operation
     * consuming each result with the [Blackhole].
     *
     * E.g. If the current collectionType is [CollectionType.ARRAY], then it calls [transformArray] on each array.
     */
    protected inline fun transformEachCollection(
        bh: Blackhole,
        transformList: (List<T>) -> Any?,
        transformPersistentList: (PersistentList<T>) -> Any?,
        transformArray: (Array<T>) -> Any?,
        transformImmutableArray: (ImmutableArray<T>) -> Any?,
    ) {
        when (collectionType) {
            LIST -> data.listData.forEach { bh.consume(transformList(it)) }
            PERSISTENT_LIST -> data.persistentListData.forEach { bh.consume(transformPersistentList(it)) }
            ARRAY -> data.arrayData.forEach { bh.consume(transformArray(it)) }
            IMMUTABLE_ARRAY -> data.immutableArrayData.forEach { bh.consume(transformImmutableArray(it)) }
        }
    }
}

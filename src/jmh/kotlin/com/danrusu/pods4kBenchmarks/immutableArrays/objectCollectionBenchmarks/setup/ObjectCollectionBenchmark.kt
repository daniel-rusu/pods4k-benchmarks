package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
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
 * Base state for benchmarks over collections of objects [T].
 *
 * Each trial materializes [numCollections] collections for one [CollectionType]. Subclasses define the measured
 * operation and pass equivalent transforms for all four representations to [transformEachCollection].
 */
@State(Scope.Benchmark)
abstract class ObjectCollectionBenchmark<T>(
    /** Number of distinct collections processed by each benchmark invocation. */
    private val numCollections: Int,
    /** Controls generated collection sizes. */
    private val sizeDistributionFactory: DistributionFactory = DistributionFactory.ListSizeDistribution,
    /** Creates the objects stored in each collection. */
    private val objectGeneratorFactory: ObjectGeneratorFactory<T>,
) {
    /** Repeats each benchmark for every collection type */
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

    /** Applies the transform for the active [collectionType] to every collection and consumes each result. */
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

package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.generators.GeneratorRngs
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.infra.Blackhole

/**
 * Base state for benchmarks over collections of objects [T].
 *
 * Each trial materializes [numCollections] collections for the current [CollectionType]. Subclasses must switch on the
 * [DataType] parameter as they control the shape of [T] and know what the dataType represents. Subclasses define the
 * measured operation and pass equivalent transforms for all four representations to [transformEachCollection].
 */
abstract class ObjectCollectionBenchmark<T>(
    /** Number of distinct collections processed by each benchmark invocation. */
    private val numCollections: Int,
    /** Constructs fresh components once per trial from the supplied purpose-specific streams. */
    private val createDataRecipe: (GeneratorRngs) -> ObjectCollectionRecipe<T>,
) : CollectionBenchmark() {
    @PublishedApi
    internal lateinit var data: ObjectCollectionBenchmarkData<T>

    @Setup(Level.Trial)
    fun setupBenchmarkData() {
        val generatorRngs = BenchmarkGeneratorRngs()
        data = ObjectCollectionBenchmarkData.create(
            collectionType = collectionType,
            numCollections = numCollections,
            recipe = createDataRecipe(generatorRngs),
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
            LIST -> for (collection in data.lists) bh.consume(transformList(collection))
            PERSISTENT_LIST -> for (collection in data.persistentLists) bh.consume(transformPersistentList(collection))
            ARRAY -> for (collection in data.arrays) bh.consume(transformArray(collection))
            IMMUTABLE_ARRAY -> for (collection in data.immutableArrays) bh.consume(transformImmutableArray(collection))
        }
    }
}

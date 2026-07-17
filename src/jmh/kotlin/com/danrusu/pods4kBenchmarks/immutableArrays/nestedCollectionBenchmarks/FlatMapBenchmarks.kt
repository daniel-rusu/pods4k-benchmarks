package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.flatMap
import com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup.CollectionOwner
import com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks.setup.NestedCollectionBenchmark
import kotlinx.collections.immutable.PersistentList
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit

private const val NUM_COLLECTIONS = 250

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class FlatMapBenchmarks : NestedCollectionBenchmark(numCollections = NUM_COLLECTIONS) {
    @Benchmark
    fun flatMap(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<CollectionOwner<List<String>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Boolean>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Byte>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Char>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Short>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Int>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Float>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Long>>> -> list.flatMap { it.nestedCollection } },
            { list: List<CollectionOwner<List<Double>>> -> list.flatMap { it.nestedCollection } },

            // persistent lists
            { list: PersistentList<CollectionOwner<PersistentList<String>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Boolean>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Byte>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Char>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Short>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Int>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Float>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Long>>> -> list.flatMap { it.nestedCollection } },
            { list: PersistentList<CollectionOwner<PersistentList<Double>>> -> list.flatMap { it.nestedCollection } },

            // arrays
            // Array.flatMap requires a nested iterable. Using asList() wraps each nested array without copying its data.
            { array: Array<CollectionOwner<Array<String>>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<BooleanArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<ByteArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<CharArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<ShortArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<IntArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<FloatArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<LongArray>> -> array.flatMap { it.nestedCollection.asList() } },
            { array: Array<CollectionOwner<DoubleArray>> -> array.flatMap { it.nestedCollection.asList() } },

            // immutable arrays
            { array: ImmutableArray<CollectionOwner<ImmutableArray<String>>> ->
                array.flatMap { it.nestedCollection }
            },
            { array: ImmutableArray<CollectionOwner<ImmutableBooleanArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableByteArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableCharArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableShortArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableIntArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableFloatArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableLongArray>> -> array.flatMap { it.nestedCollection } },
            { array: ImmutableArray<CollectionOwner<ImmutableDoubleArray>> -> array.flatMap { it.nestedCollection } },
        )
    }
}

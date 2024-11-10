package com.danrusu.pods4kBenchmarks.immutableArrays.nestedCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.flatMap
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.NestedCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.BooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.BooleanListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ByteListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.CharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.CharListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.DoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.DoubleListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.FloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.FloatListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableBooleanArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableByteArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableCharArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableDoubleArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableFloatArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableIntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableLongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ImmutableShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.IntArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.IntListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.LongArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.LongListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ReferenceArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ReferenceListWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ShortArrayWrapper
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.collectionWrappers.ShortListWrapper
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
open class FlatMapBenchmarks : NestedCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun flatMap(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<ReferenceListWrapper> -> list.flatMap { it.referenceList } },
            { list: List<BooleanListWrapper> -> list.flatMap { it.booleanList } },
            { list: List<ByteListWrapper> -> list.flatMap { it.byteList } },
            { list: List<CharListWrapper> -> list.flatMap { it.charList } },
            { list: List<ShortListWrapper> -> list.flatMap { it.shortList } },
            { list: List<IntListWrapper> -> list.flatMap { it.intList } },
            { list: List<FloatListWrapper> -> list.flatMap { it.floatList } },
            { list: List<LongListWrapper> -> list.flatMap { it.longList } },
            { list: List<DoubleListWrapper> -> list.flatMap { it.doubleList } },

            // arrays
            // Note that array.flatMap requires a nested iterable, so we need to call asList() on each nested array. The
            // asList() function wraps the array without copying the data by using the same array as the backing data
            { array: Array<ReferenceArrayWrapper> -> array.flatMap { it.referenceArray.asList() } },
            { array: Array<BooleanArrayWrapper> -> array.flatMap { it.booleanArray.asList() } },
            { array: Array<ByteArrayWrapper> -> array.flatMap { it.byteArray.asList() } },
            { array: Array<CharArrayWrapper> -> array.flatMap { it.charArray.asList() } },
            { array: Array<ShortArrayWrapper> -> array.flatMap { it.shortArray.asList() } },
            { array: Array<IntArrayWrapper> -> array.flatMap { it.intArray.asList() } },
            { array: Array<FloatArrayWrapper> -> array.flatMap { it.floatArray.asList() } },
            { array: Array<LongArrayWrapper> -> array.flatMap { it.longArray.asList() } },
            { array: Array<DoubleArrayWrapper> -> array.flatMap { it.doubleArray.asList() } },

            // immutable arrays
            { array: ImmutableArray<ImmutableReferenceArrayWrapper> -> array.flatMap { it.immutableReferenceArray } },
            { array: ImmutableArray<ImmutableBooleanArrayWrapper> -> array.flatMap { it.immutableBooleanArray } },
            { array: ImmutableArray<ImmutableByteArrayWrapper> -> array.flatMap { it.immutableByteArray } },
            { array: ImmutableArray<ImmutableCharArrayWrapper> -> array.flatMap { it.immutableCharArray } },
            { array: ImmutableArray<ImmutableShortArrayWrapper> -> array.flatMap { it.immutableShortArray } },
            { array: ImmutableArray<ImmutableIntArrayWrapper> -> array.flatMap { it.immutableIntArray } },
            { array: ImmutableArray<ImmutableFloatArrayWrapper> -> array.flatMap { it.immutableFloatArray } },
            { array: ImmutableArray<ImmutableLongArrayWrapper> -> array.flatMap { it.immutableLongArray } },
            { array: ImmutableArray<ImmutableDoubleArrayWrapper> -> array.flatMap { it.immutableDoubleArray } },
        )
    }
}

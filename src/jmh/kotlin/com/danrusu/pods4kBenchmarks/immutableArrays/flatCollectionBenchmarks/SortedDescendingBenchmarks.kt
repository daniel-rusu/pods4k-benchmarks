package com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
import com.danrusu.pods4k.immutableArrays.sortedDescending
import com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup.FlatCollectionBenchmark
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

private const val NUM_COLLECTIONS = 1000

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(2)
open class SortedDescendingBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun sortedDescending(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.sortedDescending() },
            { list: List<Boolean> -> list.sortedDescending() },
            { list: List<Byte> -> list.sortedDescending() },
            { list: List<Char> -> list.sortedDescending() },
            { list: List<Short> -> list.sortedDescending() },
            { list: List<Int> -> list.sortedDescending() },
            { list: List<Float> -> list.sortedDescending() },
            { list: List<Long> -> list.sortedDescending() },
            { list: List<Double> -> list.sortedDescending() },

            // arrays
            { array: Array<String> -> array.sortedDescending() },
            { _: BooleanArray -> throw UnsupportedOperationException("BooleanArray doesn't have this ability") },
            { array: ByteArray -> array.sortedDescending() },
            { array: CharArray -> array.sortedDescending() },
            { array: ShortArray -> array.sortedDescending() },
            { array: IntArray -> array.sortedDescending() },
            { array: FloatArray -> array.sortedDescending() },
            { array: LongArray -> array.sortedDescending() },
            { array: DoubleArray -> array.sortedDescending() },

            // immutable arrays
            { array: ImmutableArray<String> -> array.sortedDescending() },
            { _: ImmutableBooleanArray ->
                throw UnsupportedOperationException("ImmutableBooleanArray doesn't have this ability")
            },
            { array: ImmutableByteArray -> array.sortedDescending() },
            { array: ImmutableCharArray -> array.sortedDescending() },
            { array: ImmutableShortArray -> array.sortedDescending() },
            { array: ImmutableIntArray -> array.sortedDescending() },
            { array: ImmutableFloatArray -> array.sortedDescending() },
            { array: ImmutableLongArray -> array.sortedDescending() },
            { array: ImmutableDoubleArray -> array.sortedDescending() },
        )
    }
}
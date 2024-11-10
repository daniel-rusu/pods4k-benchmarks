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
import com.danrusu.pods4k.immutableArrays.sorted
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.FlatCollectionBenchmark
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
open class SortedBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun sorted(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.sorted() },
            { list: List<Boolean> -> list.sorted() },
            { list: List<Byte> -> list.sorted() },
            { list: List<Char> -> list.sorted() },
            { list: List<Short> -> list.sorted() },
            { list: List<Int> -> list.sorted() },
            { list: List<Float> -> list.sorted() },
            { list: List<Long> -> list.sorted() },
            { list: List<Double> -> list.sorted() },

            // arrays
            { array: Array<String> -> array.sorted() },
            { _: BooleanArray -> throw UnsupportedOperationException("BooleanArray doesn't have this ability") },
            { array: ByteArray -> array.sorted() },
            { array: CharArray -> array.sorted() },
            { array: ShortArray -> array.sorted() },
            { array: IntArray -> array.sorted() },
            { array: FloatArray -> array.sorted() },
            { array: LongArray -> array.sorted() },
            { array: DoubleArray -> array.sorted() },

            // immutable arrays
            { array: ImmutableArray<String> -> array.sorted() },
            { _: ImmutableBooleanArray ->
                throw UnsupportedOperationException("ImmutableBooleanArray doesn't have this ability")
            },
            { array: ImmutableByteArray -> array.sorted() },
            { array: ImmutableCharArray -> array.sorted() },
            { array: ImmutableShortArray -> array.sorted() },
            { array: ImmutableIntArray -> array.sorted() },
            { array: ImmutableFloatArray -> array.sorted() },
            { array: ImmutableLongArray -> array.sorted() },
            { array: ImmutableDoubleArray -> array.sorted() },
        )
    }
}

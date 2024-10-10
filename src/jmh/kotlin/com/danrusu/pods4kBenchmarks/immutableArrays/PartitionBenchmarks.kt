package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray
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
open class PartitionBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun partition(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.partition { it.length % 2 == 0 } },
            { list: List<Boolean> -> list.partition { it } },
            { list: List<Byte> -> list.partition { it >= 0 } },
            { list: List<Char> -> list.partition { it.code % 2 == 0 } },
            { list: List<Short> -> list.partition { it >= 0 } },
            { list: List<Int> -> list.partition { it >= 0 } },
            { list: List<Float> -> list.partition { it >= 0.5f } },
            { list: List<Long> -> list.partition { it >= 0L } },
            { list: List<Double> -> list.partition { it >= 0.5 } },

            // arrays
            { array: Array<String> -> array.partition { it.length % 2 == 0 } },
            { array: BooleanArray -> array.partition { it } },
            { array: ByteArray -> array.partition { it >= 0 } },
            { array: CharArray -> array.partition { it.code % 2 == 0 } },
            { array: ShortArray -> array.partition { it >= 0 } },
            { array: IntArray -> array.partition { it >= 0 } },
            { array: FloatArray -> array.partition { it >= 0.5f } },
            { array: LongArray -> array.partition { it >= 0L } },
            { array: DoubleArray -> array.partition { it >= 0.5 } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.partition { it.length % 2 == 0 } },
            { array: ImmutableBooleanArray -> array.partition { it } },
            { array: ImmutableByteArray -> array.partition { it >= 0 } },
            { array: ImmutableCharArray -> array.partition { it.code % 2 == 0 } },
            { array: ImmutableShortArray -> array.partition { it >= 0 } },
            { array: ImmutableIntArray -> array.partition { it >= 0 } },
            { array: ImmutableFloatArray -> array.partition { it >= 0.5f } },
            { array: ImmutableLongArray -> array.partition { it >= 0L } },
            { array: ImmutableDoubleArray -> array.partition { it >= 0.5 } },
        )
    }
}

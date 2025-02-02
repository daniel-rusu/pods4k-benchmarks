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
import com.danrusu.pods4k.immutableArrays.plus
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
open class PlusElementBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun plusElement(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list + "" },
            { list: List<Boolean> -> list + true },
            { list: List<Byte> -> list + Byte.MIN_VALUE },
            { list: List<Char> -> list + 'A' },
            { list: List<Short> -> list + Short.MIN_VALUE },
            { list: List<Int> -> list + Int.MIN_VALUE },
            { list: List<Float> -> list + 0.0f },
            { list: List<Long> -> list + Long.MIN_VALUE },
            { list: List<Double> -> list + 0.0 },

            // arrays
            { array: Array<String> -> array + "" },
            { array: BooleanArray -> array + true },
            { array: ByteArray -> array + Byte.MIN_VALUE },
            { array: CharArray -> array + 'A' },
            { array: ShortArray -> array + Short.MIN_VALUE },
            { array: IntArray -> array + Int.MIN_VALUE },
            { array: FloatArray -> array + 0.0f },
            { array: LongArray -> array + Long.MIN_VALUE },
            { array: DoubleArray -> array + 0.0 },

            // immutable arrays
            { array: ImmutableArray<String> -> array + "" },
            { array: ImmutableBooleanArray -> array + true },
            { array: ImmutableByteArray -> array + Byte.MIN_VALUE },
            { array: ImmutableCharArray -> array + 'A' },
            { array: ImmutableShortArray -> array + Short.MIN_VALUE },
            { array: ImmutableIntArray -> array + Int.MIN_VALUE },
            { array: ImmutableFloatArray -> array + 0.0f },
            { array: ImmutableLongArray -> array + Long.MIN_VALUE },
            { array: ImmutableDoubleArray -> array + 0.0 },
        )
    }
}

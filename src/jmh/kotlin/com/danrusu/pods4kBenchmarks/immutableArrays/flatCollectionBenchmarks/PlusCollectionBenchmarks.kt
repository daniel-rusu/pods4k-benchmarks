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
open class PlusCollectionBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun plusCollection(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list + list },
            { list: List<Boolean> -> list + list },
            { list: List<Byte> -> list + list },
            { list: List<Char> -> list + list },
            { list: List<Short> -> list + list },
            { list: List<Int> -> list + list },
            { list: List<Float> -> list + list },
            { list: List<Long> -> list + list },
            { list: List<Double> -> list + list },

            // arrays
            { array: Array<String> -> array + array },
            { array: BooleanArray -> array + array },
            { array: ByteArray -> array + array },
            { array: CharArray -> array + array },
            { array: ShortArray -> array + array },
            { array: IntArray -> array + array },
            { array: FloatArray -> array + array },
            { array: LongArray -> array + array },
            { array: DoubleArray -> array + array },

            // immutable arrays
            { array: ImmutableArray<String> -> array + array },
            { array: ImmutableBooleanArray -> array + array },
            { array: ImmutableByteArray -> array + array },
            { array: ImmutableCharArray -> array + array },
            { array: ImmutableShortArray -> array + array },
            { array: ImmutableIntArray -> array + array },
            { array: ImmutableFloatArray -> array + array },
            { array: ImmutableLongArray -> array + array },
            { array: ImmutableDoubleArray -> array + array },
        )
    }
}

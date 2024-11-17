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
import com.danrusu.pods4kBenchmarks.immutableArrays.flatCollectionBenchmarks.setup.FlatCollectionBenchmark
import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import com.danrusu.pods4kBenchmarks.utils.DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH
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
open class FilterBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun filter(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.filter { it.length > DEFAULT_MEDIAN_STRING_LENGTH } },
            { list: List<Boolean> -> list.filter { it } },
            { list: List<Byte> -> list.filter { it >= 0 } },
            { list: List<Char> -> list.filter { it >= DataGenerator.MEDIAN_CHARACTER } },
            { list: List<Short> -> list.filter { it >= 0 } },
            { list: List<Int> -> list.filter { it >= 0 } },
            { list: List<Float> -> list.filter { it >= 0.5f } },
            { list: List<Long> -> list.filter { it >= 0 } },
            { list: List<Double> -> list.filter { it >= 0.5 } },

            // arrays
            { array: Array<String> -> array.filter { it.length >= DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: BooleanArray -> array.filter { it } },
            { array: ByteArray -> array.filter { it >= 0 } },
            { array: CharArray -> array.filter { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ShortArray -> array.filter { it >= 0 } },
            { array: IntArray -> array.filter { it >= 0 } },
            { array: FloatArray -> array.filter { it >= 0.5f } },
            { array: LongArray -> array.filter { it >= 0 } },
            { array: DoubleArray -> array.filter { it >= 0.5 } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.filter { it.length >= DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: ImmutableBooleanArray -> array.filter { it } },
            { array: ImmutableByteArray -> array.filter { it >= 0 } },
            { array: ImmutableCharArray -> array.filter { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ImmutableShortArray -> array.filter { it >= 0 } },
            { array: ImmutableIntArray -> array.filter { it >= 0 } },
            { array: ImmutableFloatArray -> array.filter { it >= 0.5f } },
            { array: ImmutableLongArray -> array.filter { it >= 0 } },
            { array: ImmutableDoubleArray -> array.filter { it >= 0.5 } },
        )
    }
}

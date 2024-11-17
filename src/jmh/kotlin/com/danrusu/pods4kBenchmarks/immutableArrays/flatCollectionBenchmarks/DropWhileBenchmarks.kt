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
open class DropWhileBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun dropWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropWhile { it.length > DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { list: List<Boolean> -> list.dropWhile { it } },
            { list: List<Byte> -> list.dropWhile { it >= 0 } },
            { list: List<Char> -> list.dropWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { list: List<Short> -> list.dropWhile { it >= 0 } },
            { list: List<Int> -> list.dropWhile { it >= 0 } },
            { list: List<Float> -> list.dropWhile { it >= 0.5f } },
            { list: List<Long> -> list.dropWhile { it >= 0 } },
            { list: List<Double> -> list.dropWhile { it >= 0.5 } },

            // arrays
            { array: Array<String> -> array.dropWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: BooleanArray -> array.dropWhile { it } },
            { array: ByteArray -> array.dropWhile { it >= 0 } },
            { array: CharArray -> array.dropWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ShortArray -> array.dropWhile { it >= 0 } },
            { array: IntArray -> array.dropWhile { it >= 0 } },
            { array: FloatArray -> array.dropWhile { it >= 0.5f } },
            { array: LongArray -> array.dropWhile { it >= 0 } },
            { array: DoubleArray -> array.dropWhile { it >= 0.5 } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: ImmutableBooleanArray -> array.dropWhile { it } },
            { array: ImmutableByteArray -> array.dropWhile { it >= 0 } },
            { array: ImmutableCharArray -> array.dropWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ImmutableShortArray -> array.dropWhile { it >= 0 } },
            { array: ImmutableIntArray -> array.dropWhile { it >= 0 } },
            { array: ImmutableFloatArray -> array.dropWhile { it >= 0.5f } },
            { array: ImmutableLongArray -> array.dropWhile { it >= 0 } },
            { array: ImmutableDoubleArray -> array.dropWhile { it >= 0.5 } },
        )
    }
}

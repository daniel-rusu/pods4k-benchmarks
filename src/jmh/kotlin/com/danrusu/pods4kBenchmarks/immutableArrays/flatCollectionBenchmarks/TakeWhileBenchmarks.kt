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
open class TakeWhileBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun takeWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.takeWhile { it.length > DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { list: List<Boolean> -> list.takeWhile { it } },
            { list: List<Byte> -> list.takeWhile { it >= 0 } },
            { list: List<Char> -> list.takeWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { list: List<Short> -> list.takeWhile { it >= 0 } },
            { list: List<Int> -> list.takeWhile { it >= 0 } },
            { list: List<Float> -> list.takeWhile { it >= 0.5f } },
            { list: List<Long> -> list.takeWhile { it >= 0 } },
            { list: List<Double> -> list.takeWhile { it >= 0.5 } },

            // arrays
            { array: Array<String> -> array.takeWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: BooleanArray -> array.takeWhile { it } },
            { array: ByteArray -> array.takeWhile { it >= 0 } },
            { array: CharArray -> array.takeWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ShortArray -> array.takeWhile { it >= 0 } },
            { array: IntArray -> array.takeWhile { it >= 0 } },
            { array: FloatArray -> array.takeWhile { it >= 0.5f } },
            { array: LongArray -> array.takeWhile { it >= 0 } },
            { array: DoubleArray -> array.takeWhile { it >= 0.5 } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.takeWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: ImmutableBooleanArray -> array.takeWhile { it } },
            { array: ImmutableByteArray -> array.takeWhile { it >= 0 } },
            { array: ImmutableCharArray -> array.takeWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ImmutableShortArray -> array.takeWhile { it >= 0 } },
            { array: ImmutableIntArray -> array.takeWhile { it >= 0 } },
            { array: ImmutableFloatArray -> array.takeWhile { it >= 0.5f } },
            { array: ImmutableLongArray -> array.takeWhile { it >= 0 } },
            { array: ImmutableDoubleArray -> array.takeWhile { it >= 0.5 } },
        )
    }
}

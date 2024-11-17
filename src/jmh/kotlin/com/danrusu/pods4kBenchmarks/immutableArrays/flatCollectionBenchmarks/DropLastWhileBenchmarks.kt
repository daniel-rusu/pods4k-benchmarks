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
open class DropLastWhileBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    @Benchmark
    fun dropLastWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropLastWhile { it.length > DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { list: List<Boolean> -> list.dropLastWhile { it } },
            { list: List<Byte> -> list.dropLastWhile { it >= 0 } },
            { list: List<Char> -> list.dropLastWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { list: List<Short> -> list.dropLastWhile { it >= 0 } },
            { list: List<Int> -> list.dropLastWhile { it >= 0 } },
            { list: List<Float> -> list.dropLastWhile { it >= 0.5f } },
            { list: List<Long> -> list.dropLastWhile { it >= 0 } },
            { list: List<Double> -> list.dropLastWhile { it >= 0.5 } },

            // arrays
            { array: Array<String> -> array.dropLastWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: BooleanArray -> array.dropLastWhile { it } },
            { array: ByteArray -> array.dropLastWhile { it >= 0 } },
            { array: CharArray -> array.dropLastWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ShortArray -> array.dropLastWhile { it >= 0 } },
            { array: IntArray -> array.dropLastWhile { it >= 0 } },
            { array: FloatArray -> array.dropLastWhile { it >= 0.5f } },
            { array: LongArray -> array.dropLastWhile { it >= 0 } },
            { array: DoubleArray -> array.dropLastWhile { it >= 0.5 } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropLastWhile { it.length >= DataGenerator.DEFAULT_MEDIAN_STRING_LENGTH } },
            { array: ImmutableBooleanArray -> array.dropLastWhile { it } },
            { array: ImmutableByteArray -> array.dropLastWhile { it >= 0 } },
            { array: ImmutableCharArray -> array.dropLastWhile { it >= DataGenerator.MEDIAN_CHARACTER } },
            { array: ImmutableShortArray -> array.dropLastWhile { it >= 0 } },
            { array: ImmutableIntArray -> array.dropLastWhile { it >= 0 } },
            { array: ImmutableFloatArray -> array.dropLastWhile { it >= 0.5f } },
            { array: ImmutableLongArray -> array.dropLastWhile { it >= 0 } },
            { array: ImmutableDoubleArray -> array.dropLastWhile { it >= 0.5 } },
        )
    }
}

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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataFilter
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducer
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

    override val dataProducer: FlatDataProducer
        get() = FlatDataFilter.generateData(
            // statistically, `dropWhile` will drop about 34 values on average since (0.98)^34 = 50%
            acceptRatio = 0.98,
        )

    @Benchmark
    fun dropWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Boolean> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Byte> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Char> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Short> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Int> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Float> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Long> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Double> -> list.dropWhile { FlatDataFilter.shouldAccept(it) } },

            // arrays
            { array: Array<String> -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: BooleanArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ByteArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: CharArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ShortArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: IntArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: FloatArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: LongArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: DoubleArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableBooleanArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableByteArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableCharArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableShortArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableIntArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableFloatArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableLongArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableDoubleArray -> array.dropWhile { FlatDataFilter.shouldAccept(it) } },
        )
    }
}

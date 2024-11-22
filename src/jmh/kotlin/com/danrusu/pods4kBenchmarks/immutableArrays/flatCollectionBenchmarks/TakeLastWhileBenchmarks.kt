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
open class TakeLastWhileBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    override val dataProducer: FlatDataProducer
        get() = FlatDataFilter.generateData(
            // statistically, `takeLastWhile` will take about 34 values on average since (0.98)^34 = 50%
            acceptRatio = 0.98,
        )

    @Benchmark
    fun takeLastWhile(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Boolean> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Byte> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Char> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Short> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Int> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Float> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Long> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { list: List<Double> -> list.takeLastWhile { FlatDataFilter.shouldAccept(it) } },

            // arrays
            { array: Array<String> -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: BooleanArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ByteArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: CharArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ShortArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: IntArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: FloatArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: LongArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: DoubleArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableBooleanArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableByteArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableCharArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableShortArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableIntArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableFloatArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableLongArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableDoubleArray -> array.takeLastWhile { FlatDataFilter.shouldAccept(it) } },
        )
    }
}

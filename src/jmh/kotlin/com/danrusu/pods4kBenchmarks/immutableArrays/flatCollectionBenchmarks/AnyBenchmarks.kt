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
open class AnyBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    override val dataProducer: FlatDataProducer
        get() = FlatDataFilter.generateData(
            // statistically, `any` will need to inspect about 68 elements on average before finding a match because
            // the probability of finding 68 consecutive non-matching elements is (1 - 0.01)^68 = 50%
            acceptRatio = 0.01,
        )

    @Benchmark
    fun any(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Boolean> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Byte> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Char> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Short> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Int> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Float> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Long> -> list.any { FlatDataFilter.shouldAccept(it) } },
            { list: List<Double> -> list.any { FlatDataFilter.shouldAccept(it) } },

            // arrays
            { array: Array<String> -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: BooleanArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ByteArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: CharArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ShortArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: IntArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: FloatArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: LongArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: DoubleArray -> array.any { FlatDataFilter.shouldAccept(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableBooleanArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableByteArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableCharArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableShortArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableIntArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableFloatArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableLongArray -> array.any { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableDoubleArray -> array.any { FlatDataFilter.shouldAccept(it) } },
        )
    }
}

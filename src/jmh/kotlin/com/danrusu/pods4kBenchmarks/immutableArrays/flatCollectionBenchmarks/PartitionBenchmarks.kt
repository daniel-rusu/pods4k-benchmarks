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
open class PartitionBenchmarks : FlatCollectionBenchmark() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    override val dataProducer: FlatDataProducer
        get() = FlatDataFilter.generateData(
            // statistically, about half of the values will be on the left and the other half on the right on average
            acceptRatio = 0.5,
        )

    @Benchmark
    fun partition(bh: Blackhole) {
        transformEachCollection(
            bh,
            // lists
            { list: List<String> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Boolean> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Byte> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Char> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Short> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Int> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Float> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Long> -> list.partition { FlatDataFilter.shouldAccept(it) } },
            { list: List<Double> -> list.partition { FlatDataFilter.shouldAccept(it) } },

            // arrays
            { array: Array<String> -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: BooleanArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ByteArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: CharArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ShortArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: IntArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: FloatArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: LongArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: DoubleArray -> array.partition { FlatDataFilter.shouldAccept(it) } },

            // immutable arrays
            { array: ImmutableArray<String> -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableBooleanArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableByteArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableCharArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableShortArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableIntArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableFloatArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableLongArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
            { array: ImmutableDoubleArray -> array.partition { FlatDataFilter.shouldAccept(it) } },
        )
    }
}

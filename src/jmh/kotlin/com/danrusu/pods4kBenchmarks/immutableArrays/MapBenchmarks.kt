package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.ObjectCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.dataProducers.CompoundElement
import com.danrusu.pods4kBenchmarks.immutableArrays.benchmarkTypes.dataProducers.ObjectProducer
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
open class MapBenchmarks : ObjectCollectionBenchmark<CompoundElement>() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    override val elementProducer: Pair<ObjectProducer<CompoundElement>, Class<CompoundElement>>
        get() = Pair(ObjectProducer.CompoundElementProducer, CompoundElement::class.java)

    @Benchmark
    fun mapReference(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.referenceValue } },
            { array: Array<CompoundElement> -> array.map { it.referenceValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.referenceValue } },
        )
    }

    @Benchmark
    fun mapBoolean(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.booleanValue } },
            { array: Array<CompoundElement> -> array.map { it.booleanValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.booleanValue } },
        )
    }

    @Benchmark
    fun mapByte(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.byteValue } },
            { array: Array<CompoundElement> -> array.map { it.byteValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.byteValue } },
        )
    }

    @Benchmark
    fun mapChar(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.charValue } },
            { array: Array<CompoundElement> -> array.map { it.charValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.charValue } },
        )
    }

    @Benchmark
    fun mapShort(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.shortValue } },
            { array: Array<CompoundElement> -> array.map { it.shortValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.shortValue } },
        )
    }

    @Benchmark
    fun mapInt(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.intValue } },
            { array: Array<CompoundElement> -> array.map { it.intValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.intValue } },
        )
    }

    @Benchmark
    fun mapFloat(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.floatValue } },
            { array: Array<CompoundElement> -> array.map { it.floatValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.floatValue } },
        )
    }

    @Benchmark
    fun mapLong(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.longValue } },
            { array: Array<CompoundElement> -> array.map { it.longValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.longValue } },
        )
    }

    @Benchmark
    fun mapDouble(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElement> -> list.map { it.doubleValue } },
            { array: Array<CompoundElement> -> array.map { it.doubleValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.doubleValue } },
        )
    }
}

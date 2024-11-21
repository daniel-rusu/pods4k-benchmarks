package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.mapNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.CompoundElementOfNullableValues
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.CompoundElementOfNullableValuesProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.ObjectCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.ObjectProducer
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
open class MapNotNullBenchmarks : ObjectCollectionBenchmark<CompoundElementOfNullableValues>() {
    override val numCollections: Int
        get() = NUM_COLLECTIONS

    override val objectProducer: ObjectProducer<CompoundElementOfNullableValues>
        get() = CompoundElementOfNullableValuesProducer(nullRatio = 0.5)

    @Benchmark
    fun mapNotNullReference(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableReference } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableReference } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableReference } },
        )
    }

    @Benchmark
    fun mapNotNullBoolean(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableBoolean } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableBoolean } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableBoolean } },
        )
    }

    @Benchmark
    fun mapNotNullByte(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableByte } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableByte } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableByte } },
        )
    }

    @Benchmark
    fun mapNotNullChar(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableChar } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableChar } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableChar } },
        )
    }

    @Benchmark
    fun mapNotNullShort(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableShort } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableShort } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableShort } },
        )
    }

    @Benchmark
    fun mapNotNullInt(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableInt } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableInt } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableInt } },
        )
    }

    @Benchmark
    fun mapNotNullFloat(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableFloat } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableFloat } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableFloat } },
        )
    }

    @Benchmark
    fun mapNotNullLong(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableLong } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableLong } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableLong } },
        )
    }

    @Benchmark
    fun mapNotNullDouble(bh: Blackhole) {
        transformEachCollection(
            bh,
            { list: List<CompoundElementOfNullableValues> -> list.mapNotNull { it.nullableDouble } },
            { array: Array<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableDouble } },
            { array: ImmutableArray<CompoundElementOfNullableValues> -> array.mapNotNull { it.nullableDouble } },
        )
    }
}

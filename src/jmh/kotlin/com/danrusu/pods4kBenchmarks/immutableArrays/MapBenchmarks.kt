package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.ObjectCollections
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.CompoundElement
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.dataProducers.ObjectProducer
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
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
@State(Scope.Benchmark)
open class MapBenchmarks {
    @Param("LIST", "ARRAY", "IMMUTABLE_ARRAY")
    private lateinit var collectionType: CollectionType

    private lateinit var data: ObjectCollections<CompoundElement>

    @Setup(Level.Trial)
    fun setupCollections() {
        data = ObjectCollections(
            numCollections = NUM_COLLECTIONS,
            type = collectionType,
            objectProducer = ObjectProducer.CompoundElementProducer,
            objectClass = CompoundElement::class.java,
        )
    }

    @TearDown
    fun tearDown() {
    }

    @Benchmark
    fun mapReference(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.referenceValue } },
            { array: Array<CompoundElement> -> array.map { it.referenceValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.referenceValue } },
        )
    }

    @Benchmark
    fun mapBoolean(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.booleanValue } },
            { array: Array<CompoundElement> -> array.map { it.booleanValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.booleanValue } },
        )
    }

    @Benchmark
    fun mapByte(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.byteValue } },
            { array: Array<CompoundElement> -> array.map { it.byteValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.byteValue } },
        )
    }

    @Benchmark
    fun mapChar(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.charValue } },
            { array: Array<CompoundElement> -> array.map { it.charValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.charValue } },
        )
    }

    @Benchmark
    fun mapShort(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.shortValue } },
            { array: Array<CompoundElement> -> array.map { it.shortValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.shortValue } },
        )
    }

    @Benchmark
    fun mapInt(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.intValue } },
            { array: Array<CompoundElement> -> array.map { it.intValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.intValue } },
        )
    }

    @Benchmark
    fun mapFloat(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.floatValue } },
            { array: Array<CompoundElement> -> array.map { it.floatValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.floatValue } },
        )
    }

    @Benchmark
    fun mapLong(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.longValue } },
            { array: Array<CompoundElement> -> array.map { it.longValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.longValue } },
        )
    }

    @Benchmark
    fun mapDouble(bh: Blackhole) {
        data.transformEachCollectionOfCollectionType(
            bh,
            collectionType,
            { list: List<CompoundElement> -> list.map { it.doubleValue } },
            { array: Array<CompoundElement> -> array.map { it.doubleValue } },
            { array: ImmutableArray<CompoundElement> -> array.map { it.doubleValue } },
        )
    }
}

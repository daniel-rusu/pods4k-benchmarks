package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionsByCollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CompoundElement
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.ObjectProducer
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.CollectionType.LIST
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

    private lateinit var data: CollectionsByCollectionType<CompoundElement>

    @Setup(Level.Trial)
    fun setupCollections() {
        data = CollectionsByCollectionType(
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
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.referenceValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.referenceValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.referenceValue }
            }
        }
    }

    @Benchmark
    fun mapBoolean(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.booleanValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.booleanValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.booleanValue }
            }
        }
    }

    @Benchmark
    fun mapByte(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.byteValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.byteValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.byteValue }
            }
        }
    }

    @Benchmark
    fun mapChar(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.charValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.charValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.charValue }
            }
        }
    }

    @Benchmark
    fun mapShort(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.shortValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.shortValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.shortValue }
            }
        }
    }

    @Benchmark
    fun mapInt(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.intValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.intValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.intValue }
            }
        }
    }

    @Benchmark
    fun mapFloat(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.floatValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.floatValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.floatValue }
            }
        }
    }

    @Benchmark
    fun mapLong(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.longValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.longValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.longValue }
            }
        }
    }

    @Benchmark
    fun mapDouble(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.transformEachList(bh) { list -> list.map { it.doubleValue } }
            ARRAY -> data.transformEachArray(bh) { array -> array.map { it.doubleValue } }
            IMMUTABLE_ARRAY -> data.transformEachImmutableArray(bh) { immutableArray ->
                immutableArray.map { it.doubleValue }
            }
        }
    }
}

package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.Collections
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CompoundElement
import com.danrusu.pods4kBenchmarks.utils.Distribution
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

@BenchmarkMode(Mode.Throughput) // Measure operations per second
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS) // The # of operations being performed for each invocation
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS) // Warmup each benchmark for 2 seconds 7 times
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS) // Run each benchmark for 1 second 7 times
@Fork(2) // Create 2 JVM processes to isolate and repeat the entire benchmark
@State(Scope.Benchmark)
open class MapBenchmarks {
    @Param("LIST", "ARRAY", "IMMUTABLE_ARRAY")
    private lateinit var collectionType: CollectionType

    private lateinit var data: Collections<CompoundElement>

    @Setup(Level.Trial)
    fun setupCollections() {
        data = Collections(
            numCollections = NUM_COLLECTIONS,
            type = collectionType,
            sizeDistribution = Distribution.LIST_SIZE_DISTRIBUTION,
            createList = { random, size ->
                (1..size).map { CompoundElement.create(random) }
            },
            createArray = { random, size ->
                Array(size) { CompoundElement.create(random) }
            },
            createImmutableArray = { random, size ->
                ImmutableArray(size) { CompoundElement.create(random) }
            }
        )
    }

    @TearDown
    fun tearDown() {
    }

    @Benchmark
    fun mapReference(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.referenceValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.referenceValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.referenceValue })
            }
        }
    }

    @Benchmark
    fun mapBoolean(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.booleanValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.booleanValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.booleanValue })
            }
        }
    }

    @Benchmark
    fun mapByte(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.byteValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.byteValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.byteValue })
            }
        }
    }

    @Benchmark
    fun mapChar(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.charValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.charValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.charValue })
            }
        }
    }

    @Benchmark
    fun mapShort(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.shortValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.shortValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.shortValue })
            }
        }
    }

    @Benchmark
    fun mapInt(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.intValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.intValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.intValue })
            }
        }
    }

    @Benchmark
    fun mapFloat(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.floatValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.floatValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.floatValue })
            }
        }
    }

    @Benchmark
    fun mapLong(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.longValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.longValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.longValue })
            }
        }
    }

    @Benchmark
    fun mapDouble(bh: Blackhole) {
        when (collectionType) {
            LIST -> data.forEachList { list -> bh.consume(list.map { it.doubleValue }) }
            ARRAY -> data.forEachArray { array -> bh.consume(array.map { it.doubleValue }) }
            IMMUTABLE_ARRAY -> data.forEachImmutableArray { immutableArray ->
                bh.consume(immutableArray.map { it.doubleValue })
            }
        }
    }
}
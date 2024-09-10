package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.data.CompoundElement
import com.danrusu.pods4kBenchmarks.immutableArrays.data.ElementCollections
import com.danrusu.pods4kBenchmarks.utils.Distribution
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OperationsPerInvocation
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

private const val NUM_COLLECTIONS = 1000

@BenchmarkMode(Mode.Throughput) // Measure operations per second
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS) // The # of operations being performed for each invocation
@Warmup(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS) // Warmup each benchmark for 1 second 7 times
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS) // Run each benchmark for 1 second 5 times
@Fork(2) // Create 2 JVM processes to isolate and repeat the entire benchmark
@State(Scope.Benchmark)
open class MapBenchmark {
    /**
     * For the benchmark data, create a bunch of different sized collections that are aligned with the distribution
     * that's seen in the real world and measure the performance of operating on all of them.
     *
     * These are all created in advance to avoid generating them for each invocation which would muddy the results.
     */
    private var collections: Array<ElementCollections> = emptyArray<ElementCollections>()

    @Setup(Level.Trial)
    fun setup() {
        // Use constant seed so the data is identical for all benchmarks since they're compared against each other
        val random = Random(0)

        collections = Array(NUM_COLLECTIONS) {
            val numElements = Distribution.LIST_SIZE_DISTRIBUTION.nextValue(random)
            val regularArray = Array(numElements) { CompoundElement.create(random) }

            ElementCollections(
                list = (0..<numElements).map { index -> regularArray[index] },
                regularArray = regularArray,
                immutableArray = ImmutableArray(numElements) { index -> regularArray[index] },
            )
        }
    }

    @Benchmark
    fun listMapReference(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.referenceValue })
        }
    }

    @Benchmark
    fun listMapBoolean(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.booleanValue })
        }
    }

    @Benchmark
    fun listMapByte(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.byteValue })
        }
    }

    @Benchmark
    fun listMapChar(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.charValue })
        }
    }

    @Benchmark
    fun listMapShort(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.shortValue })
        }
    }

    @Benchmark
    fun listMapInt(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.intValue })
        }
    }

    @Benchmark
    fun listMapFloat(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.floatValue })
        }
    }

    @Benchmark
    fun listMapLong(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.longValue })
        }
    }

    @Benchmark
    fun listMapDouble(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.list.map { it.doubleValue })
        }
    }

    @Benchmark
    fun arrayMapReference(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.referenceValue })
        }
    }

    @Benchmark
    fun arrayMapBoolean(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.booleanValue })
        }
    }

    @Benchmark
    fun arrayMapByte(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.byteValue })
        }
    }

    @Benchmark
    fun arrayMapChar(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.charValue })
        }
    }

    @Benchmark
    fun arrayMapShort(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.shortValue })
        }
    }

    @Benchmark
    fun arrayMapInt(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.intValue })
        }
    }

    @Benchmark
    fun arrayMapFloat(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.floatValue })
        }
    }

    @Benchmark
    fun arrayMapLong(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.longValue })
        }
    }

    @Benchmark
    fun arrayMapDouble(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.regularArray.map { it.doubleValue })
        }
    }

    @Benchmark
    fun immutableArrayMapReference(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.referenceValue })
        }
    }

    @Benchmark
    fun immutableArrayMapBoolean(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.booleanValue })
        }
    }

    @Benchmark
    fun immutableArrayMapByte(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.byteValue })
        }
    }

    @Benchmark
    fun immutableArrayMapChar(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.charValue })
        }
    }

    @Benchmark
    fun immutableArrayMapShort(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.shortValue })
        }
    }

    @Benchmark
    fun immutableArrayMapInt(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.intValue })
        }
    }

    @Benchmark
    fun immutableArrayMapFloat(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.floatValue })
        }
    }

    @Benchmark
    fun immutableArrayMapLong(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.longValue })
        }
    }

    @Benchmark
    fun immutableArrayMapDouble(bh: Blackhole) {
        for (state in collections) {
            bh.consume(state.immutableArray.map { it.doubleValue })
        }
    }

    @TearDown
    fun tearDown() {
        collections = emptyArray()
    }
}

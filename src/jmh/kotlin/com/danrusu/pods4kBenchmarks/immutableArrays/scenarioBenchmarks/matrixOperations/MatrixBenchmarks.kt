package com.danrusu.pods4kBenchmarks.immutableArrays.scenarioBenchmarks.matrixOperations

import com.danrusu.pods4kBenchmarks.immutableArrays.scenarioBenchmarks.matrixOperations.setup.ImmutableArrayBasedMatrix
import com.danrusu.pods4kBenchmarks.immutableArrays.scenarioBenchmarks.matrixOperations.setup.ListBasedMatrix
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Level
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.TearDown
import org.openjdk.jmh.annotations.Warmup
import org.openjdk.jmh.infra.Blackhole
import java.util.concurrent.TimeUnit
import kotlin.random.Random

/**
 * The matrices will have N X N dimensions.
 *
 * This should be large enough to represent numeric data-intensive algorithms such as those manipulating image pixels.
 */
private const val N = 1000

/**
 * Multiply non-trivial matrices with the matrix representation swapping between lists of lists to immutable array of
 * immutable arrays.
 *
 * This is intended to represent the potential impact of updating numeric memory-intensive algorithms from using lists
 * to instead use immutable arrays.
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MINUTES)
@Warmup(iterations = 10, time = 15, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 15, timeUnit = TimeUnit.SECONDS)
@Fork(2)
@State(Scope.Benchmark)
open class MatrixBenchmarks {
    @Param("LIST", "IMMUTABLE_ARRAY")
    private lateinit var collectionType: CollectionType

    private var firstListBasedMatrix = ListBasedMatrix.EMPTY
    private var secondListBasedMatrix = ListBasedMatrix.EMPTY

    private var firstImmutableArrayBasedMatrix: ImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.EMPTY
    private var secondImmutableArrayBasedMatrix: ImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.EMPTY

    @Setup(Level.Trial)
    fun setupCollections() {
        // Use constant seed so the data is identical across multiple runs
        val random = Random(0)
        when (collectionType) {
            LIST -> {
                firstListBasedMatrix = ListBasedMatrix.create(createMatrixData(random))
                secondListBasedMatrix = ListBasedMatrix.create(createMatrixData(random))
            }

            IMMUTABLE_ARRAY -> {
                firstImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.create(createMatrixData(random))
                secondImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.create(createMatrixData(random))
            }

            else -> throw IllegalStateException("Unexpected collection type: $collectionType")
        }
    }

    private fun createMatrixData(random: Random): Array<FloatArray> {
        return Array(N) { FloatArray(N) { random.nextFloat() } }
    }

    @TearDown
    fun tearDown() {
        firstListBasedMatrix = ListBasedMatrix.EMPTY
        secondListBasedMatrix = ListBasedMatrix.EMPTY
        firstImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.EMPTY
        secondImmutableArrayBasedMatrix = ImmutableArrayBasedMatrix.EMPTY
    }

    @Benchmark
    fun multiply(bh: Blackhole) {
        when (collectionType) {
            LIST -> bh.consume(firstListBasedMatrix * secondListBasedMatrix)
            IMMUTABLE_ARRAY -> bh.consume(firstImmutableArrayBasedMatrix * secondImmutableArrayBasedMatrix)
            else -> throw IllegalStateException("Unexpected collection type: $collectionType")
        }
    }
}

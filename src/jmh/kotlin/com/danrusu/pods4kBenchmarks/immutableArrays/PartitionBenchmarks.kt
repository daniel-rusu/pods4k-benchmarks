package com.danrusu.pods4kBenchmarks.immutableArrays

import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.CollectionsByDataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.commonData.benchmarkParameters.DataType.SHORT
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
open class PartitionBenchmarks {
    @Param("REFERENCE", "BOOLEAN", "BYTE", "CHAR", "SHORT", "INT", "FLOAT", "LONG", "DOUBLE")
    private lateinit var dataType: DataType

    private lateinit var data: CollectionsByDataType

    @Setup(Level.Trial)
    fun setupCollections() {
        data = CollectionsByDataType(numCollections = NUM_COLLECTIONS, dataType = dataType)
    }

    @Benchmark
    fun listPartition(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.forEachList { wrapper ->
                bh.consume(wrapper.referenceList.partition { it.length % 2 == 0 })
            }

            BOOLEAN -> data.forEachList { wrapper ->
                bh.consume(wrapper.booleanList.partition { it })
            }

            BYTE -> data.forEachList { wrapper ->
                bh.consume(wrapper.byteList.partition { it >= 0 })
            }

            CHAR -> data.forEachList { wrapper ->
                bh.consume(wrapper.charList.partition { it.code % 2 == 0 })
            }

            SHORT -> data.forEachList { wrapper ->
                bh.consume(wrapper.shortList.partition { it >= 0 })
            }

            INT -> data.forEachList { wrapper ->
                bh.consume(wrapper.intList.partition { it >= 0 })
            }

            FLOAT -> data.forEachList { wrapper ->
                bh.consume(wrapper.floatList.partition { it >= 0.5f })
            }

            LONG -> data.forEachList { wrapper ->
                bh.consume(wrapper.longList.partition { it >= 0L })
            }

            DOUBLE -> data.forEachList { wrapper ->
                bh.consume(wrapper.doubleList.partition { it >= 0.5 })
            }
        }
    }

    @Benchmark
    fun arrayPartition(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.forEachArray { wrapper ->
                bh.consume(wrapper.referenceArray.partition { it.length % 2 == 0 })
            }

            BOOLEAN -> data.forEachArray { wrapper ->
                bh.consume(wrapper.booleanArray.partition { it })
            }

            BYTE -> data.forEachArray { wrapper ->
                bh.consume(wrapper.byteArray.partition { it >= 0 })
            }

            CHAR -> data.forEachArray { wrapper ->
                bh.consume(wrapper.charArray.partition { it.code % 2 == 0 })
            }

            SHORT -> data.forEachArray { wrapper ->
                bh.consume(wrapper.shortArray.partition { it >= 0 })
            }

            INT -> data.forEachArray { wrapper ->
                bh.consume(wrapper.intArray.partition { it >= 0 })
            }

            FLOAT -> data.forEachArray { wrapper ->
                bh.consume(wrapper.floatArray.partition { it >= 0.5f })
            }

            LONG -> data.forEachArray { wrapper ->
                bh.consume(wrapper.longArray.partition { it >= 0L })
            }

            DOUBLE -> data.forEachArray { wrapper ->
                bh.consume(wrapper.doubleArray.partition { it >= 0.5 })
            }
        }
    }

    @Benchmark
    fun immutableArrayPartition(bh: Blackhole) {
        when (dataType) {
            REFERENCE -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableReferenceArray.partition { it.length % 2 == 0 })
            }

            BOOLEAN -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableBooleanArray.partition { it })
            }

            BYTE -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableByteArray.partition { it >= 0 })
            }

            CHAR -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableCharArray.partition { it.code % 2 == 0 })
            }

            SHORT -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableShortArray.partition { it >= 0 })
            }

            INT -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableIntArray.partition { it >= 0 })
            }

            FLOAT -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableFloatArray.partition { it >= 0.5f })
            }

            LONG -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableLongArray.partition { it >= 0L })
            }

            DOUBLE -> data.forEachImmutableArray { wrapper ->
                bh.consume(wrapper.immutableDoubleArray.partition { it >= 0.5 })
            }
        }
    }
}

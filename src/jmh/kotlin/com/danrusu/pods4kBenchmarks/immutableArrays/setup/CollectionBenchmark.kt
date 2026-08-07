package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State

/** Base for benchmarks that run across every [CollectionType]/[DataType] combination. */
@State(Scope.Benchmark)
abstract class CollectionBenchmark {
    /** Repeats each benchmark for every collection representation. */
    @Param
    protected lateinit var collectionType: CollectionType

    /** Repeats each benchmark for `REFERENCE` and the eight primitive families. */
    @Param
    protected lateinit var dataType: DataType
}

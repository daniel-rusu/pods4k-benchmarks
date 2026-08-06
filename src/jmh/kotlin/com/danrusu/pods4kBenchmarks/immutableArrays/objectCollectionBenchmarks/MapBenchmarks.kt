package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.multiplicativeSpecializations.map
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.CompoundElement
import com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks.setup.ObjectCollectionBenchmark
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList
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
open class MapBenchmarks : ObjectCollectionBenchmark<CompoundElement>(
    numCollections = NUM_COLLECTIONS,
    objectGeneratorFactory = ObjectGeneratorFactory.of<CompoundElement> { fieldGenerator, referenceGenerator ->
        CompoundElement(
            referenceValue = referenceGenerator.next(),
            booleanValue = fieldGenerator.nextBoolean(),
            byteValue = fieldGenerator.nextByte(),
            charValue = fieldGenerator.nextChar(),
            shortValue = fieldGenerator.nextShort(),
            intValue = fieldGenerator.nextInt(),
            floatValue = fieldGenerator.nextFloat(),
            longValue = fieldGenerator.nextLong(),
            doubleValue = fieldGenerator.nextDouble(),
        )
    },
) {
    @Benchmark
    fun map(bh: Blackhole) {
        when (dataType) {
            DataType.REFERENCE -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.referenceValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.referenceValue } },
                { array: Array<CompoundElement> -> array.map { it.referenceValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.referenceValue } },
            )

            DataType.BOOLEAN -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.booleanValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.booleanValue } },
                { array: Array<CompoundElement> -> array.map { it.booleanValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.booleanValue } },
            )

            DataType.BYTE -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.byteValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.byteValue } },
                { array: Array<CompoundElement> -> array.map { it.byteValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.byteValue } },
            )

            DataType.CHAR -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.charValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.charValue } },
                { array: Array<CompoundElement> -> array.map { it.charValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.charValue } },
            )

            DataType.SHORT -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.shortValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.shortValue } },
                { array: Array<CompoundElement> -> array.map { it.shortValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.shortValue } },
            )

            DataType.INT -> {
                transformEachCollection(
                    bh,
                    { list: List<CompoundElement> -> list.map { it.intValue } },
                    { list: PersistentList<CompoundElement> -> list.map { it.intValue } },
                    { array: Array<CompoundElement> -> array.map { it.intValue } },
                    { array: ImmutableArray<CompoundElement> -> array.map { it.intValue } },
                )
            }

            DataType.FLOAT -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.floatValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.floatValue } },
                { array: Array<CompoundElement> -> array.map { it.floatValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.floatValue } },
            )

            DataType.LONG -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.longValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.longValue } },
                { array: Array<CompoundElement> -> array.map { it.longValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.longValue } },
            )

            DataType.DOUBLE -> transformEachCollection(
                bh,
                { list: List<CompoundElement> -> list.map { it.doubleValue } },
                { list: PersistentList<CompoundElement> -> list.map { it.doubleValue } },
                { array: Array<CompoundElement> -> array.map { it.doubleValue } },
                { array: ImmutableArray<CompoundElement> -> array.map { it.doubleValue } },
            )
        }
    }
}

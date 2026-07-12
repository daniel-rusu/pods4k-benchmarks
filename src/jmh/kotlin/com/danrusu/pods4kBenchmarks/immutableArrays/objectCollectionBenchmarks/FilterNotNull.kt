package com.danrusu.pods4kBenchmarks.immutableArrays.objectCollectionBenchmarks

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.filterNotNull
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.IMMUTABLE_ARRAY
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType.PERSISTENT_LIST
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BOOLEAN
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.BYTE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.CHAR
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.DOUBLE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.FLOAT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.INT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.LONG
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.REFERENCE
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType.SHORT
import com.danrusu.pods4kBenchmarks.utils.Distribution
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.nullable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
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

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(NUM_COLLECTIONS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 7, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
open class FilterNotNull {
    /** Repeat the benchmark for each collection type */
    @Param
    private lateinit var collectionType: CollectionType

    /** Repeat the benchmark for each of the 8 base data types plus a String reference type */
    @Param
    private lateinit var dataType: DataType

    private val sizeDistributionFactory = DistributionFactory.ListSizeDistribution

    /** Creates simple field generators for nullable primitive wrapper elements. */
    private val fieldGeneratorFactory = FieldGeneratorFactory.withRandomNullableFields(nullRatio = 0.5)

    /** Creates reference generators for nullable string elements. */
    private val referenceGeneratorFactory = ObjectGeneratorFactory.randomStrings().nullable(nullRatio = 0.5)

    /** Using a single list type for each value type as statically-typed lists will never make any difference*/
    private lateinit var listData: Array<List<Any?>>
    private lateinit var persistentListData: Array<PersistentList<Any?>>

    private lateinit var referenceArrays: Array<Array<String?>>
    private lateinit var booleanArrays: Array<Array<Boolean?>>
    private lateinit var byteArrays: Array<Array<Byte?>>
    private lateinit var charArrays: Array<Array<Char?>>
    private lateinit var shortArrays: Array<Array<Short?>>
    private lateinit var intArrays: Array<Array<Int?>>
    private lateinit var floatArrays: Array<Array<Float?>>
    private lateinit var longArrays: Array<Array<Long?>>
    private lateinit var doubleArrays: Array<Array<Double?>>

    private lateinit var immutableReferenceArrays: Array<ImmutableArray<String?>>
    private lateinit var immutableBooleanArrays: Array<ImmutableArray<Boolean?>>
    private lateinit var immutableByteArrays: Array<ImmutableArray<Byte?>>
    private lateinit var immutableCharArrays: Array<ImmutableArray<Char?>>
    private lateinit var immutableShortArrays: Array<ImmutableArray<Short?>>
    private lateinit var immutableIntArrays: Array<ImmutableArray<Int?>>
    private lateinit var immutableFloatArrays: Array<ImmutableArray<Float?>>
    private lateinit var immutableLongArrays: Array<ImmutableArray<Long?>>
    private lateinit var immutableDoubleArrays: Array<ImmutableArray<Double?>>

    @Setup(Level.Trial)
    fun setupCollections() {
        val rngFactory = RngFactory()
        val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
        val sizeDistribution = sizeDistributionFactory.create(rngFactory)
        val fields = fieldGeneratorFactory.create(generatorRngs)
        val references = referenceGeneratorFactory.create(generatorRngs)

        when (collectionType) {
            LIST -> createLists(sizeDistribution, fields, references)
            PERSISTENT_LIST -> createPersistentLists(sizeDistribution, fields, references)
            ARRAY -> createArrays(sizeDistribution, fields, references)
            IMMUTABLE_ARRAY -> createImmutableArrays(sizeDistribution, fields, references)
        }
    }

    @TearDown
    fun tearDown() {
        listData = emptyArray()
        persistentListData = emptyArray()

        referenceArrays = emptyArray()
        booleanArrays = emptyArray()
        byteArrays = emptyArray()
        charArrays = emptyArray()
        shortArrays = emptyArray()
        intArrays = emptyArray()
        floatArrays = emptyArray()
        longArrays = emptyArray()
        doubleArrays = emptyArray()

        immutableReferenceArrays = emptyArray()
        immutableBooleanArrays = emptyArray()
        immutableByteArrays = emptyArray()
        immutableCharArrays = emptyArray()
        immutableShortArrays = emptyArray()
        immutableIntArrays = emptyArray()
        immutableFloatArrays = emptyArray()
        immutableLongArrays = emptyArray()
        immutableDoubleArrays = emptyArray()
    }

    @Benchmark
    @Suppress("UNCHECKED_CAST")
    fun filterNotNull(bh: Blackhole) {
        when (collectionType) {
            LIST -> {
                // no need to check the dataType since we always use the same listData
                listData.forEach { bh.consume(it.filterNotNull()) }
            }

            PERSISTENT_LIST -> {
                // no need to check the dataType since we always use persistentListData
                persistentListData.forEach { bh.consume(it.filterNotNull()) }
            }

            ARRAY -> when (dataType) {
                REFERENCE -> referenceArrays.forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> booleanArrays.forEach { bh.consume(it.filterNotNull()) }
                BYTE -> byteArrays.forEach { bh.consume(it.filterNotNull()) }
                CHAR -> charArrays.forEach { bh.consume(it.filterNotNull()) }
                SHORT -> shortArrays.forEach { bh.consume(it.filterNotNull()) }
                INT -> intArrays.forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> floatArrays.forEach { bh.consume(it.filterNotNull()) }
                LONG -> longArrays.forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> doubleArrays.forEach { bh.consume(it.filterNotNull()) }
            }

            IMMUTABLE_ARRAY -> when (dataType) {
                REFERENCE -> immutableReferenceArrays.forEach { bh.consume(it.filterNotNull()) }
                BOOLEAN -> immutableBooleanArrays.forEach { bh.consume(it.filterNotNull()) }
                BYTE -> immutableByteArrays.forEach { bh.consume(it.filterNotNull()) }
                CHAR -> immutableCharArrays.forEach { bh.consume(it.filterNotNull()) }
                SHORT -> immutableShortArrays.forEach { bh.consume(it.filterNotNull()) }
                INT -> immutableIntArrays.forEach { bh.consume(it.filterNotNull()) }
                FLOAT -> immutableFloatArrays.forEach { bh.consume(it.filterNotNull()) }
                LONG -> immutableLongArrays.forEach { bh.consume(it.filterNotNull()) }
                DOUBLE -> immutableDoubleArrays.forEach { bh.consume(it.filterNotNull()) }
            }
        }
    }

    private fun createLists(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String?>,
    ) {
        listData = Array(NUM_COLLECTIONS) { index ->
            when (dataType) {
                REFERENCE -> createList(sizeDistribution.nextValue()) { references.next() }
                BOOLEAN -> createList(sizeDistribution.nextValue()) { fields.nextNullableBoolean() }
                BYTE -> createList(sizeDistribution.nextValue()) { fields.nextNullableByte() }
                CHAR -> createList(sizeDistribution.nextValue()) { fields.nextNullableChar() }
                SHORT -> createList(sizeDistribution.nextValue()) { fields.nextNullableShort() }
                INT -> createList(sizeDistribution.nextValue()) { fields.nextNullableInt() }
                FLOAT -> createList(sizeDistribution.nextValue()) { fields.nextNullableFloat() }
                LONG -> createList(sizeDistribution.nextValue()) { fields.nextNullableLong() }
                DOUBLE -> createList(sizeDistribution.nextValue()) { fields.nextNullableDouble() }
            }
        }
    }

    private inline fun <T> createList(
        listSize: Int,
        initializer: () -> T,
    ): List<T> {
        val result = ArrayList<T>(listSize)
        repeat(listSize) { result.add(initializer()) }
        return result
    }

    private fun createPersistentLists(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String?>,
    ) {
        persistentListData = Array(NUM_COLLECTIONS) { index ->
            when (dataType) {
                REFERENCE -> createPersistentList(sizeDistribution.nextValue()) { references.next() }
                BOOLEAN -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableBoolean() }
                BYTE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableByte() }
                CHAR -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableChar() }
                SHORT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableShort() }
                INT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableInt() }
                FLOAT -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableFloat() }
                LONG -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableLong() }
                DOUBLE -> createPersistentList(sizeDistribution.nextValue()) { fields.nextNullableDouble() }
            }
        }
    }

    private inline fun <T> createPersistentList(listSize: Int, crossinline initializer: () -> T): PersistentList<T> {
        val builder = persistentListOf<T>().builder()
        repeat(listSize) { builder.add(initializer()) }
        return builder.build()
    }

    private fun createArrays(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String?>,
    ) {
        if (dataType == REFERENCE) {
            referenceArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { references.next() }
            }
        } else if (dataType == BOOLEAN) {
            booleanArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableBoolean() }
            }
        } else if (dataType == BYTE) {
            byteArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableByte() }
            }
        } else if (dataType == CHAR) {
            charArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableChar() }
            }
        } else if (dataType == SHORT) {
            shortArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableShort() }
            }
        } else if (dataType == INT) {
            intArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableInt() }
            }
        } else if (dataType == FLOAT) {
            floatArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableFloat() }
            }
        } else if (dataType == LONG) {
            longArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableLong() }
            }
        } else if (dataType == DOUBLE) {
            doubleArrays = Array(NUM_COLLECTIONS) { index ->
                Array(sizeDistribution.nextValue()) { fields.nextNullableDouble() }
            }
        }
    }

    private fun createImmutableArrays(
        sizeDistribution: Distribution,
        fields: FieldGenerator,
        references: ObjectGenerator<String?>,
    ) {
        if (dataType == REFERENCE) {
            immutableReferenceArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { references.next() }
            }
        } else if (dataType == BOOLEAN) {
            immutableBooleanArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableBoolean() }
            }
        } else if (dataType == BYTE) {
            immutableByteArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableByte() }
            }
        } else if (dataType == CHAR) {
            immutableCharArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableChar() }
            }
        } else if (dataType == SHORT) {
            immutableShortArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableShort() }
            }
        } else if (dataType == INT) {
            immutableIntArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableInt() }
            }
        } else if (dataType == FLOAT) {
            immutableFloatArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableFloat() }
            }
        } else if (dataType == LONG) {
            immutableLongArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableLong() }
            }
        } else if (dataType == DOUBLE) {
            immutableDoubleArrays = Array(NUM_COLLECTIONS) { index ->
                ImmutableArray(sizeDistribution.nextValue()) { fields.nextNullableDouble() }
            }
        }
    }
}

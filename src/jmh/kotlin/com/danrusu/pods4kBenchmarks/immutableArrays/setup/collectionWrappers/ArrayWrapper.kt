package com.danrusu.pods4kBenchmarks.immutableArrays.setup.collectionWrappers

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
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.FlatDataProducer
import com.danrusu.pods4kBenchmarks.utils.Distribution
import kotlin.random.Random

/** Represents a wrapper class that stores a single array of the appropriate [DataType] */
sealed class ArrayWrapper : CollectionWrapper() {
    companion object {
        fun create(
            random: Random,
            dataType: DataType,
            size: Int,
            dataProducer: FlatDataProducer
        ): ArrayWrapper = when (dataType) {
            REFERENCE -> ReferenceArrayWrapper(random, size, dataProducer)
            BOOLEAN -> BooleanArrayWrapper(random, size, dataProducer)
            BYTE -> ByteArrayWrapper(random, size, dataProducer)
            CHAR -> CharArrayWrapper(random, size, dataProducer)
            SHORT -> ShortArrayWrapper(random, size, dataProducer)
            INT -> IntArrayWrapper(random, size, dataProducer)
            FLOAT -> FloatArrayWrapper(random, size, dataProducer)
            LONG -> LongArrayWrapper(random, size, dataProducer)
            DOUBLE -> DoubleArrayWrapper(random, size, dataProducer)
        }

        fun createWrappers(
            random: Random,
            dataType: DataType,
            size: Int,
            nestedCollectionSizeDistribution: Distribution,
            dataProducer: FlatDataProducer
        ): Array<out ArrayWrapper> = when (dataType) {
            REFERENCE -> Array(size) {
                ReferenceArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            BOOLEAN -> Array(size) {
                BooleanArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            BYTE -> Array(size) {
                ByteArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            CHAR -> Array(size) {
                CharArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            SHORT -> Array(size) {
                ShortArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            INT -> Array(size) {
                IntArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            FLOAT -> Array(size) {
                FloatArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            LONG -> Array(size) {
                LongArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }

            DOUBLE -> Array(size) {
                DoubleArrayWrapper(random, nestedCollectionSizeDistribution.nextValue(random), dataProducer)
            }
        }
    }

    fun copyData(): FlatDataProducer = object : FlatDataProducer {
        override fun startNewCollection(size: Int) {
            require(size == this@ArrayWrapper.size)
        }

        override fun nextReference(index: Int, random: Random): String = referenceArray[index]

        override fun nextBoolean(index: Int, random: Random): Boolean = booleanArray[index]

        override fun nextByte(index: Int, random: Random): Byte = byteArray[index]

        override fun nextChar(index: Int, random: Random): Char = charArray[index]

        override fun nextShort(index: Int, random: Random): Short = shortArray[index]

        override fun nextInt(index: Int, random: Random): Int = intArray[index]

        override fun nextFloat(index: Int, random: Random): Float = floatArray[index]

        override fun nextLong(index: Int, random: Random): Long = longArray[index]

        override fun nextDouble(index: Int, random: Random): Double = doubleArray[index]
    }
}

class ReferenceArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = referenceArray.size

    override val referenceArray: Array<String> = Array(size) { dataProducer.nextReference(it, random) }
}

class BooleanArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = booleanArray.size

    override val booleanArray: BooleanArray = BooleanArray(size) { dataProducer.nextBoolean(it, random) }
}

class ByteArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = byteArray.size

    override val byteArray: ByteArray = ByteArray(size) { dataProducer.nextByte(it, random) }
}

class CharArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = charArray.size

    override val charArray: CharArray = CharArray(size) { dataProducer.nextChar(it, random) }
}

class ShortArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = shortArray.size

    override val shortArray: ShortArray = ShortArray(size) { dataProducer.nextShort(it, random) }
}

class IntArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = intArray.size

    override val intArray: IntArray = IntArray(size) { dataProducer.nextInt(it, random) }
}

class FloatArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = floatArray.size

    override val floatArray: FloatArray = FloatArray(size) { dataProducer.nextFloat(it, random) }
}

class LongArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = longArray.size

    override val longArray: LongArray = LongArray(size) { dataProducer.nextLong(it, random) }
}

class DoubleArrayWrapper(
    random: Random,
    size: Int,
    dataProducer: FlatDataProducer,
) : ArrayWrapper() {
    init {
        dataProducer.startNewCollection(size)
    }

    override val size: Int
        get() = doubleArray.size

    override val doubleArray: DoubleArray = DoubleArray(size) { dataProducer.nextDouble(it, random) }
}

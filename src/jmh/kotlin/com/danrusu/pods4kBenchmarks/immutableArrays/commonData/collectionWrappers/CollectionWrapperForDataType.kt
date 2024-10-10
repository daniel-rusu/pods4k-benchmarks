package com.danrusu.pods4kBenchmarks.immutableArrays.commonData.collectionWrappers

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableBooleanArray
import com.danrusu.pods4k.immutableArrays.ImmutableByteArray
import com.danrusu.pods4k.immutableArrays.ImmutableCharArray
import com.danrusu.pods4k.immutableArrays.ImmutableDoubleArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.ImmutableIntArray
import com.danrusu.pods4k.immutableArrays.ImmutableLongArray
import com.danrusu.pods4k.immutableArrays.ImmutableShortArray

abstract class CollectionWrapperForDataType {
    open val referenceArray: Array<String>
        get() = throw UnsupportedOperationException()

    open val booleanArray: BooleanArray
        get() = throw UnsupportedOperationException()

    open val byteArray: ByteArray
        get() = throw UnsupportedOperationException()

    open val charArray: CharArray
        get() = throw UnsupportedOperationException()

    open val shortArray: ShortArray
        get() = throw UnsupportedOperationException()

    open val intArray: IntArray
        get() = throw UnsupportedOperationException()

    open val floatArray: FloatArray
        get() = throw UnsupportedOperationException()

    open val longArray: LongArray
        get() = throw UnsupportedOperationException()

    open val doubleArray: DoubleArray
        get() = throw UnsupportedOperationException()

    open val immutableReferenceArray: ImmutableArray<String>
        get() = throw UnsupportedOperationException()

    open val immutableBooleanArray: ImmutableBooleanArray
        get() = throw UnsupportedOperationException()

    open val immutableByteArray: ImmutableByteArray
        get() = throw UnsupportedOperationException()

    open val immutableCharArray: ImmutableCharArray
        get() = throw UnsupportedOperationException()

    open val immutableShortArray: ImmutableShortArray
        get() = throw UnsupportedOperationException()

    open val immutableIntArray: ImmutableIntArray
        get() = throw UnsupportedOperationException()

    open val immutableFloatArray: ImmutableFloatArray
        get() = throw UnsupportedOperationException()

    open val immutableLongArray: ImmutableLongArray
        get() = throw UnsupportedOperationException()

    open val immutableDoubleArray: ImmutableDoubleArray
        get() = throw UnsupportedOperationException()

    open val referenceList: List<String>
        get() = throw UnsupportedOperationException()

    open val booleanList: List<Boolean>
        get() = throw UnsupportedOperationException()

    open val byteList: List<Byte>
        get() = throw UnsupportedOperationException()

    open val charList: List<Char>
        get() = throw UnsupportedOperationException()

    open val shortList: List<Short>
        get() = throw UnsupportedOperationException()

    open val intList: List<Int>
        get() = throw UnsupportedOperationException()

    open val floatList: List<Float>
        get() = throw UnsupportedOperationException()

    open val longList: List<Long>
        get() = throw UnsupportedOperationException()

    open val doubleList: List<Double>
        get() = throw UnsupportedOperationException()
}

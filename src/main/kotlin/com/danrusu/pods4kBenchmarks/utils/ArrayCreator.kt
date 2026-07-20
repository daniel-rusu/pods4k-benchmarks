package com.danrusu.pods4kBenchmarks.utils

import java.lang.reflect.Array as JavaArray

object ArrayCreator {
    fun <T> createArray(componentClass: Class<T & Any>, size: Int, init: (index: Int) -> T): Array<T> {
        @Suppress("UNCHECKED_CAST")
        val result = JavaArray.newInstance(componentClass, size) as Array<T>

        for (i in 0..<size) {
            result[i] = init(i)
        }
        return result
    }

    /**
     * Creates an Array<Array<T>>.
     *
     * The component type of the outer array is not Array::class.java but rather a dynamically generated class that
     * uniquely identifies Array<T> based on the concrete T class.  That's because array generics are not erased.  The
     * array object header references a dynamically generated array class which encodes the class of the elements.
     */
    fun <T> createNestedArrays(
        nestedElementClass: Class<T & Any>,
        size: Int,
        init: (index: Int) -> Array<T>,
    ): Array<Array<T>> {
        @Suppress("UNCHECKED_CAST")
        val result = JavaArray.newInstance(nestedElementClass.arrayType(), size) as Array<Array<T>>

        for (i in 0..<size) {
            result[i] = init(i)
        }
        return result
    }
}

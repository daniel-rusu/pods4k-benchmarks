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
}

package com.danrusu.pods4kBenchmarks.utils

import java.lang.reflect.Array

object ArrayCreator {
    fun <T> createArray(componentClass: Class<T & Any>, size: Int, init: (index: Int) -> T): kotlin.Array<T> {
        @Suppress("UNCHECKED_CAST")
        val result = Array.newInstance(componentClass, size) as kotlin.Array<T>

        for (i in 0..<size) {
            result[i] = init(i)
        }
        return result
    }
}

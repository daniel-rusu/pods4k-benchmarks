package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import kotlin.jvm.javaObjectType

/** Resolves the runtime element class represented by this benchmark data type. */
internal fun DataType.resolveElementClass(referenceClass: Class<*>): Class<*> = when (this) {
    DataType.REFERENCE -> referenceClass
    DataType.BOOLEAN -> Boolean::class.javaObjectType
    DataType.BYTE -> Byte::class.javaObjectType
    DataType.CHAR -> Char::class.javaObjectType
    DataType.SHORT -> Short::class.javaObjectType
    DataType.INT -> Int::class.javaObjectType
    DataType.FLOAT -> Float::class.javaObjectType
    DataType.LONG -> Long::class.javaObjectType
    DataType.DOUBLE -> Double::class.javaObjectType
}

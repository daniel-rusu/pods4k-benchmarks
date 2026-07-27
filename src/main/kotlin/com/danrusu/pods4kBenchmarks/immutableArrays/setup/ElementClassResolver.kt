package com.danrusu.pods4kBenchmarks.immutableArrays.setup

/**
 * Resolves the logical benchmark element class represented by this data type.
 *
 * Primitive data types resolve to their boxed classes because this class is a runtime validation token rather than a
 * description of the physical primitive-array representation.
 */
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

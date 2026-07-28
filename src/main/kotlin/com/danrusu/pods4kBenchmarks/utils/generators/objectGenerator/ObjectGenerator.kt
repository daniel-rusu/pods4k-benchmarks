package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

/** Generates object or reference values for benchmark setup */
interface ObjectGenerator<T> {
    /** Runtime class used for array component creation, even when [T] is nullable. */
    val objectClass: Class<T & Any>

    /** Returns the next generated object value. */
    fun next(): T
}

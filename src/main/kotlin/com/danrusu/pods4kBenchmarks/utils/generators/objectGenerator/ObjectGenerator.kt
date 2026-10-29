package com.danrusu.pods4kBenchmarks.utils.generators.objectGenerator

/** Generates object or reference values for benchmark setup */
interface ObjectGenerator<T> {
    /** Runtime class used for array component creation, even when [T] is nullable. */
    val objectClass: Class<T & Any>

    /** Returns the next generated object value. */
    fun next(): T
}

inline fun <reified T : Any> objectGenerator(
    noinline generate: () -> T,
): ObjectGenerator<T> = object : ObjectGenerator<T> {
    override val objectClass: Class<T> = T::class.java

    override fun next(): T = generate()
}

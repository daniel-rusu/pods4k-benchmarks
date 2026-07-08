package com.danrusu.pods4kBenchmarks.utils.generators

import com.danrusu.pods4kBenchmarks.utils.DataGenerator
import kotlin.random.Random

/**
 * Generates object or reference values for benchmark setup.
 */
interface ObjectGenerator<T> {
    /** Runtime class used for array component creation, even when [T] is nullable. */
    val objectClass: Class<T & Any>

    /** Returns the next generated object value. */
    fun next(): T
}

/**
 * Generates random alphanumeric strings.
 */
class StringGenerator(
    private val random: Random,
    private val minLength: Int = 3,
    private val maxLength: Int = 10,
) : ObjectGenerator<String> {
    init {
        require(minLength >= 0) { "minLength ($minLength) cannot be negative" }
        require(minLength <= maxLength) { "minLength ($minLength) cannot be larger than maxLength($maxLength)" }
    }

    override val objectClass: Class<String> = String::class.java

    override fun next(): String {
        val length = random.nextInt(from = minLength, until = maxLength + 1)
        val randomChars = CharArray(length) { DataGenerator.randomChar(random) }
        return String(randomChars)
    }
}

/**
 * Uses [nullabilityPolicy] to decide whether to return null or ask [delegate] for the next object.
 */
class NullableObjectGenerator<T : Any>(
    private val delegate: ObjectGenerator<T>,
    private val nullabilityPolicy: NullabilityPolicy,
) : ObjectGenerator<T?> {
    override val objectClass: Class<T> = delegate.objectClass

    override fun next(): T? = nullabilityPolicy.nullable { delegate.next() }
}

/**
 * Creates [ObjectGenerator] instances from benchmark RNG streams.
 */
abstract class ObjectGeneratorFactory<T> {
    /** Creates an object generator using the provided RNG streams. */
    abstract fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T>

    companion object {
        /**
         * Creates a factory for objects initialized from field and reference generators.
         */
        inline fun <reified T : Any, R> of(
            fieldsFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
            referenceFactory: ObjectGeneratorFactory<R>,
            noinline initializer: (fields: FieldGenerator, references: ObjectGenerator<R>) -> T,
        ): ObjectGeneratorFactory<T> = object : ObjectGeneratorFactory<T>() {
            override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T> = object : ObjectGenerator<T> {
                override val objectClass: Class<T> = T::class.java

                private val fieldGenerator = fieldsFactory.create(generatorRngs)
                private val referenceGenerator = referenceFactory.create(generatorRngs)

                override fun next(): T = initializer(fieldGenerator, referenceGenerator)
            }
        }

        /** Creates a factory for the common case where [String] is used for reference fields. */
        inline fun <reified T : Any> of(
            fieldsFactory: FieldGeneratorFactory = FieldGeneratorFactory.withRandomFields(),
            noinline initializer: (fields: FieldGenerator, references: ObjectGenerator<String>) -> T,
        ): ObjectGeneratorFactory<T> = of(
            fieldsFactory = fieldsFactory,
            referenceFactory = randomStrings(),
            initializer = initializer,
        )

        /** Creates a factory for random string generators with configurable string lengths. */
        fun randomStrings(
            minLength: Int = 3,
            maxLength: Int = 10,
        ): ObjectGeneratorFactory<String> = object : ObjectGeneratorFactory<String>() {
            override fun create(generatorRngs: GeneratorRngs): StringGenerator =
                StringGenerator(
                    random = generatorRngs.dataGenerationRng,
                    minLength = minLength,
                    maxLength = maxLength,
                )
        }
    }
}

/**
 * Creates a factory that uses [nullRatio] to decide whether to return null or generate the next non-null value.
 */
fun <T : Any> ObjectGeneratorFactory<T>.nullable(
    nullRatio: Double,
): ObjectGeneratorFactory<T?> = object : ObjectGeneratorFactory<T?>() {
    override fun create(generatorRngs: GeneratorRngs): ObjectGenerator<T?> {
        return NullableObjectGenerator(
            delegate = this@nullable.create(generatorRngs),
            nullabilityPolicy = RandomNullabilityPolicy(nullRatio, generatorRngs.nullabilityDecisionsRng),
        )
    }
}

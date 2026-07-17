package com.danrusu.pods4kBenchmarks.immutableArrays.nullableFlatCollectionBenchmarks.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkGeneratorRngs
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionFactory
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.resolveElementClass
import com.danrusu.pods4kBenchmarks.utils.ArrayCreator
import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import com.danrusu.pods4kBenchmarks.utils.RngFactory
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.FieldGeneratorFactory
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGenerator
import com.danrusu.pods4kBenchmarks.utils.generators.ObjectGeneratorFactory
import kotlinx.collections.immutable.PersistentList

/**
 * Materialized nullable collections for one [com.danrusu.pods4kBenchmarks.immutableArrays.setup.CollectionType]/[com.danrusu.pods4kBenchmarks.immutableArrays.setup.DataType] trial.
 *
 * Nullable primitive values use their boxed representations, so every data type can share the same four typed
 * accessors. Only the active collection representation is retained. Accessors validate the non-null class underlying
 * the nullable element type, while the outer array's runtime component type guards the collection representation cast.
 */
class NullableFlatCollectionBenchmarkData private constructor(
    @PublishedApi internal val elementClass: Class<*>,
    @PublishedApi internal val collectionData: Array<*>,
) {
    inline fun <reified T : Any> listData(): Array<ArrayList<T?>> {
        return typedCollectionData<T, ArrayList<T?>>()
    }

    inline fun <reified T : Any> persistentListData(): Array<PersistentList<T?>> {
        return typedCollectionData<T, PersistentList<T?>>()
    }

    inline fun <reified T : Any> arrayData(): Array<Array<T?>> {
        return typedCollectionData<T, Array<T?>>()
    }

    inline fun <reified T : Any> immutableArrayData(): Array<ImmutableArray<T?>> {
        return typedCollectionData<T, ImmutableArray<T?>>()
    }

    @PublishedApi
    internal inline fun <reified T : Any, reified C : Any> typedCollectionData(): Array<C> {
        val requestedElementClass = T::class.javaObjectType
        check(elementClass === requestedElementClass) {
            "Requested ${requestedElementClass.name} elements, but the data contains ${elementClass.name} elements"
        }

        @Suppress("UNCHECKED_CAST")
        return collectionData as Array<C>
    }

    companion object {
        /** Creates deterministic nullable data for one flat benchmark parameter combination. */
        fun create(
            collectionType: CollectionType,
            dataType: DataType,
            numCollections: Int,
            sizeDistributionFactory: DistributionFactory,
            fieldGeneratorFactory: FieldGeneratorFactory,
            referenceGeneratorFactory: ObjectGeneratorFactory<String?>,
        ): NullableFlatCollectionBenchmarkData {
            require(numCollections > 0) { "numCollections must be positive" }

            val rngFactory = RngFactory()
            val generatorRngs = BenchmarkGeneratorRngs(rngFactory)
            val sizeDistribution = sizeDistributionFactory.create(rngFactory)
            val fields = fieldGeneratorFactory.create(generatorRngs)
            val references = referenceGeneratorFactory.create(generatorRngs)
            val elementClass = dataType.resolveElementClass(references.objectClass)
            val generateElement = createElementGenerator(dataType, fields, references)

            val data: Array<*> = when (collectionType) {
                CollectionType.LIST -> Array(numCollections) {
                    CollectionFactory.createList(sizeDistribution.nextValue()) { generateElement() }
                }

                CollectionType.PERSISTENT_LIST -> Array(numCollections) {
                    CollectionFactory.createPersistentList(sizeDistribution.nextValue()) { generateElement() }
                }

                CollectionType.ARRAY -> Array(numCollections) {
                    @Suppress("UNCHECKED_CAST") // cast as Class<Any> because generateElement returns Any?
                    ArrayCreator.createArray(elementClass as Class<Any>, sizeDistribution.nextValue()) {
                        generateElement()
                    }
                }

                CollectionType.IMMUTABLE_ARRAY -> Array(numCollections) {
                    ImmutableArray.Companion(sizeDistribution.nextValue()) { generateElement() }
                }
            }

            return NullableFlatCollectionBenchmarkData(elementClass, data)
        }

        private fun createElementGenerator(
            dataType: DataType,
            fields: FieldGenerator,
            references: ObjectGenerator<String?>,
        ): () -> Any? = when (dataType) {
            DataType.REFERENCE -> references::next
            DataType.BOOLEAN -> fields::nextNullableBoolean
            DataType.BYTE -> fields::nextNullableByte
            DataType.CHAR -> fields::nextNullableChar
            DataType.SHORT -> fields::nextNullableShort
            DataType.INT -> fields::nextNullableInt
            DataType.FLOAT -> fields::nextNullableFloat
            DataType.LONG -> fields::nextNullableLong
            DataType.DOUBLE -> fields::nextNullableDouble
        }
    }
}
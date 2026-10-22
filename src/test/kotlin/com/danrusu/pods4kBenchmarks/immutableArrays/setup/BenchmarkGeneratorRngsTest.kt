package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.utils.DistributionFactory
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import kotlin.random.Random

class BenchmarkGeneratorRngsTest {
    @Test
    fun `same master seed reproduces every stream`() {
        for (masterSeed in listOf(0L, 123L, -456L)) {
            val firstStreams = BenchmarkGeneratorRngs(masterSeed).streams()
            val secondStreams = BenchmarkGeneratorRngs(masterSeed).streams()

            for ((name, firstStream) in firstStreams) {
                val secondStream = secondStreams.getValue(name)
                expectThat(List(20) { firstStream.nextLong() })
                    .describedAs("$name with master seed $masterSeed")
                    .isEqualTo(List(20) { secondStream.nextLong() })
            }
        }
    }

    @Test
    fun `different master seeds change every stream`() {
        val firstStreams = BenchmarkGeneratorRngs(123L).streams()
        val secondStreams = BenchmarkGeneratorRngs(456L).streams()

        for ((name, firstStream) in firstStreams) {
            val secondStream = secondStreams.getValue(name)
            expectThat(List(20) { firstStream.nextLong() })
                .describedAs(name)
                .isNotEqualTo(List(20) { secondStream.nextLong() })
        }
    }

    @Test
    fun `consuming one stream does not advance any other stream`() {
        for (consumedName in BenchmarkGeneratorRngs().streams().keys) {
            val expectedStreams = BenchmarkGeneratorRngs(123L).streams()
            val actualStreams = BenchmarkGeneratorRngs(123L).streams()
            repeat(100) { actualStreams.getValue(consumedName).nextLong() }

            for ((name, expectedStream) in expectedStreams) {
                if (name == consumedName) continue

                val actualStream = actualStreams.getValue(name)
                expectThat(List(20) { actualStream.nextLong() })
                    .describedAs("$name after consuming $consumedName")
                    .isEqualTo(List(20) { expectedStream.nextLong() })
            }
        }
    }

    @Test
    fun `size distributions can be created and sampled in either order`() {
        val outerFirstRngs = BenchmarkGeneratorRngs(123L)
        val firstOuterDistribution =
            DistributionFactory.ListSizeDistribution.create(outerFirstRngs.outerCollectionSizeRng)
        val expectedOuterSizes = List(100) { firstOuterDistribution.nextValue() }
        val firstNestedDistribution =
            DistributionFactory.NestedListSizeDistribution.create(outerFirstRngs.nestedCollectionSizeRng)
        val expectedNestedSizes = List(100) { firstNestedDistribution.nextValue() }

        val nestedFirstRngs = BenchmarkGeneratorRngs(123L)
        val secondNestedDistribution =
            DistributionFactory.NestedListSizeDistribution.create(nestedFirstRngs.nestedCollectionSizeRng)
        val actualNestedSizes = List(100) { secondNestedDistribution.nextValue() }
        val secondOuterDistribution =
            DistributionFactory.ListSizeDistribution.create(nestedFirstRngs.outerCollectionSizeRng)
        val actualOuterSizes = List(100) { secondOuterDistribution.nextValue() }

        expectThat(actualOuterSizes).isEqualTo(expectedOuterSizes)
        expectThat(actualNestedSizes).isEqualTo(expectedNestedSizes)
    }

    @Test
    fun `default seed preserves the original stream sequences`() {
        // Fixed samples validating RNG allocation order.
        val expectedSamples = mapOf(
            "dataGenerationRng" to listOf(1750893463095773485L, -3420126876893692269L, -4229505535527673651L),
            "nullabilityDecisionsRng" to listOf(-3696125781861132782L, 2568919157239051596L, 6358794574524172695L),
            "predicateOutcomeRng" to listOf(1132958324480006400L, -5562990496568061489L, -2655255275767423636L),
            "outerCollectionSizeRng" to listOf(-2753407341945129819L, -7888170092377505251L, 6988320364346431829L),
            "nestedCollectionSizeRng" to listOf(-7192322087110104742L, -1599020845469869262L, 5016601068726458620L),
        )
        val streams = BenchmarkGeneratorRngs().streams()

        for ((name, expected) in expectedSamples) {
            val stream = streams.getValue(name)
            // If this test fails then newly-generated data is no longer comparable to previous runs
            // Either restore previous RNG behavior or update this test with the new expected values and re-run all the
            // benchmarks to make all the charts consistent again
            expectThat(List(expected.size) { stream.nextLong() })
                .describedAs(name)
                .isEqualTo(expected)
        }
    }

    private fun BenchmarkGeneratorRngs.streams(): Map<String, Random> = mapOf(
        "dataGenerationRng" to dataGenerationRng,
        "nullabilityDecisionsRng" to nullabilityDecisionsRng,
        "predicateOutcomeRng" to predicateOutcomeRng,
        "outerCollectionSizeRng" to outerCollectionSizeRng,
        "nestedCollectionSizeRng" to nestedCollectionSizeRng,
    )
}

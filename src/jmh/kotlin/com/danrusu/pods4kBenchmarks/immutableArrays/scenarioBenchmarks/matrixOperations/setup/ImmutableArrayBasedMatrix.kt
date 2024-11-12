package com.danrusu.pods4kBenchmarks.immutableArrays.scenarioBenchmarks.matrixOperations.setup

import com.danrusu.pods4k.immutableArrays.ImmutableArray
import com.danrusu.pods4k.immutableArrays.ImmutableFloatArray
import com.danrusu.pods4k.immutableArrays.emptyImmutableArray
import com.danrusu.pods4k.immutableArrays.toImmutableArray


class ImmutableArrayBasedMatrix private constructor(
    val numRows: Int,
    val numColumns: Int,
    private val rows: ImmutableArray<ImmutableFloatArray>
) {
    /**
     * IMPORTANT:
     *
     * This uses the classical matrix multiplication algorithm.  Although much more sophisticated algorithms exist, note
     * that both [ListBasedMatrix] and [ImmutableArrayBasedMatrix] use the exact same algorithm as they only differ by
     * storing the values in a list of lists versus an immutable array of immutable arrays.  This is intended to
     * represent the potential impact of updating generic memory-intensive numeric algorithms from using lists to
     * immutable arrays.
     */
    operator fun times(other: ImmutableArrayBasedMatrix): ImmutableArrayBasedMatrix {
        require(numColumns == other.numRows)

        val result = ImmutableArray(numRows) { row ->
            val currentRow = rows[row]

            ImmutableFloatArray(other.numColumns) { column ->
                var cellValue = 0.0f
                for (k in 0..<numColumns) {
                    cellValue += currentRow[k] * other.rows[k][column]
                }
                cellValue
            }
        }
        return ImmutableArrayBasedMatrix(numRows = numRows, numColumns = other.numColumns, rows = result)
    }

    companion object {
        val EMPTY = ImmutableArrayBasedMatrix(
            numRows = 0,
            numColumns = 0,
            rows = emptyImmutableArray()
        )

        /** Creates a matrix from a non-empty array */
        fun create(from: Array<FloatArray>): ImmutableArrayBasedMatrix {
            val numRows = from.size

            return ImmutableArrayBasedMatrix(
                numRows = numRows,
                numColumns = from[0].size,
                rows = ImmutableArray(numRows) { row -> from[row].toImmutableArray() }
            )
        }
    }
}

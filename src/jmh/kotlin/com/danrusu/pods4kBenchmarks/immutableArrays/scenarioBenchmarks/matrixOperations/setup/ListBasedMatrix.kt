package com.danrusu.pods4kBenchmarks.immutableArrays.scenarioBenchmarks.matrixOperations.setup

class ListBasedMatrix private constructor(
    val numRows: Int,
    val numColumns: Int,
    private val rows: List<List<Float>>
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
    operator fun times(other: ListBasedMatrix): ListBasedMatrix {
        require(numColumns == other.numRows)

        val result = ArrayList<ArrayList<Float>>(numRows)

        for (row in 0..<numRows) {
            val resultRow = ArrayList<Float>(other.numColumns).also { result.add(it) }
            val currentRow = rows[row]

            for (column in 0..<other.numColumns) {
                var cellValue = 0.0f
                for (k in 0..<numColumns) {
                    cellValue += currentRow[k] * other.rows[k][column]
                }
                resultRow.add(cellValue)
            }
        }
        return ListBasedMatrix(numRows = numRows, numColumns = other.numColumns, rows = result)
    }

    companion object {
        val EMPTY = ListBasedMatrix(
            numRows = 0,
            numColumns = 0,
            rows = emptyList()
        )

        /** Creates a matrix from a non-empty array */
        fun create(from: Array<FloatArray>): ListBasedMatrix = ListBasedMatrix(
            numRows = from.size,
            numColumns = from[0].size,
            rows = from.map { it.toList() }
        )
    }
}

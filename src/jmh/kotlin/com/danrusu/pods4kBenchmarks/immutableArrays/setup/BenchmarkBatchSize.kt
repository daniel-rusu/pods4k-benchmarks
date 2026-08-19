package com.danrusu.pods4kBenchmarks.immutableArrays.setup

import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize.DEFAULT
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize.NESTED_COLLECTIONS
import com.danrusu.pods4kBenchmarks.immutableArrays.setup.BenchmarkBatchSize.SPARSE_ACCESS

/**
 * Defaults for the number of collections to process in one benchmark invocation.  Benchmarks with unique access
 * patterns may justify a benchmark-local override.
 *
 * A batch should touch enough data that it does not receive an unrealistic boost from repeatedly fitting in the L1 CPU
 * cache, but not so much that fetching data from main memory dominates the operation being measured. Processing several
 * collections per invocation also amortizes JMH invocation overhead.
 *
 * ### Count data accessed, not dataset size
 *
 * The relevant size is the data fetched or written while the operation runs. It is not the total size of the data
 * created during setup. For example, if `any` stops after inspecting 50 elements of a 1,000-element ArrayList, count
 * the list metadata, roughly 50 array slots, and the element fields actually read, not all 1,000 slots and not
 * untouched fields of those elements. CPUs fetch whole cache lines, so an estimate should include nearby bytes brought
 * in with each access even when the operation uses only some of them.
 *
 * For nested collections, count the outer collection entries that are visited, the inner collection storage reached
 * through those entries, the fields being operated on, and the fields of the elements brought in with the cache line.
 * Also include cache traffic from result construction and temporary storage. A result consumed after each source
 * collection is not necessarily live for the whole batch, but writing it can still evict source data.
 *
 * These defaults use a 32 KiB L1 data cache and a 32 MiB L3 cache as the reference envelope:
 *
 * 1. Bytes accessed by the lightest collectionType / DataType combination >= 2 * L1 cache
 * 2. Bytes accessed plus result/temporary traffic in heaviest CollectionType / DataType combination <= 70% * L3 cache
 *
 * The first check reduces the chance that the smallest-access parameter combination remains hot in L1. The second
 * leaves L3 headroom for the benchmark harness, the JVM, and other activity. It also keeps the largest-access
 * combination from becoming primarily a DRAM-bandwidth test.
 *
 * When a result grows in a JVM `ArrayList` without an exact initial capacity, include more than its final size. The
 * roughly 1.5x growth policy repeatedly allocates and copies backing arrays. Include other relevant overhead such as
 * object and array headers, reference width, alignment, hash-table storage, and sorting scratch space.
 *
 * ### Choosing a tier
 *
 * Choose from the operation's access and result pattern:
 *
 * - [NESTED_COLLECTIONS] when each collection leads to traversal or materialization of many inner elements, such as
 * `flatMap`.
 *
 * - [SPARSE_ACCESS] for non-nested operations that inspect only a small prefix, suffix, or subset, and do not create a
 *   result proportional to the full source.
 *
 * - [DEFAULT] for full traversals and operations that create or copy a material portion of each collection.
 *
 * Short-circuiting alone does not imply [SPARSE_ACCESS]: `dropWhile`, for example, may inspect only a short prefix but
 * then copy most of the collection, so it uses [DEFAULT].
 *
 * These bounds are heuristics rather than guarantees. Prefetching, cache-line sharing, associativity, object layout,
 * GC, and a shared L3 all affect actual behavior.
 *
 * A batch size counts input collections, whereas `@OperationsPerInvocation` counts logical operations. A pairwise
 * benchmark therefore reports half its batch size. All sizes are even to avoid rounding.
 */
object BenchmarkBatchSize {
    /** Default for full traversals and operations that create or copy a material result. */
    const val DEFAULT: Int = 750

    /** For non-nested, sparse or short-circuiting access with no collection-sized result. */
    const val SPARSE_ACCESS: Int = 1_000

    /** For operations that traverse or materialize nested collection contents. */
    const val NESTED_COLLECTIONS: Int = 200
}

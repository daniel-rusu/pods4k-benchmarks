# Benchmark Architecture

## Purpose

This project uses JMH to compare [Immutable Arrays][immutable-arrays-url] with equivalent operations on regular arrays,
`ArrayList`, and lists from  [kotlinx.collections.immutable][persistent-list-url].

## Benchmark Matrix

`CollectionBenchmark` defines two JMH parameters:

- `CollectionType`
    - `LIST`: uses `ArrayList` exposed as `List<T>`
    - `PERSISTENT_LIST`: uses `PersistentList` from `kotlinx.collections.immutable`
    - `ARRAY`: uses `Array<T>` for references and primitive arrays for non-null primitives
    - `IMMUTABLE_ARRAY`: uses `ImmutableArray<T>` for references and primitive variants for non-null primitives
- `DataType`
    - `REFERENCE`, `BOOLEAN`, `BYTE`, `CHAR`, `SHORT`, `INT`, `FLOAT`, `LONG`, and `DOUBLE`

Together, these axes produce **36 independent trials** per benchmark method.

## Runtime Flow

1. A benchmark class defines:
    - Equivalent, statically typed operations for each collection representation.
    - Data-generation recipe for populating collections.
2. JMH selects one `CollectionType` and `DataType` combination.
3. `@Setup(Level.Trial)` builds a deterministic batch of collections for only that combination.
4. The benchmark invokes the operation on every collection in the batch consuming the result with `Blackhole`.
5. The throughput is normalized based on `@OperationsPerInvocation` (usually batch size).

## Benchmark Categories

Benchmark categories describe the shape of the data consumed by an operation:

| Category     | Shape                                                       | Examples                                                                      |
|--------------|-------------------------------------------------------------|-------------------------------------------------------------------------------|
| Flat         | `CollectionType<DataType>`                                  | `List<Boolean>` <br/> `BooleanArray`                                          |
| NullableFlat | `CollectionType<DataType?>`                                 | `List<Double?>` <br/> `Array<Double?>`                                        |
| Nested       | `CollectionType<CollectionOwner<CollectionType<DataType>>>` | `List<CollectionOwner<List<Byte>>>` <br/> `Array<CollectionOwner<ByteArray>>` |
| Object       | `CollectionType<CustomType>`                                | `List<CustomType>` <br/>  `Array<CustomType>`                                 |

## Code Organization

| Source set        | Responsibility                                                                            |
|-------------------|-------------------------------------------------------------------------------------------|
| `src/main/kotlin` | Reusable utilities, shared infrastructure, and deterministic data builders                |
| `src/jmh/kotlin`  | JMH lifecycle, typed operation dispatch, benchmark-only fixtures, and measured operations |
| `src/test/kotlin` | Validation tests for utilities and data builders from `src/main`                          |

Each benchmark follows the same structure:

- `<Operation>Benchmarks` extends `<Category>CollectionBenchmark` based on the shape of the data being consumed
- `<Category>CollectionBenchmark` uses `<Category>CollectionBenchmarkData` to create the trial data

For example, `FilterBenchmarks` extends `FlatCollectionBenchmark` which uses `FlatCollectionBenchmarkData`.

## Data Construction

This is the data generation flow for the `drop` operation. The nested, nullable-flat, and object-collection benchmark
categories follow the same general pattern:

1. `DropBenchmarks` specifies the data generation recipe:
    * `numCollections` to create
    * Default `DistributionFactory` for sampling collection sizes
    * Default `ObjectGeneratorFactory<String>` & `FieldGeneratorFactory` for random string & primitive values
2. JMH iterates through every `CollectionType` & `DataType`, and creates `DropBenchmarks` with the current combination.
3. JMH calls `FlatCollectionBenchmark.setupBenchmarkData()` to begin data construction.
4. Data creation is delegated to `FlatCollectionBenchmarkData.create(...)` which performs the following:
    * Creates an `RngFactory` from a constant seed
    * Splits independent RNG streams for sizes, values, null placement, and predicate decisions.
    * Builds the requested size distribution and element generators.
    * Uses `CollectionFactory` to materialize a `CollectionBatch` containing an array of `NUM_COLLECTIONS` collections.
        * Collection are populated with elements generated from the `FieldGenerator` or `ObjectGenerator` depending on
          the `DataType`

Although a benchmark has 36 parameter combinations, a `CollectionBatch` stores only the current combination. For
example, the `LIST`/`BOOLEAN` trial contains an array of `List<Boolean>` collections.

### Collection Sizes

Collection sizes are sampled from the specified size `Distribution`.

Nested data uses separate distributions for outer and inner collection sizes. This models common business cases such as
managing many orders with each order usually only containing a few products.

### Nullability Handling

Benchmarks that deal with nullable elements produce `null` according to a configured null ratio.

### Predicate Handling

Benchmarks that deal with predicates generate elements that pass according to a configured acceptance ratio.

They use `FlatDataFilter` to preselect whether each value should match, then generate a value on the required side of
the data-type's median. An element is accepted if it's smaller than the median. This controls acceptance ratios without
adding RNG overhead to the measurement.

## Fair-Comparison Safeguards

- **Public APIs:** every representation uses an equivalent, statically typed operation through its published API.
- **Specialization:** JVM arrays and Immutable Arrays retain primitive-specialized forms.
- **Equivalent inputs:**
    - Same sequence of collection sizes is used across all `CollectionType` & `DataType` combinations.
    - A `DataType` has the same sequence of values across all `CollectionType` representations.
    - Nullable elements have the same sequence of null positions across all `CollectionType` & `DataType` combinations.
    - Predicates have the same sequence of predicate acceptance across all `CollectionType` & `DataType` combinations.
- **Isolated setup:** `@Setup(Level.Trial)` excludes construction from timed work and materializes only the active
  representation, avoiding cross-representation cache pressure.
- **Batched work:** each invocation processes hundreds of prebuilt collections instead of one repeatedly hot input.
- **Correct normalization:** `@OperationsPerInvocation` matches the number of collections processed; pairwise benchmarks
  use `NUM_COLLECTIONS / 2`.
- **Dead-code prevention:** every result is consumed by `Blackhole` to prevent the JIT from eliminating the operation.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

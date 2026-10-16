# Benchmark Architecture

## Purpose

This project uses JMH to compare [Immutable Arrays][immutable-arrays-url] with equivalent operations on regular arrays,
`ArrayList`, and lists from  [kotlinx.collections.immutable][persistent-list-url].

## Code Organization

| Source set        | Responsibility                                                             |
|-------------------|----------------------------------------------------------------------------|
| `src/main/kotlin` | Reusable generators, collection factories, and deterministic data builders |
| `src/jmh/kotlin`  | JMH lifecycle, typed operation dispatch, and measured operations           |
| `src/test/kotlin` | Validation tests for `src/main` utilities and data builders                |

## Benchmark Matrix

`CollectionBenchmark` defines two JMH parameters:

| CollectionType    | Representation                                                                       |
|-------------------|--------------------------------------------------------------------------------------|
| `LIST`            | `ArrayList` stored as `List<T>`                                                      |
| `PERSISTENT_LIST` | `PersistentList<T>` from `kotlinx.collections.immutable`                             |
| `ARRAY`           | `Array<T>`, or primitive arrays for primitives (eg. `BooleanArray`)                  |
| `IMMUTABLE_ARRAY` | `ImmutableArray<T>`, or primitive variants for primitives (eg. `ImmutableByteArray`) |

and

| DataType | `BOOLEAN`, `BYTE`, `CHAR`, `SHORT`, `INT`, `FLOAT`, `LONG`, `DOUBLE`, & `REFERENCE` |
|----------|-------------------------------------------------------------------------------------|

Together, these produce **36 independent trials** per benchmark method.

## Benchmark Categories

Benchmarks are categorized based on the shape of data being consumed:

| Category     | Base Class                        | Input Shape Examples                                                        | Example Benchmark         |
|--------------|-----------------------------------|-----------------------------------------------------------------------------|---------------------------|
| Flat         | `FlatCollectionBenchmark`         | `List<Float>` <br/> `FloatArray`                                            | `FilterBenchmarks`        |
| NullableFlat | `NullableFlatCollectionBenchmark` | `List<Double?>` <br/> `Array<Double?>`                                      | `FilterNotNullBenchmarks` |
| Nested       | `NestedCollectionBenchmark`       | `List<CollectionOwner<List<Int>>>` <br/> `Array<CollectionOwner<IntArray>>` | `FlatMapBenchmarks`       |
| Object       | `ObjectCollectionBenchmark`       | `List<CustomType>` <br/>  `Array<CustomType>`                               | `MapBenchmarks`           |

Together, the benchmark category and benchmark matrix define the type of data being consumed:

* `FilterBenchmarks` extends `FlatCollectionBenchmark`
    * Trial for `(ARRAY, LONG)` measures `LongArray.filter { ... }`
* `FilterNotNullBenchmarks` extends `NullableFlatCollectionBenchmark`
    * Trial for `(LIST, INT)` measures `List<Int?>.filterNotNull()`

## Runtime Flow

1. A benchmark class defines:
    - Equivalent operations for each collection representation.
    - Data-generation recipe for creating collections.
2. JMH selects one `CollectionType` and `DataType` combination.
3. `@Setup(Level.Trial)` in the base class builds a deterministic batch of collections for only that combination.
4. The benchmark invokes the operation on every collection in the batch consuming the result with `Blackhole`.
5. The throughput is normalized based on `@OperationsPerInvocation` (usually batch size).

## Data Generation Recipe

Benchmark classes define the data-generation recipe:

| Ingredient                  | Explanation                                                                                                                                                                                                                             |
|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Batch Size                  | Controls how many collections will be created for the current `(CollectionType, DataType)`  combination.                                                                                                                                |
| `DistributionFactory`       | - Creates a `Distribution` to control collection sizes.<br/>- Batch will contain collections of varying sizes sampled from this distribution.<br/>- Nested collections use a separate `DistributionFactory` for inner collection sizes. |
| `PrimitiveGeneratorFactory` | - Creates a `PrimitiveGenerator` for generating primitive values.<br/>- Calling `withPredicateMatchRatio(ratio, predicate)` wraps the factory so that `ratio` proportion of values pass the predicate.                                  |
| `ObjectGeneratorFactory`    | - Creates a `ObjectGenerator` for generating object values.<br/>- Calling `withPredicateMatchRatio(ratio, predicate)` wraps the factory so that `ratio` proportion of values pass the predicate.                                        |
| `NullabilityPolicyFactory`  | Creates a `NullabilityPolicy` for deciding when values should be null.                                                                                                                                                                  |

## Data Construction

This is the data generation flow for the `drop` operation. The nested, nullable-flat, and object-collection benchmark
categories follow the same general pattern:

1. `DropBenchmarks` specifies the data generation recipe:
    * `numCollections` to create
    * Default `DistributionFactory` for sampling collection sizes
    * Default `ObjectGeneratorFactory<String>` & `PrimitiveGeneratorFactory` for random string & primitive values
2. JMH iterates through every `CollectionType` & `DataType`, and creates `DropBenchmarks` with the current combination.
3. JMH calls `FlatCollectionBenchmark.setupBenchmarkData()` to begin data construction.
4. Data creation is delegated to `FlatCollectionBenchmarkData.create(...)` which performs the following:
    * Creates an `RngFactory` from a constant seed
    * Splits independent RNG streams for sizes, values, null placement, and predicate decisions.
    * Uses `CollectionFactory` to materialize a `CollectionBatch` containing an array of `NUM_COLLECTIONS` collections.
        * Collection are populated with elements generated from the `PrimitiveGenerator` or `ObjectGenerator` depending
          on
          the `DataType`

Although a benchmark has 36 parameter combinations, a `CollectionBatch` stores only the current combination. For
example, the `LIST`/`BOOLEAN` trial contains an array of `List<Boolean>` collections.

### Collection Sizes

* The size `Distribution` produces a combination of small, medium, and large collections
* Nested data, such as a list of orders containing products, use separate distributions for the inner collections

### Nullability Handling

Benchmarks that deal with nullable elements produce `null` according to a configured null ratio.

### Predicate Handling

Benchmarks that deal with predicates decorate their ordinary primitive and reference generator factories with a
configured match ratio. A dedicated RNG stream independently selects the required predicate outcome for each emitted
value, then the adapter rejection-samples the source until it finds a value on the required side of the data-type's
median. The measured operation invokes the same concrete midpoint predicate without RNG or generator overhead.

## Fair-Comparison Safeguards

- **Public APIs:** every representation uses an equivalent, statically typed operation through its published API.
- **Specialization:** JVM arrays and Immutable Arrays retain primitive-specialized forms.
- **Equivalent inputs:**
    - Same sequence of collection sizes is used across all `CollectionType` & `DataType` combinations.
    - A `DataType` has the same sequence of values across all `CollectionType` representations.
    - Nullable elements have the same sequence of null positions across all `CollectionType` & `DataType` combinations.
    - Predicates have the same sequence of predicate outcomes across all `CollectionType` & `DataType` combinations.
- **Isolated setup:** `@Setup(Level.Trial)` excludes construction from timed work and materializes only the active
  representation, avoiding cross-representation cache pressure.
- **Batched work:** each invocation processes hundreds of prebuilt collections instead of one repeatedly hot input.
- **Correct normalization:** `@OperationsPerInvocation` matches the number of collections processed; pairwise benchmarks
  use `NUM_COLLECTIONS / 2`.
- **Dead-code prevention:** every result is consumed by `Blackhole` to prevent the JIT from eliminating the operation.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

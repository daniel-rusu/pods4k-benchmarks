# Benchmark Architecture

## Purpose

This project uses JMH to compare [Immutable Arrays][immutable-arrays-url] with equivalent operations on regular arrays,
`ArrayList`, and lists from  [kotlinx.collections.immutable][persistent-list-url].

## Code Organization

| Source set        | Responsibility                                                                |
|-------------------|-------------------------------------------------------------------------------|
| `src/jmh/kotlin`  | Benchmarks - JMH lifecycle, typed operation dispatch, and measured operations |
| `src/main/kotlin` | Data generation - Deterministic element generators and collection factories   |
| `src/test/kotlin` | Validation tests for `src/main` utilities and data builders                   |

## Benchmark Matrix

`CollectionBenchmark` defines two JMH parameters, `CollectionType` & `DataType`:

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
5. The throughput is normalized based on `@OperationsPerInvocation`
    - Usually normalized by batch size, except for operations that consume 2 collections at a time.

## Data Generation Recipe

Benchmark classes define the data-generation recipe:

| Ingredient                                                                   | Explanation                                                                                                                                                                                                                                                                                                                                                                                                        |
|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Batch Size                                                                   | Controls how many collections will be created for the current `(CollectionType, DataType)`  combination.                                                                                                                                                                                                                                                                                                           |
| `DistributionFactory`                                                        | - Creates a `Distribution` to control collection sizes.<br/>- The batch will contain collections of varying sizes sampled from this distribution.<br/>- A separate `DistributionFactory` is specified for nested collections to control inner collection sizes.                                                                                                                                                    |
| `PrimitiveGeneratorFactory`                                                  | - Creates a `PrimitiveGenerator` for generating primitive values.                                                                                                                                                                                                                                                                                                                                                  |
| `ObjectGeneratorFactory`                                                     | - Creates a `ObjectGenerator` for generating object values.                                                                                                                                                                                                                                                                                                                                                        |
| `PredicateMatchingObjectGenerator`<br/>`PredicateMatchingPrimitiveGenerator` | - Calling `withPredicateMatchRatio(matchRatio, predicate)` on a generator factory wraps the factory so that a random subset of the generated elements will pass the predicate in accordance with the specified `matchRatio`.<br/>- E.g. Configuring `factory.withPredicateMatchRatio(0.6, MedianValuePredicate)` produces collections whose elements each have a 60% chance of passing the `MedianValuePredicate`. |
| `NullabilityPolicyFactory`                                                   | Creates a `NullabilityPolicy` for deciding when values should be null.                                                                                                                                                                                                                                                                                                                                             |

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
    * Uses `CollectionFactory` to materialize a `CollectionBatch` containing an array of collections.
        * Collection are populated with elements generated from the `PrimitiveGenerator` or `ObjectGenerator` depending
          on the `DataType`

Although a benchmark has 36 parameter combinations, a `CollectionBatch` stores only the current combination. For
example, the `LIST`/`BOOLEAN` trial contains an array of `List<Boolean>` collections.

## Fair-Comparison Safeguards

- **Public APIs:** every representation uses an equivalent, statically typed operation through its published API.
- **Specialization:** JVM arrays and Immutable Arrays retain primitive-specialized forms when storing primitives to
  represent real-world usage.
- **Comparable trials:** The RNG stream is initialized from a constant seed ensuring a deterministic and repeatable
  dataset across trials.
- **Batched work:** each invocation processes hundreds of prebuilt collections instead of one repeatedly hot input.
- **Equivalent data characteristics:** Independent RNG streams for data generation, size distribution, predicate
  decisions, and nullability decisions to ensure fair comparisons for all data combinations:
    - Same sequence of collection sizes across all `CollectionType` & `DataType` combinations.
    - Same sequence of values across all `CollectionType` representations for a given `DataType`.
    - Same sequence of null positions across all `CollectionType` & `DataType` combinations.
    - Same sequence of predicate outcomes across all `CollectionType` & `DataType` combinations.
- **Isolated setup:** `@Setup(Level.Trial)` excludes construction from timed work and materializes only the active
  representation, avoiding cross-representation cache pressure.
- **Dead-code prevention:** every result is consumed by `Blackhole` to prevent the JIT from eliminating the operation.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

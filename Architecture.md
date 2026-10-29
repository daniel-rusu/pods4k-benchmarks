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

Each benchmark specifies how many collections to create and a data-generation recipe containing a subset of the
following ingredients:

| Ingredient                                                                   | Explanation                                                                                                                                                                                                                                                                |
|------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Distribution`                                                               | - The batch will contain collections of varying sizes sampled from this distribution.<br/>- Nested scenarios, like `flatMap`, specify an additional `nestedSizeDistribution` for nested collection sizes.                                                                  |
| `PrimitiveGenerator`                                                         | - Generates primitive values like `Byte`, `Boolean`, `Int`, etc.                                                                                                                                                                                                           |
| `ObjectGenerator`                                                            | - Generates generic objects, such as `String` or custom compound elements.                                                                                                                                                                                                 |
| `PredicateMatchingObjectGenerator`<br/>`PredicateMatchingPrimitiveGenerator` | - Decorators for benchmarking operations that use predicates, such as `elements.filter { predicate }`<br/>- Eg. `recipe.withPredicateMatchProbability(50.percent(), predicate,...)` results in collections with each element having a 50% chance of passing the predicate. |
| `NullabilityPolicy`                                                          | - Decides which elements should be null.                                                                                                                                                                                                                                   |

## Data Construction

This is the data generation flow for the `drop` operation. All four shapes follow a similar lifecycle:

1. `DropBenchmarks` specifies the trial batch size and standard flat-collection data recipe:
    - Standard distribution of collection sizes.
    - Populate the collections with random strings or random basic values depending on the `DataType`.
2. JMH selects the `CollectionType` & `DataType` and creates a `DropBenchmarks` instance with that combination.
3. `FlatCollectionBenchmark.setupBenchmarkData()` prepares data generation:
    - Creates `BenchmarkGeneratorRngs` from a fixed master seed and splits off separate RNG streams for each attribute
      of the data.
    - Initializes the recipe with the RNG streams and delegates data creation to `FlatCollectionBenchmarkData`
4. `FlatCollectionBenchmarkData.create(...)` creates a `CollectionBatch`
    - The batch stores an array of collections (size = trial batch size)
    - `CollectionFactory` creates each collection and populates it using the element generators

Although a benchmark has 36 `CollectionType` & `DataType` parameter combinations, a `CollectionBatch` stores only the
current combination. For example, the `LIST`/`BOOLEAN` trial operates on batch containing an array of `List<Boolean>`
collections.

## Fair-Comparison Safeguards

- **Public APIs:** every representation uses an equivalent, statically typed operation through its published API.
- **Specialization:** JVM arrays and Immutable Arrays retain primitive-specialized forms when storing primitives to
  represent real-world usage.
- **Comparable trials:** RNG streams are initialized from a constant master seed ensuring a deterministic and repeatable
  dataset across trials.
- **Batched work:** each invocation processes hundreds of prebuilt collections instead of one repeatedly hot input.
- **Equivalent data characteristics:** Independent RNG streams for data generation, outer and nested size
  distributions, predicate decisions, and nullability decisions ensure fair comparisons for all data combinations:
    - Same sequences of outer and nested collection sizes across all `CollectionType` & `DataType` combinations.
    - Same sequence of values across all `CollectionType` representations for a given `DataType`.
    - Same sequence of null positions across all `CollectionType` & `DataType` combinations.
    - Same sequence of predicate outcomes across all `CollectionType` & `DataType` combinations.
- **Isolated setup:** `@Setup(Level.Trial)` excludes construction from timed work and materializes only the active
  representation, avoiding cross-representation cache pressure.
- **Dead-code prevention:** every result is consumed by `Blackhole` to prevent the JIT from eliminating the operation.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

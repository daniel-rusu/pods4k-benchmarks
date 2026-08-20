# Benchmark Architecture

## Purpose

This project uses JMH to compare the published [Immutable Array][immutable-arrays-url] API with equivalent operations on
`List`, [PersistentList][persistent-list-url], and JVM arrays.

## Benchmark Matrix

`CollectionBenchmark` defines two JMH `@Param` axes:

- `CollectionType`
    - `LIST`: `ArrayList` exposed as `List<T>`
    - `PERSISTENT_LIST`: `kotlinx.collections.immutable.PersistentList<T>`
    - `ARRAY`: `Array<T>` for references and nullable values; primitive arrays for non-null primitives
    - `IMMUTABLE_ARRAY`: `ImmutableArray<T>` for references and nullable values; primitive-specialized immutable arrays
      for non-null primitives
- `DataType`: `REFERENCE`, `BOOLEAN`, `BYTE`, `CHAR`, `SHORT`, `INT`, `FLOAT`, `LONG`, and `DOUBLE`

Together, these axes produce **36 independent trials** per benchmark method.

## Runtime Flow

1. A benchmark class defines equivalent, statically typed operations for each collection representation and supplies a
   data-generation recipe.
2. JMH selects one `CollectionType` and `DataType` combination.
3. `@Setup(Level.Trial)` builds a deterministic batch for only that combination.
4. The benchmark invokes the operation on every collection in the batch.
5. JMH consumes each result with `Blackhole` and normalizes the score using `@OperationsPerInvocation`.

## Benchmark Categories

Benchmark categories describe the shape of the data consumed by an operation:

| Category      | Shape                                                       | Examples                                                                    |
|---------------|-------------------------------------------------------------|-----------------------------------------------------------------------------|
| Flat          | `CollectionType<DataType>`                                  | `BooleanArray`<br/>`List<Boolean>`                                          |
| Nullable flat | `CollectionType<DataType?>`                                 | `Array<Double?>`<br/>`List<Double?>`                                        |
| Nested        | `CollectionType<CollectionOwner<CollectionType<DataType>>>` | `Array<CollectionOwner<ByteArray>>`<br/>`List<CollectionOwner<List<Byte>>>` |
| Object        | `CollectionType<CustomType>`                                | `Array<CustomType>`<br/>`List<CustomType>`                                  |

Nested outer and inner collection sizes use separate distributions. This models cases such as orders containing fewer
products than the total number of orders.

## Code Organization

| Source set        | Responsibility                                                                            |
|-------------------|-------------------------------------------------------------------------------------------|
| `src/main/kotlin` | Reusable utilities, shared infrastructure, and deterministic data builders                |
| `src/jmh/kotlin`  | JMH lifecycle, typed operation dispatch, benchmark-only fixtures, and measured operations |
| `src/test/kotlin` | Tests for utilities and data builders in `src/main`                                       |

Reusable data construction stays in `src/main` so it can be unit-tested without JMH. Benchmark lifecycle and
scenario-specific code stay in `src/jmh`.

Each operation follows the same structure:

- `<Operation>Benchmarks` defines the operations and data recipe.
- The class extends the matching `<Category>CollectionBenchmark`.
- `<Category>CollectionBenchmarkData` creates the trial data.
- The category base class dispatches to the typed operation for the active representation.

For example, `FilterBenchmarks` extends `FlatCollectionBenchmark` and uses `FlatCollectionBenchmarkData`.

## 5. Data Construction

This is the data generation flow for the `drop` operation. The nested, nullable-flat, and object-collection benchmark
categories follow the same general pattern:

1. `DropBenchmarks` specifies the data generation recipe:
    * `numCollections` to create
    * Default `DistributionFactory` for sampling the collection sizes
    * Default `ObjectGeneratorFactory<String>` & `FieldGeneratorFactory` for random strings & primitives
2. JMH iterates through every `CollectionType` & `DataType`, and creates `DropBenchmarks` with the current combination.
3. JMH calls `FlatCollectionBenchmark.setupBenchmarkData()` to begin data construction.
4. Data creation is delegated to `FlatCollectionBenchmarkData.create(...)` which performs the following actions
    * Create `SplittableRandom` RNG stream from constant seed
    * Split off separate RNG streams for each aspect of data generation (values, collection sizes, etc.)
    * Use the factories and associated RNG streams to create size `Distribution`, `FieldGenerator`, & `ObjectGenerator`
    * Create a `CollectionBatch` with an array of `numCollections` collections. Each collection is created with
      `CollectionFactory`
        * size sampled from the size `Distribution`
        * `CollectionType` & `DataType` controls the type of collection to be created
        * elements generated from the `FieldGenerator` or `ObjectGenerator` depending on the `DataType`

Although there are 36 `CollectionType` & `DataType` combinations, benchmarking data is only constructed for the current
combination. Eg. the `CollectionBatch` stores an array of `List<Boolean>` collections when `CollectionType = LIST` &
`DataType = BOOLEAN`.

### Nullability Handling

Benchmarks that deal with null values, such as `FilterNotNullBenchmarks`, specify factories that create `null` values
null-ratio portion of the time. During data generation, the next element will be null if a random `double` is less than
the null ratio.

### Predicate Handling

Benchmarks that deal with predicates, such as `FilterBenchmarks`, specify the acceptance ratio. Elements are accepted if
their value is smaller than the median value of their data type. When generating the next element, the RNG determines
whether it should be accepted by checking whether a random `double` is less than the acceptance ratio. If the next
element should be accepted then we repeatedly generate random values discarding them until we find one smaller than the
median value (and vice versa).

Note that predicate decisions are shifted to the data generation phase in order to remove the RNG overhead from the
benchmark, and instead focus on the performance of the operation.

## Fair-Comparison Safeguards

- **Public APIs:** every representation uses an equivalent, statically typed operation through its published API.
- **Realistic specialization:** JVM arrays and Immutable Arrays retain primitive-specialized forms.
- **Equivalent inputs:**
    - Same sequence of collection sizes is used across all `CollectionType` & `DataType` combinations.
    - A given `DataType` results in the same sequence of values across all `CollectionType` representations.
    - Operations that deal with null elements encounter the same sequence of null-element positions across all
      `CollectionType` & `DataType` combinations.
    - Operations that deal with predicates encounter the same sequence of predicate acceptance across all
      `CollectionType` & `DataType` combinations.
- **Isolated setup:** `@Setup(Level.Trial)` excludes construction from timed work and materializes only the active
  representation, avoiding cross-representation cache pressure.
- **Batched work:** each invocation processes hundreds of prebuilt collections instead of one repeatedly hot input.
- **Correct normalization:** `@OperationsPerInvocation` matches the number of collections processed; pairwise benchmarks
  use `NUM_COLLECTIONS / 2`.
- **Dead-code prevention:** every result is consumed by `Blackhole`.

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

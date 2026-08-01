# Benchmark Architecture

## 1. Overview

### Purpose and Scope

This project uses JMH to benchmark operations on [Immutable Arrays][immutable-arrays-url] with equivalent operations on
regular arrays, `ArrayList`, and [PersistentList][persistent-list-url].

### Architecture at a Glance

1. Benchmark classes define
    * Operations to benchmark across representations (eg. `List<Boolean>.filter{...}`, `BooleanArray.filter{...}`, ...)
    * Recipe for data generation (eg. size distribution, element generation, etc.)
2. JMH iterates through each combination of `CollectionType` and `DataType`
3. Create fixed-seed RNG streams and use the data-generation recipe to create collections for the current
   `CollectionType` & `DataType`
4. Invoke the operation on each collection and measure throughput

## 2. Benchmark Model

### Collection Representations

### Data Types

### Parameter Matrix

### Work per Invocation

## 3. Benchmark Categories

Benchmark families are organized by the structure of the data that is consumed.

### Flat Collections

For operating on data structured as `CollectionType<DataType>`

Example `FilterBenchmarks` scenarios:

| Parameters                                      | Measured Operation                  |
|-------------------------------------------------|-------------------------------------|
| `CollectionType = LIST` & `DataType = BOOLEAN`  | `List<Int>.filter { predicate }`    |
| `CollectionType = ARRAY` & `DataType = BOOLEAN` | `BooleanArray.filter { predicate }` |

### Nullable Flat Collections

For operating on data structured as `CollectionType<DataType?>`

Example `FilterNotNullBenchmarks` scenarios:

| Parameters                                  | Measured Operation            |
|---------------------------------------------|-------------------------------|
| `CollectionType = LIST` & `DataType = INT`  | `List<Int?>.filterNotNull()`  |
| `CollectionType = ARRAY` & `DataType = INT` | `Array<Int?>.filterNotNull()` |

### Nested Collections

For operating on data structured as `CollectionType<CollectionOwner<CollectionType<DataType>>`

* E.g. List of orders with each order containing a list of products

Example `FlatMapBenchmarks` scenarios:

| Parameters                                      | Measured Operation                                                            |
|-------------------------------------------------|-------------------------------------------------------------------------------|
| `CollectionType = LIST` & `DataType = BOOLEAN`  | `List<CollectionOwner<List<Boolean>>.flatMap { it.nestedCollection }`         |
| `CollectionType = ARRAY` & `DataType = BOOLEAN` | `List<CollectionOwner<BooleanArray>.flatMap { it.nestedCollection.asList() }` |

Nested collections use a separate size distribution to better model the real world as the number of products in an order
it usually smaller than the number of orders.

### Object Collections

For operating on data structured as `CollectionType<CustomType>`

Example `MapBenchmarks` scenarios:

| Parameters               | Measured Operation               |
|--------------------------|----------------------------------|
| `CollectionType = LIST`  | `List<Person>.map { it.field }`  |
| `CollectionType = ARRAY` | `Array<Person>.map { it.field }` |

These benchmarks are only parameterized by `CollectionType` so they contain 9 separate benchmark methods to measure
the impact of different field types (eg. `mapBoolean()` measures `CollectionType<Person>.map { it.isMarried }` )

## 4. Code Organization

### Benchmark Definitions (`src/jmh`)

#### Benchmark Classes

#### Base States and Parameter Dispatch

### Benchmark Data (`src/main/.../immutableArrays`)

#### Benchmark-Family Data Builders

#### Collection Construction and Storage

### Data-Generation Utilities (`src/main/.../utils`)

#### Distributions

#### Generators and Random Sources

### Tests (`src/test`)

### Build Configuration

## 5. Benchmark Lifecycle

### JMH Parameter Selection

### Trial Setup

### Data Generation and Materialization

### Typed Operation Dispatch

### Measurement and Result Consumption

## 6. Data Construction

### Deterministic Random Streams

### Collection Sizes and Element Values

### Collection Factory

### Collection Batch

*Shared storage for the active parameter combination, with typed access provided by each benchmark family.*

### Benchmark-Family Data Builders

## 7. Measurement Invariants

### Setup Versus Measured Work

### Equivalent Operations Across Representations

### Comparable Input Data

### Operations per Invocation

### Public API Boundary

[immutable-arrays-url]: https://github.com/daniel-rusu/pods4k/tree/main/immutable-arrays

[persistent-list-url]: https://github.com/Kotlin/kotlinx.collections.immutable

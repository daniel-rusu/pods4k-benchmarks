# pods4k-benchmarks

This repository benchmarks the published [pods4k library](https://github.com/daniel-rusu/pods4k) through its public API.
Keeping the benchmarks separate from the library prevents accidental access to implementation details and better
represents normal dependency usage.

The project uses the [Java Microbenchmark Harness (JMH)](https://github.com/openjdk/jmh) with the
[JMH Gradle plugin](https://github.com/melix/jmh-gradle-plugin).

## Project Layout

- `build.gradle.kts`: JMH benchmark selection and result configuration.
- `src/jmh/kotlin`: JMH benchmark classes. Categorized into flat collections of values, nested collections (e.g., a list
  of orders with each order containing a list of products), and collections of objects.
- `src/main/kotlin`: reusable data generators, collection factories, and deterministic benchmark-data builders.
- `src/test/kotlin`: unit tests for shared utilities and data builders.

## Running Benchmarks

1. Clone the repository and ensure you have JDK 21 installed.
2. Set `jmh.includes` in [build.gradle.kts](build.gradle.kts) to the smallest class or package you want to measure:

   ```kotlin
   jmh {
       includes = listOf("immutableArrays.nestedCollectionBenchmarks.FlatMapBenchmarks")
   }
   ```

3. Prepare the machine:
    * Plug in your laptop.
    * Select a performance power profile.
    * Change your screen and sleep timeouts to `Never`.
    * Disable screen savers.
    * Temporarily pause Windows updates.
    * Turn off unnecessary startup programs and services, then restart your computer.
    * Close applications and stop unnecessary processes to minimize interference.
4. Run the selected benchmarks:

   ```shell
   ./gradlew jmh
   ```

Results are written to `build/results/jmh/results.csv`. Check the relative error before drawing conclusions; rerun a
result whose error exceeds 5% because environmental interference may have made it unreliable.

Benchmark runs are deliberately long. Total measurement time grows with the number of benchmark methods, JMH parameter
combinations, warmup and measurement iterations, and forks. Every benchmark expands across four collection types and
nine data types (36 combinations per benchmark method), so even one benchmark class can take tens of minutes. Inspect
its annotations before running a broad include.

## Validation Without Running Benchmarks

- `./gradlew test`: run unit tests.
- `./gradlew jmhClasses`: compile JMH sources without executing benchmarks.
- `./gradlew build --no-daemon`: assemble the project, run tests, and compile JMH sources.
- `./gradlew build jmhJar --no-daemon`: run the CI-equivalent build and generate the executable JMH harness jar.

## JMH Conventions

| Annotation                 | Explanation                                                                                                                                                                                                                                                                                                |
|:---------------------------|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `@State`                   | Marks the class as storing data that the benchmarks will operate on and controls how this data is shared. <br/> - `Scope.Benchmark` - The dataset will be shared across all threads.                                                                                                                       |
| `@Setup`                   | Marks the setup method that prepares benchmark data.<br/> - `Level.Trial`: Performs setup once for each benchmark trial.                                                                                                                                                                                   |
| `@Benchmark`               | Marks the method that contains the operation that we want to benchmark.                                                                                                                                                                                                                                    |
| `@Param`                   | Parameterizes a variable with multiple values and repeats all the benchmarks for each value.                                                                                                                                                                                                               |
| `@BenchmarkMode`           | Specifies what we're trying to measure. <br/> - `Mode.Throughput`: Measures the number of operations per time unit.                                                                                                                                                                                        |
| `@OutputTimeUnit`          | Specifies the time unit to report the results in <br/> - `TimeUnit.SECONDS`: Report the results per second. <br/> - `TimeUnit.MILLISECONDS`: Report the results per millisecond.                                                                                                                           |
| `@OperationsPerInvocation` | Specifies how many logical operations one benchmark-method invocation performs, allowing JMH to normalize its reported score per operation. <br/> - Most benchmarks process hundreds of deterministically generated collections per invocation. Pairwise benchmarks process half as many collection pairs. |
| `@Warmup`                  | Controls warmup so execution reaches a steady state before measurement.<br/> - `iterations`: Number of warmup iterations. <br/> - `time`: Duration of each warmup iteration. <br/> - `timeUnit`: Unit for the duration (e.g., seconds or milliseconds).                                                    |
| `@Measurement`             | Controls how many measurements to take and how long each measurement should last.                                                                                                                                                                                                                          |
| `@Fork`                    | Controls how many JVM processes should be created to run each benchmark. <br/> - This should be set to at least 1 in order to avoid benchmarks from affecting each other. A value at least 2 is even better as it provides more information about repeatability and margin of error.                       |

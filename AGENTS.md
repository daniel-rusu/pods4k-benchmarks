# Repository Guidance

`pods4k-benchmarks` is a Kotlin/JVM benchmark project for the external `pods4k` library. It keeps benchmarks outside the library repo so they exercise `pods4k` only through its published dependency and public API.

## Collaboration Rules

- Unless the user explicitly asks you to make changes, do not edit files. For exploratory, design, review, or "is it possible" requests, respond with findings and/or a proposed plan first.
- When the user does explicitly ask for changes, proceed with the requested edits as long as the scope is clear. If the scope is ambiguous, clarify the plan before editing.

## Important Directories

- `src/main/kotlin/com/danrusu/pods4kBenchmarks/utils`: reusable distributions, RNGs, and data generators.
- `src/main/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays`: shared parameters, factories, and deterministic
  benchmark-data builders.
- `src/test/kotlin/com/danrusu/pods4kBenchmarks`: JUnit 5 + Strikt tests for shared utilities and data builders.
- `src/jmh/kotlin/com/danrusu/pods4kBenchmarks/immutableArrays`: JMH states, benchmark-only fixtures, and benchmarks.
- `build.gradle.kts`, `gradle/libs.versions.toml`: single Gradle module, Kotlin/JVM, JMH plugin, and dependency versions.
- `README.md`: human benchmark-running instructions and machine-preparation notes.

## Commands

- `./gradlew build --no-daemon`: assembles main source, runs tests, and compiles JMH benchmark sources.
- `./gradlew build jmhJar --no-daemon`: CI command; also generates and compiles the JMH harness and packages the JMH jar.
- `./gradlew test`: cheap utility test run.
- `./gradlew jmhClasses`: compile JMH benchmark sources without executing benchmarks.
- `./gradlew jmh`: runs configured benchmarks and writes CSV results under `build/results/jmh/`.
- Windows shell equivalent: use `.\gradlew.bat ...`.
- No repository lint or format task is configured. Kotlin style is `official` in `gradle.properties`.

## Generated Code

- Do not edit `build/`, `.gradle/`, `.kotlin/`, or JMH-generated outputs.
- Checked-in source lives under `src/**`; no checked-in generated source was found.
- JMH annotation processing/generation is build output only.

## Kotlin And API Conventions

- Use package `com.danrusu.pods4kBenchmarks...`.
- Keep reusable, unit-testable data construction in `src/main`; keep JMH states and scenario-only fixtures in `src/jmh`.
- Tests use JUnit 5 `@Test` and Strikt assertions.
- Keep tests focused by behavior or class under test. Put reusable test fakes/fixtures in clearly named fixture packages and files instead of bundling unrelated helpers into one catch-all test-support file.
- Benchmark code should compare `List`, `PersistentList`, JVM arrays, and `ImmutableArray` variants through public `pods4k` APIs.
- Do not reach into `pods4k` internals or copy library implementation details into this repo.

## Performance Constraints

- Preserve deterministic setup data unless intentionally changing benchmark scenarios: data builders use a constant
  `RngFactory` master seed and purpose-specific streams so value generation does not also change collection sizes,
  null placement, or filter acceptance.
- Consume benchmark results with JMH `Blackhole`.
- Keep `@OperationsPerInvocation` aligned with the number of collections processed per invocation; pairwise benchmarks usually process `NUM_COLLECTIONS / 2` collection pairs.
- Avoid changing warmup, measurement, fork counts, or `jmh.includes` as part of unrelated edits.
- Do not run benchmarks casually; they can take many minutes or hours.

## Public API Compatibility

- This repo validates the externally published `com.danrusu.pods4k:pods4k` dependency. Treat any required `pods4k` API change as a compatibility concern to report, not as something to patch locally.
- Keep benchmarks representative of public usage: no reflection or internal package access unless explicitly requested and reviewed.

## Done Means

- Relevant tests or compile checks were run, or skipped with a clear reason.
- Benchmark changes include honest notes about whether benchmarks were only compiled or actually executed.
- Generated/build outputs are not edited or committed.
- Existing unrelated worktree changes are preserved.
- Documentation is updated when workflow, benchmark structure, or verification commands change.

## More Context

- [Context map](docs/ai/context-map.md)
- [Architecture](docs/ai/architecture.md)
- [Benchmark workflow](docs/ai/benchmark-workflow.md)
- [Code review checklist](docs/ai/code-review.md)

# Benchmark Workflow

## Cheap Validation

- `./gradlew test`: run unit tests for shared utilities.
- `./gradlew build --no-daemon`: CI-equivalent check; assembles main source and runs tests.
- Windows: use `.\gradlew.bat test` or `.\gradlew.bat build --no-daemon`.

## Benchmark Compile Checks

- Preferred benchmark compile check: `./gradlew jmhClasses`.
- Narrower tasks shown by Gradle include `compileJmhKotlin`, `compileJmhJava`, `processJmhResources`, `jmhRunBytecodeGenerator`, and `jmhCompileGeneratedClasses`.
- `./gradlew build --no-daemon` is still useful for CI parity, but Gradle dry-run output shows it does not depend on `jmhClasses`.

## Running Benchmarks

- Configure `jmh.includes` in `build.gradle.kts` to target the class/package being investigated.
- Run: `./gradlew jmh`.
- Results: `build/results/jmh/results.csv`.
- README guidance says a benchmark class can take about 20 minutes, and broader categories can take hours.
- Prepare the machine first: plugged in, performance power profile, sleep/screensaver disabled, background work minimized.

## When Not To Run Benchmarks

- Do not run benchmarks for documentation-only changes.
- Do not run broad benchmark categories unless the user asked for benchmark numbers and accepts the runtime.
- Do not run benchmarks while the machine is under load or power-saving settings are active.
- Do not change benchmark parameters just to make a run faster unless the report clearly says the run is not comparable to normal results.

## Reporting Benchmark Changes

- Say whether you ran tests, compiled benchmarks, or executed JMH.
- If only compile checks ran, say "benchmarks were compiled but not run."
- If JMH ran, report the command, include filter, JVM/JDK if known, and result file path.
- Do not claim performance improvement from code inspection alone.
- Treat high relative error as inconclusive; README recommends rerunning when relative error is above 5%.
- Mention any changes to `jmh.includes`, warmup, measurement, fork count, data distributions, or `OperationsPerInvocation`.

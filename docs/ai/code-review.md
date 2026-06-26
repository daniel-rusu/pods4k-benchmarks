# Code Review Checklist

Use this checklist when reviewing Codex-generated changes in this repo.

## Scope

- Change is limited to the requested area.
- Existing unrelated worktree changes are preserved.
- No generated output, benchmark CSVs, IDE files, or Gradle caches are edited as source.

## Generated-Code Consistency

- No manual edits under `build/`, `.gradle/`, `.kotlin/`, or JMH generated output.
- No checked-in generated source is introduced without a documented generator and verification path.

## Tests

- Utility changes have focused tests in `src/test/kotlin`.
- Tests use JUnit 5 and Strikt, matching existing style.
- Exact exception messages are updated only when intentional and reflected in tests.
- Report the command run, usually `./gradlew test` or `./gradlew build --no-daemon`.

## Benchmarks

- New benchmark classes live under `src/jmh/kotlin`.
- Benchmark methods use JMH annotations consistent with neighboring benchmarks.
- `Blackhole` consumes measured results.
- Data setup happens in `@Setup(Level.Trial)` unless construction cost is the benchmark target.
- Constant seeds and separate RNG streams are preserved so collection sizes, null placement, and data values do not pollute each other.
- `@OperationsPerInvocation` matches collections processed per invocation, including pairwise loops.
- `jmh.includes` changes are intentional and called out.
- Benchmark claims distinguish compile checks from actual JMH runs.

## Documentation

- `README.md` is updated for user-facing workflow changes.
- `AGENTS.md` and `docs/ai/*.md` are updated when repository conventions, commands, or benchmark structure change.
- Docs stay concise and avoid copying large source snippets.

## Gradle And Source Sets

- Dependency versions belong in `gradle/libs.versions.toml`.
- Build behavior belongs in `build.gradle.kts`.
- Main utilities belong in `src/main`; tests in `src/test`; benchmark-only code in `src/jmh`.
- CI parity remains `./gradlew build --no-daemon`.
- Kotlin style remains compatible with `kotlin.code.style=official`.

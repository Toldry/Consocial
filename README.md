# Clarity AI Senior Developer Code Test
This project is my solution to Clarity AI's Senior Developer Code Test.

A console-based social networking application written in Kotlin, supporting posting messages, reading timelines, following users, and viewing aggregated walls.

---

## Requirements

- **JDK 21** — the Gradle toolchain will download it automatically via the [Foojay resolver](https://github.com/gradle/foojay-toolchains) if not already installed.
- **Git** — to clone the repository.

No other dependencies are required. There are no external frameworks; the project uses only the Kotlin standard library and the JDK.

---

## Installation

```bash
git clone https://github.com/Toldry/Consocial
cd consocial
```

The Gradle wrapper (`gradlew`) is included — no separate Gradle installation is needed.

---

## Building

```bash
./gradlew build
```

This compiles the main and test sources and runs the full test suite. Compiled classes are placed under `build/`.


## Linting

```bash
./gradlew ktlintFormat
```

Reformats all Kotlin files.

---

## Running

```bash
./gradlew run
```

Or build a JAR and run it directly:

```bash
./gradlew jar
java -jar build/libs/consocial-1.0.jar
```

### Development shortcut

```bash
./gradlew run
```

Press `Ctrl+C` to exit (or `Ctrl+Z` + Enter on Windows if using `./gradlew run`).

The application reads commands from standard input, one per line.

### Supported commands

| Input | Action |
|---|---|
| `Alice -> I love the weather today` | Alice posts a message |
| `Alice` | Display Alice's timeline (newest first) |
| `Charlie follows Alice` | Charlie subscribes to Alice's posts |
| `Charlie wall` | Display Charlie's timeline plus all followed users' posts |

### Example session

```
> Alice -> I love the weather today
> Bob -> Damn! We lost!
> Bob -> Good game though.
> Alice
I love the weather today (1 second ago)
> Charlie follows Alice
> Charlie wall
Charlie - Good game though. (2 minutes ago)
Alice - I love the weather today (5 minutes ago)
```

---

## Testing

```bash
./gradlew test
```

Test reports are written to `build/reports/tests/test/index.html`.

The test suite uses [Kotlin Test](https://kotlinlang.org/api/latest/kotlin.test/) with the JUnit 5 platform. A `FixedClock` test double is used throughout to make time-dependent tests fully deterministic.

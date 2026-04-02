# Clarity AI Senior Developer Code Test
This project is my solution to Clarity AI's Senior Developer Code Test.

A console-based social networking application written in Kotlin, supporting posting messages, reading timelines, following users, and viewing aggregated walls.

---


## Approach, assumptions, and tradeoffs
I tried building this project with Clean Architecture principles in mind. 
The code is separated into the following directories, or "layers":
- **model**: Defines the entities used in the application.
- **repository**: Data creation, retrieval, and storage, based on the models.
- **service**: Consume data from repositories and transforms them for use in the UI.
- **parser**: Parses user input from the console, and transforms them into *model* objects.
- **app**: The actual console application.

### Storing followers and followees in the `User` object
One of the design decisions I made was to store followers in the `User` object.
In principle, the `followers` field is currently not needed, but I decided to add it anyway in anticipation that a future requirement might make use of this data structure.

Each `User` has both `followers` and `followees`, therefore each `follow` command creates a new listing in both the follower and followee `User` objects, which doubles the storage requirement for the following feature.

Another disadvantage of this approach is that it makes it possible to have illegal database states such as `userA` being in `userB`'s `followers` list, without `userB` being in `userA`'s `followees` list.


### NOT storing user messages in the `User` object
As well as followers, I considered adding a `messages` item in the `User` object. 

I rejected this choice in favor of cleanly separating the logic and implementation of the `User` and `Message` repositories. 
This follows the Single Responsibility Principle. It allows testing `User` and `Message` logic separately and independently.

### Users don't exist until they post at least one message
Another design decision I made is that users do not exist until they post at least one message.

One advantage of this approach is that if a user wants to see Alice's timeline, but accidentally types `Alcie` instead of `Alice`, they get a helpful error message rather than a confusingly empty timeline.

The same helpful error message advantage also apply to the `wall` and `follow` commands.

A disadvantage of this approach is that a future requirement might be to introduce a more complicated registration process (such as creating an account with an email).
It would be then better to allow the user to register without obligating them to post a message.

Also, there might be users that want only to "lurk", i.e, to follow other users and view their posts, without posting anything themselves.

### Test Driven Development
I wrote this project with a TDD approach, as can be seen in the git history.
I wrote stubs for all the classes, then I wrote the corresponding tests, and then finally I wrote the implementation.

This helped ensure that the solution was valid, robust, and flexible for future changes.

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

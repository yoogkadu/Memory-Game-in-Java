# Memory Game

A classic memory game implemented in Java Swing where players have to remember and recall sequences of numbers. The game features multiple difficulty levels, scoring, animations, sound effects, and a persistent leaderboard.

## Features

- **Multiple Difficulties:** Easy, Medium, and Hard levels that adjust sequence length, display time, and number complexity.
- **Animated UI:** Smooth, 60 FPS animations for number reveals, feedback, and transitions.
- **Scoring System:** Points awarded for correct sequences, with partial credit available.
- **Persistent Leaderboard:** High scores are saved locally on your machine in `~/.memorygame/scores.csv`.
- **Sound Effects:** Optional sound effects for key game events.
- **Keyboard Shortcuts:** Fully playable using the keyboard (Enter, Arrow Keys, Esc).

## Build Instructions

This project is built using Apache Maven. You need to have Java 21 and Maven installed.

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd memory-game
    ```

2.  **Build the project:**
    Use Maven to compile the code and package it into a runnable JAR file.
    ```bash
    mvn clean package
    ```
    This will create a `memory-game-1.0.0.jar` file in the `target/` directory.

## How to Run

Once the project is built, you can run the game using the following command:

```bash
java -jar target/memory-game-1.0.0.jar
```

## Sound Files (Required)

This project uses sound effects that are not included in the repository. To enable sound, you must provide your own `.wav` files and place them in the `src/main/resources/sounds/` directory before building the project.

The required files are:
- `reveal.wav`: Played when the number sequence is shown.
- `correct.wav`: Played for a correct guess.
- `incorrect.wav`: Played for an incorrect guess.

If these files are not present, the game will run without sound effects and will print a warning to the console.

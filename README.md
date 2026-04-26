# SudoClean Architect

**SudoClean** is a high-performance Sudoku application built with Java, focusing on clean Object-Oriented Design, test-driven development, and the implementation of classic design patterns.

## Project Information
* **Name of Project:** SudoClean Architect
* **Team Members:** Aaditya Yanamandra (It's just me)
* **Language:** Java (OpenJDK 25.0.2)
* **Build System:** Gradle
* **Persistence:** N/A
* **UI:** Rudimentary using JavaFX

---

## Design Patterns & Architecture

To satisfy the core requirements of Object-Oriented Design, this project implements the following patterns:

### 1. Strategy Pattern
* **Explanation:** By defining the ISolveStrategy interface and creating separate classes like BacktrackingStrategy and HeuristicSolveStrategy, I allow the solver's algorithm to be swapped at runtime without changing the Board or UI code.

### 1. Observer Pattern
* **Explanation:** My Board class acts as the "Subject" and SudokuUI acts as the "Observer." When a cell value changes in the model, the Board notifies all registered observers (via onCellChanged), which allows the UI to update automatically without the model needing to know anything about JavaFX.

### 1. Singleton Pattern
* **Explanation:** I utilize a GameConfig class (referenced in SudokuUI) to the global difficulty level. This ensures that throughout the application's lifecycle, there is only one source of truth for the game's current configuration.

### 2. Command Pattern
* **Explanation:** My StepSolver and Timeline implementation acts as a state machine. Instead of a single recursive call, they encapsulate the state of the solve (the Stack of BoardState objects) so that each "step" can be executed, paused, or resumed as a distinct command pulse.

### 3. Iterator Pattern
* **Explanation:** The StepSolver essentially provides an internal iterator over the solution space. By calling step(), the UI "iterates" through the possible board states one by one until the Stack is empty or the board is full, rather than receiving the entire solution at once.

---

## OO Principles Implemented

* **Coding to Abstractions:** All major systems (Solving, Commands, Persistence) are defined by interfaces in the `com.sudoclean.core` package.
* **Dependency Injection:** The `Board` class does not instantiate its own solver. Instead, a concrete `ISolveStrategy` is injected into the `attemptSolve()` method.
* **Polymorphism:** The Undo stack and the Solver both treat concrete implementations through their interface types, eliminating the need for complex `if-else` or `switch` statements.

---

## Execution

This project can be run with ./gradlew run --args"hard/easy" (argument is optional)

## Note

The code having to do with the repository pattern was removed, as I genuinely am not of the opinion that a save/load system at all fits this project.
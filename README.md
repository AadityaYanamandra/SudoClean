# SudoClean Architect

**SudoClean** is a high-performance Sudoku application built with Java, focusing on clean Object-Oriented Design, test-driven development, and the implementation of classic design patterns.

## 🛠 Project Information
* **Name of Project:** SudoClean Architect
* **Team Members:** Aaditya Yanamandra (It's just me)
* **Language:** Java (OpenJDK 21)
* **Build System:** Gradle
* **Persistence:** JSON (via GSON)

---

## Design Patterns & Architecture

To satisfy the core requirements of Object-Oriented Design, this project implements the following patterns:

### 1. Strategy Pattern
* **Location:** `com.sudoclean.core.ISolveStrategy` and `com.sudoclean.logic.BacktrackingStrategy`
* **Explanation:** The solving logic is decoupled from the `Board` class. By coding to the `ISolveStrategy` abstraction, the application can swap between different algorithms (e.g., Brute Force vs. Heuristic) at runtime without modifying the underlying data structure.

### 2. Command Pattern
* **Location:** `com.sudoclean.core.ICommand` and `com.sudoclean.commands.PlaceNumberCommand`
* **Explanation:** Every move made by a user is encapsulated as an object. This allows for **Polymorphic** treatment of actions, enabling a robust Undo/Redo system where the invoker doesn't need to know the specifics of the move to revert it.

### 3. Repository Pattern (Persistence)
* **Location:** `com.sudoclean.core.IGameRepository` and `com.sudoclean.logic.JsonGameRepository`
* **Explanation:** This pattern abstracts the data logic. It handles the **Persisted State** requirement by saving and loading the `Board` state to a local JSON file, ensuring the domain logic is independent of the storage format.

---

## OO Principles Implemented

* **Coding to Abstractions:** All major systems (Solving, Commands, Persistence) are defined by interfaces in the `com.sudoclean.core` package.
* **Dependency Injection:** The `Board` class does not instantiate its own solver. Instead, a concrete `ISolveStrategy` is injected into the `attemptSolve()` method.
* **Polymorphism:** The Undo stack and the Solver both treat concrete implementations through their interface types, eliminating the need for complex `if-else` or `switch` statements.

---

## Testing & Execution

This project includes **7 meaningful test cases** covering validation logic, recursive solving, command undoing, and JSON persistence.

### To Run Tests:
```bash
./gradlew test
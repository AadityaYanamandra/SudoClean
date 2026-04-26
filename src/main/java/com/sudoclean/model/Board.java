package com.sudoclean.model;

import java.util.ArrayList;
import java.util.List;

import com.sudoclean.core.IBoardObserver;
import com.sudoclean.core.ISolveStrategy;

public class Board {
    private int[][] grid;
    public static final int SIZE = 9;

    private List<IBoardObserver> observers = new ArrayList<>();

    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    public void setCell(int row, int col, int value) {
        grid[row][col] = value;
        //the board doesn't know what's listening, just that it implements IBoardObserver
        for (IBoardObserver observer : observers) {
            observer.onCellChanged(row, col, value);
        }
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public boolean isValid(int row, int col, int val) {
        // Row and Column check
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == val || grid[i][col] == val) return false;
        }

        // 3x3 Subgrid check
        int boxRowStart = row - row % 3;
        int boxColStart = col - col % 3;

        for (int r = boxRowStart; r < boxRowStart + 3; r++) {
            for (int c = boxColStart; c < boxColStart + 3; c++) {
                if (grid[r][c] == val) return false;
            }
        }
        return true;
    }

    // Dependency Injection: Pass the strategy in
    public boolean attemptSolve(ISolveStrategy strategy, java.util.function.BooleanSupplier shouldStop) {
        return strategy.solve(this, shouldStop);
    }

    public void addObserver(IBoardObserver observer) {
        observers.add(observer);
    }

    public void generateNewGame(int emptyCellsCount) {
        // 1. Completely reset the grid
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0; 
            }
        }

        // 2. Use the Random Strategy to create a valid full board
        // We temporarily remove observers so the UI doesn't "blink" through the generation
        List<IBoardObserver> tempObservers = new ArrayList<>(observers);
        observers.clear();
        
        attemptSolve(new com.sudoclean.logic.RandomSolveStrategy(), () -> false);
        
        // 3. Dig holes based on difficulty
        java.util.Random rand = new java.util.Random();
        int holes = 0;
        while (holes < emptyCellsCount) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            if (grid[r][c] != 0) {
                grid[r][c] = 0;
                holes++;
            }
        }

        // 4. Restore observers and trigger a full UI refresh
        observers.addAll(tempObservers);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                setCell(r, c, grid[r][c]); // This notifies the UI
            }
        }
    }
}
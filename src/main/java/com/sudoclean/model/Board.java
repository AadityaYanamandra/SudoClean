package com.sudoclean.model;

import java.util.ArrayList;
import java.util.List;

import com.sudoclean.core.IBoardObserver;
import com.sudoclean.core.ISolveStrategy;

public class Board {
    private int[][] grid;
    private boolean[][] isOriginal;
    public static final int SIZE = 9;

    private List<IBoardObserver> observers = new ArrayList<>();

    public Board() {
        this.grid = new int[SIZE][SIZE];
        this.isOriginal = new boolean[SIZE][SIZE];
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

    public void setOriginal(int row, int col, int value) {
        isOriginal[row][col] = (value != 0);
        setCell(row, col, value);
    }

    public boolean isOriginal(int row, int col) {
        return isOriginal[row][col];
    }

    public void clearSolverWork() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                // ONLY set to 0 if it was NOT part of the original puzzle
                if (!isOriginal[r][c]) {
                    setCell(r, c, 0);
                }
            }
        }
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

    // dependency injection: pass the strategy in
    public boolean attemptSolve(ISolveStrategy strategy, java.util.function.BooleanSupplier shouldStop) {
        return strategy.solve(this, shouldStop);
    }

    public void addObserver(IBoardObserver observer) {
        observers.add(observer);
    }

    public void generateNewGame(int emptyCellsCount) {
        // 1. Reset everything first
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                grid[r][c] = 0;
                isOriginal[r][c] = false; // Reset the "puzzle" mask
            }
        }

        // temporarily disable observers for silent generation
        List<IBoardObserver> tempObservers = new ArrayList<>(observers);
        observers.clear();
        
        // fill the board using the heuristic strategy
        attemptSolve(new com.sudoclean.logic.HeuristicSolveStrategy(), () -> false);
        
        // dig holes based on difficulty
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

        // capture the "Puzzle State"
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[r][c] != 0) {
                    isOriginal[r][c] = true; // These are the numbers the user sees at start
                }
            }
        }

        // 5. Restore observers and refresh UI
        observers.addAll(tempObservers);
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                setCell(r, c, grid[r][c]); 
            }
        }
    }
}
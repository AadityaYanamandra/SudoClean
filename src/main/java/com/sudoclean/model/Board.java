package com.sudoclean.model;

import com.sudoclean.core.ISolveStrategy;

public class Board {
    private int[][] grid;
    public static final int SIZE = 9;

    public Board() {
        this.grid = new int[SIZE][SIZE];
    }

    public void setCell(int row, int col, int value) {
        grid[row][col] = value;
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
    public boolean attemptSolve(ISolveStrategy strategy) {
        return strategy.solve(this);
    }
}
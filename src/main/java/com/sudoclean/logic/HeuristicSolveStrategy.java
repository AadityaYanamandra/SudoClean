package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;
import java.util.function.BooleanSupplier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HeuristicSolveStrategy implements ISolveStrategy {
    @Override
    public boolean solve(Board board, BooleanSupplier shouldStop) {
        if (shouldStop.getAsBoolean()) return false;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getCell(row, col) == 0) {
                    // get all valid numbers and sort them by how many neighbors they would "constrain" (Least Constraining Value)
                    List<Integer> candidates = getOrderedCandidates(board, row, col);

                    for (int n : candidates) {
                        if (shouldStop.getAsBoolean()) return false;

                        if (board.isValid(row, col, n)) {
                            board.setCell(row, col, n);
                            
                            try { Thread.sleep(20); } 
                            catch (InterruptedException e) { return false; }

                            if (solve(board, shouldStop)) return true;

                            if (shouldStop.getAsBoolean()) return false;
                            board.setCell(row, col, 0);
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private List<Integer> getOrderedCandidates(Board board, int row, int col) {
        List<Integer> vals = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (board.isValid(row, col, i)) {
                vals.add(i);
            }
        }

        // sort candidates using the Least Constraining Value heuristic
        vals.sort((a, b) -> {
            int constraintA = countConstraints(board, row, col, a);
            int constraintB = countConstraints(board, row, col, b);
            return Integer.compare(constraintA, constraintB);
        });

        return vals;
    }

    /**
     * Counts how many neighbors would lose a valid move if 'val' is placed at (row, col).
     * Higher count = more "constraining".
     */
    private int countConstraints(Board board, int row, int col, int val) {
        int count = 0;
        
        // check constraints in the same row and column
        for (int i = 0; i < 9; i++) {
            if (i != col && board.getCell(row, i) == 0 && !board.isValid(row, i, val)) count++;
            if (i != row && board.getCell(i, col) == 0 && !board.isValid(i, col, val)) count++;
        }

        // check constraints in the 3x3 box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if ((r != row || c != col) && board.getCell(r, c) == 0 && !board.isValid(r, c, val)) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
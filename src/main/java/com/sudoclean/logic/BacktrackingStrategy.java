package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;
import java.util.function.BooleanSupplier;

public class BacktrackingStrategy implements ISolveStrategy {
    @Override
    public boolean solve(Board board, BooleanSupplier shouldStop) {
        if (shouldStop.getAsBoolean()) return false;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getCell(row, col) == 0) {
                    for (int n = 1; n <= 9; n++) {
                        // Check stop signal frequently
                        if (shouldStop.getAsBoolean()) return false;

                        if (board.isValid(row, col, n)) {
                            board.setCell(row, col, n);
                            
                            try { Thread.sleep(20); } 
                            catch (InterruptedException e) { 
                                Thread.currentThread().interrupt();
                                return false; 
                            }

                            if (solve(board, shouldStop)) return true;

                            // CRITICAL: If the user hit stop, DO NOT set cell back to 0
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
}
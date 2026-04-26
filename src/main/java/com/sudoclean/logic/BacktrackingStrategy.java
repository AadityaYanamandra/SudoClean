package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;
import java.util.function.BooleanSupplier;

public class BacktrackingStrategy implements ISolveStrategy {
    @Override
    public boolean solve(Board board, BooleanSupplier shouldStop) {
        // Check at every single recursive entry
        if (shouldStop.getAsBoolean()) return false;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getCell(row, col) == 0) {
                    for (int n = 1; n <= 9; n++) {
                        // Check inside the number loop for immediate response
                        if (shouldStop.getAsBoolean()) return false;

                        if (board.isValid(row, col, n)) {
                            board.setCell(row, col, n);
                            try { 
                                Thread.sleep(20); 
                            } catch (InterruptedException e) { 
                                // Standard practice: stop if interrupted
                                return false; 
                            }

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
}
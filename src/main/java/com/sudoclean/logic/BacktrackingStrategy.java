package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;

public class BacktrackingStrategy implements ISolveStrategy {

    @Override
    public boolean solve(Board board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                
                // Find an empty cell (represented by 0)
                if (board.getCell(row, col) == 0) {
                    for (int number = 1; number <= 9; number++) {
                        
                        if (board.isValid(row, col, number)) {
                            board.setCell(row, col, number);

                            // Recursively try to solve the rest of the board
                            if (solve(board)) {
                                return true;
                            }

                            // If it didn't work, backtrack 
                            board.setCell(row, col, 0);
                        }
                    }
                    return false; // No number 1-9 worked here
                }
            }
        }
        return true; // Board is full and valid
    }
}
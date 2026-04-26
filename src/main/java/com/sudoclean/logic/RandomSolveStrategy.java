package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier; // Crucial for OO stop logic
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomSolveStrategy implements ISolveStrategy {

    @Override
    public boolean solve(Board board, BooleanSupplier shouldStop) {
        // 1. Check the functional stop signal before proceeding
        if (shouldStop.getAsBoolean()) {
            return false;
        }

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                if (board.getCell(row, col) == 0) {
                    
                    // Generate numbers 1-9 and shuffle them for the "Random" strategy
                    List<Integer> numbers = IntStream.rangeClosed(1, 9)
                            .boxed()
                            .collect(Collectors.toList());
                    Collections.shuffle(numbers); 
                    
                    for (int number : numbers) {
                        if (board.isValid(row, col, number)) {
                            board.setCell(row, col, number);
                            
                            // Functional delay for UI visualization
                            try {
                                Thread.sleep(25); 
                            } catch (InterruptedException e) {
                                // Restore interrupt status if needed, but return false to exit
                                Thread.currentThread().interrupt();
                                return false; 
                            }

                            // 2. Recursive call passing the stop signal down
                            if (solve(board, shouldStop)) {
                                return true;
                            }

                            // 3. OO Stop: If stop was requested, DO NOT clear the cell!
                            // This leaves the progress visible on the board.
                            if (shouldStop.getAsBoolean()) {
                                return false;
                            }
                            
                            board.setCell(row, col, 0);
                        }
                    }
                    return false; // Standard backtracking
                }
            }
        }
        return true; // Successfully solved
    }
}
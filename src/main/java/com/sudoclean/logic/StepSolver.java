package com.sudoclean.logic;

import com.sudoclean.model.Board;
import java.util.Stack; // Added missing import

public class StepSolver {
    private final Board board;
    private final Stack<BoardState> stack = new Stack<>();

    // helper class MUST be inside StepSolver or in its own file
    private static class BoardState {
        int row, col, nextVal;
        BoardState(int r, int c, int v) { this.row = r; this.col = c; this.nextVal = v; }
    }

    public StepSolver(Board board) {
        this.board = board;
        pushNextEmpty();
    }

    private void pushNextEmpty() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.getCell(r, c) == 0) {
                    // before pushing, verify if ANY numbers are even possible here
                    boolean possible = false;
                    for (int v = 1; v <= 9; v++) {
                        if (board.isValid(r, c, v)) {
                            possible = true;
                            break;
                        }
                    }
                    
                    // if this cell is empty but NO numbers are valid, the solver has hit a dead end and need to backtrack
                    stack.push(new BoardState(r, c, 1));
                    return;
                }
            }
        }
    }

    public boolean step() {
        if (stack.isEmpty()) {
            pushNextEmpty();
            if (stack.isEmpty()) return true; 
        }

        BoardState current = stack.peek();
        
        for (int v = current.nextVal; v <= 9; v++) {
            if (board.isValid(current.row, current.col, v)) {
                board.setCell(current.row, current.col, v);
                current.nextVal = v + 1;
                
                int oldSize = stack.size();
                pushNextEmpty();
                
                if (stack.size() > oldSize) return false; 
                return true; 
            }
        }

        // Backtrack
        board.setCell(current.row, current.col, 0);
        stack.pop();

        // IF THE STACK IS NOW EMPTY, the board was impossible to begin with (shouldn't ever happen)!
        if (stack.isEmpty()) {
            System.out.println("CLI: ERROR - Board state is unsolvable. Resetting.");
            return true; // stop the timeline
        }
        
        return false; 
    }
}
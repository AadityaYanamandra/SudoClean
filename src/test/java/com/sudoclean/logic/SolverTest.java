package com.sudoclean.logic;

import com.sudoclean.model.Board;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SolverTest {
    @Test
    public void testBacktrackingSolver() {
        Board board = new Board();
        // Set up a very simple puzzle (top left empty, others filled)
        // Or just let it solve an empty board
        BacktrackingStrategy strategy = new BacktrackingStrategy();
        
        boolean solved = board.attemptSolve(strategy);
        
        assertTrue(solved, "Solver should be able to solve an empty board");
        assertNotEquals(0, board.getCell(0, 0), "Cell should be filled after solving");
    }
}
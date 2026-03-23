package com.sudoclean.model;

import com.sudoclean.commands.PlaceNumberCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testValidMove() {
        Board board = new Board();
        // 5 is valid on an empty board
        assertTrue(board.isValid(0, 0, 5));
    }

    @Test
    public void testInvalidMoveRowConflict() {
        Board board = new Board();
        board.setCell(0, 0, 5);
        // Same row, different column
        assertFalse(board.isValid(0, 8, 5), "Should fail due to row conflict");
    }

    @Test
    public void testInvalidMoveColumnConflict() {
        Board board = new Board();
        board.setCell(0, 0, 5);
        // Same column, different row
        assertFalse(board.isValid(8, 0, 5), "Should fail due to column conflict");
    }

    @Test
    public void testInvalidMoveSubgridConflict() {
        Board board = new Board();
        board.setCell(0, 0, 5);
        // Position (1,1) is in the same 3x3 box as (0,0)
        assertFalse(board.isValid(1, 1, 5), "Should fail due to 3x3 subgrid conflict");
    }

    @Test
    public void testCommandExecution() {
        Board board = new Board();
        PlaceNumberCommand cmd = new PlaceNumberCommand(board, 1, 1, 9);
        cmd.execute();
        assertEquals(9, board.getCell(1, 1));
    }

    @Test
    public void testUndoCommand() {
        Board board = new Board();
        // Store current value (0)
        PlaceNumberCommand cmd = new PlaceNumberCommand(board, 2, 2, 7);
        
        cmd.execute();
        assertEquals(7, board.getCell(2, 2));
        
        cmd.undo();
        assertEquals(0, board.getCell(2, 2), "Board should revert to 0");
    }
}
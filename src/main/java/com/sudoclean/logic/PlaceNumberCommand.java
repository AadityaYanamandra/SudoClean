package com.sudoclean.commands;

import com.sudoclean.core.ICommand;
import com.sudoclean.model.Board;

public class PlaceNumberCommand implements ICommand {
    private final Board board;
    private final int row, col, newValue, oldValue;

    public PlaceNumberCommand(Board board, int row, int col, int value) {
        this.board = board;
        this.row = row;
        this.col = col;
        this.newValue = value;
        this.oldValue = board.getCell(row, col); // Store the old state
    }

    @Override
    public void execute() {
        board.setCell(row, col, newValue);
    }

    @Override
    public void undo() {
        board.setCell(row, col, oldValue);
    }
}
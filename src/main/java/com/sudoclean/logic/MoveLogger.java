package com.sudoclean.logic;

import com.sudoclean.core.IBoardObserver;

public class MoveLogger implements IBoardObserver {
    @Override
    public void onCellChanged(int row, int col, int value) {
        if (value != 0) {
            System.out.printf("[LOG] Value %d placed at (%d, %d)%n", value, row, col);
        } else {
            System.out.printf("[LOG] Cell (%d, %d) was cleared%n", row, col);
        }
    }
}
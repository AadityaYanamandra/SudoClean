package com.sudoclean.core;

public interface IBoardObserver {
    void onCellChanged(int row, int col, int value);
}
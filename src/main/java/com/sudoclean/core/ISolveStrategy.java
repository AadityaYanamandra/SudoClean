package com.sudoclean.core;

import com.sudoclean.model.Board;
import java.util.function.BooleanSupplier;

public interface ISolveStrategy {
    //pass a 'shouldStop' check into the solve method
    boolean solve(Board board, BooleanSupplier shouldStop);
}
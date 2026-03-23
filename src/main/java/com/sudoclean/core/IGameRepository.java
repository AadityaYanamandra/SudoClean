package com.sudoclean.core;

import com.sudoclean.model.Board;
import java.io.IOException;

public interface IGameRepository {
    void save(Board board, String fileName) throws IOException;
    Board load(String fileName) throws IOException;
}
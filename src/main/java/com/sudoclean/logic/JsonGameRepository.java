package com.sudoclean.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sudoclean.core.IGameRepository;
import com.sudoclean.model.Board;
import java.io.*;

public class JsonGameRepository implements IGameRepository {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void save(Board board, String fileName) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(board, writer);
        }
    }

    @Override
    public Board load(String fileName) throws IOException {
        try (Reader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, Board.class);
        }
    }
}
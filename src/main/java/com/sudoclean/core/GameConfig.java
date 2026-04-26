package com.sudoclean.core;

public class GameConfig {
    private static GameConfig instance;
    private int emptyCells = 40; // Default: Medium
    private String theme = "Light";

    private GameConfig() {}

    public static GameConfig getInstance() {
        if (instance == null) instance = new GameConfig();
        return instance;
    }

    // Settable on run or via UI
    public void setDifficulty(String level) {
        switch (level.toLowerCase()) {
            case "easy" -> emptyCells = 20;
            case "hard" -> emptyCells = 60;
            default -> emptyCells = 40;
        }
    }

    public int getEmptyCells() { return emptyCells; }
}
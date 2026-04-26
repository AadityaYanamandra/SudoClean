package com.sudoclean;

import com.sudoclean.core.GameConfig;
import com.sudoclean.ui.SudokuUI;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // configure the Singleton based on input
        GameConfig config = GameConfig.getInstance();
        if (args.length > 0) {
            config.setDifficulty(args[0]);
            System.out.println("Starting SudoClean with difficulty: " + args[0]);
        }

        // 2. Launch the UI
        Application.launch(SudokuUI.class, args);
    }
}
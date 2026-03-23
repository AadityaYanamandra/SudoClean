package com.sudoclean.ui;

import com.sudoclean.model.Board;
import com.sudoclean.logic.BacktrackingStrategy;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuUI extends Application {
    private Board board = new Board();
    private TextField[][] cells = new TextField[9][9];

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        
        // Build the 9x9 Grid
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                TextField tf = new TextField();
                tf.setPrefWidth(40);
                cells[r][c] = tf;
                grid.add(tf, c, r);
            }
        }

        Button solveBtn = new Button("Solve Polymorphically");
        solveBtn.setOnAction(e -> {
            updateBoardFromUI();
            board.attemptSolve(new BacktrackingStrategy()); // Dependency Injection
            updateUIFromBoard();
        });

        VBox root = new VBox(10, grid, solveBtn);
        primaryStage.setScene(new Scene(root, 400, 450));
        primaryStage.setTitle("SudoClean Architect");
        primaryStage.show();
    }

    private void updateBoardFromUI() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String text = cells[r][c].getText();
                if (!text.isEmpty()) board.setCell(r, c, Integer.parseInt(text));
            }
        }
    }

    private void updateUIFromBoard() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setText(String.valueOf(board.getCell(r, c)));
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
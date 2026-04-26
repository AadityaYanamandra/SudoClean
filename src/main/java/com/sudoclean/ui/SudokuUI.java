package com.sudoclean.ui;

import com.sudoclean.core.*;
import com.sudoclean.logic.*;
import com.sudoclean.model.Board;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SudokuUI extends Application implements IBoardObserver {
    private Board board = new Board();
    private TextField[][] cells = new TextField[9][9];
    private Thread solverThread;

    private ComboBox<String> strategyBox;
    private Button solveBtn;

    private SudokuSolverManager solverManager = new SudokuSolverManager();

    @Override
    public void start(Stage stage) {
        board.addObserver(this);
        GridPane grid = new GridPane();
        
        // build 9x9 Grid and link to Board
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c] = new TextField();
                cells[r][c].setPrefWidth(40);
                grid.add(cells[r][c], c, r);
                
                // allow manual user input to update the model
                final int row = r;
                final int col = c;
                cells[r][c].textProperty().addListener((obs, oldVal, newVal) -> {
                    if (!newVal.isEmpty() && newVal.matches("[1-9]")) {
                        board.setCell(row, col, Integer.parseInt(newVal));
                    }
                });
            }
        }

        // initialize board based on difficulty Singleton
        board.generateNewGame(GameConfig.getInstance().getEmptyCells());

        // strategy choice dropdown
        strategyBox = new ComboBox<>();
        strategyBox.getItems().addAll("Backtracking", "Random");
        strategyBox.setValue("Backtracking");

        // controls
        solveBtn = new Button("Auto-Solve");
        Button stopBtn = new Button("Exit Program");
        Button checkBtn = new Button("Verify & Submit");

        // button actions
        solveBtn.setOnAction(e -> handleSolveClick());

        stopBtn.setOnAction(e -> {
            if (solverThread != null) solverThread.interrupt();
            Platform.exit();
            System.exit(0);
        });

        checkBtn.setOnAction(e -> {
            if (isCompletelySolved()) {
                System.out.println("CLI: SUCCESS! Sudoku solved correctly.");
                Platform.exit();
            } else {
                new Alert(Alert.AlertType.WARNING, "The board is incomplete or incorrect!").show();
            }
        });

        HBox controls = new HBox(10, strategyBox, solveBtn, stopBtn, checkBtn);
        VBox layout = new VBox(10, grid, controls);
        stage.setScene(new Scene(layout));
        stage.setTitle("SudoClean Architect");
        stage.show();
    }

    private boolean isCompletelySolved() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int val = board.getCell(r, c);
                
                // 1. Check if empty
                if (val == 0) return false;

                // 2. Check if valid in current context
                // Temporarily lift the value to see if it COULD be placed there
                // based on the rest of the board.
                board.setCell(r, c, 0); 
                boolean isValid = board.isValid(r, c, val);
                board.setCell(r, c, val); // Put it back
                
                if (!isValid) return false;
            }
        }
        return true;
    }

    @Override
    public void onCellChanged(int r, int c, int v) {
        Platform.runLater(() -> cells[r][c].setText(v == 0 ? "" : String.valueOf(v)));
    }

    private void toggleUI(boolean isRunning) {
        Platform.runLater(() -> {
            strategyBox.setDisable(isRunning);
            solveBtn.setText(isRunning ? "End Auto-Solve" : "Auto-Solve");
        });
    }

    private void handleSolveClick() {
        // If we are currently solving AND haven't requested a stop yet
        if (solverManager.isRunning()) {
            solverManager.stop();
            // toggleUI(false) will be called automatically by the Manager's thread finally block
            return;
        }

        ISolveStrategy strategy = strategyBox.getValue().equals("Random") ? 
            new RandomSolveStrategy() : new BacktrackingStrategy();

        // Start fresh
        solverManager.startSolve(board, strategy, () -> toggleUI(false));
        toggleUI(true);
    }
}
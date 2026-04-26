package com.sudoclean.logic;

import com.sudoclean.core.ISolveStrategy;
import com.sudoclean.model.Board;
import java.util.concurrent.atomic.AtomicBoolean;

public class SudokuSolverManager {
    private final AtomicBoolean stopRequested = new AtomicBoolean(false);
    private Thread solverThread = null;

    public synchronized void startSolve(Board board, ISolveStrategy strategy, Runnable onFinished) {
        // 1. Force kill any existing reference before starting a new one
        if (solverThread != null) {
            stopRequested.set(true);
            solverThread.interrupt();
        }

        // 2. Reset flag IMMEDIATELY for the new thread
        stopRequested.set(false);
        
        solverThread = new Thread(() -> {
            try {
                board.attemptSolve(strategy, stopRequested::get);
            } finally {
                // When finished or stopped, reset the internal reference
                synchronized(this) {
                    solverThread = null;
                }
                if (onFinished != null) onFinished.run();
            }
        });
        
        solverThread.setDaemon(true);
        solverThread.start();
    }

    public synchronized void stop() {
        stopRequested.set(true);
        if (solverThread != null) {
            solverThread.interrupt(); 
        }
    }

    // This is the source of truth for the UI
    public synchronized boolean isRunning() {
        return solverThread != null && !stopRequested.get();
    }
}
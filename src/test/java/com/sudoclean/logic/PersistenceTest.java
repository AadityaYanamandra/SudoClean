package com.sudoclean.logic;

import com.sudoclean.model.Board;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class PersistenceTest {

    @Test
    public void testSaveAndLoad() throws IOException {
        Board originalBoard = new Board();
        originalBoard.setCell(0, 0, 9);
        
        JsonGameRepository repo = new JsonGameRepository();
        String testFile = "test_save.json";
        
        // Save it
        repo.save(originalBoard, testFile);
        
        // Load it
        Board loadedBoard = repo.load(testFile);
        
        assertEquals(9, loadedBoard.getCell(0, 0), "Loaded board should match saved state");
        
        // Cleanup
        new File(testFile).delete();
    }
}
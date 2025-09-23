package com.memorygame.tests;

import com.memorygame.io.Scoreboard;
import com.memorygame.model.Difficulty;
import com.memorygame.model.Score;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndReadScores() throws IOException {
        Path scoresFile = tempDir.resolve("test_scores.csv");
        Scoreboard scoreboard = new Scoreboard(scoresFile);

        LocalDateTime now = LocalDateTime.now();

        List<Score> scoresToSave = List.of(
                new Score("playerA", 100, now, Difficulty.EASY, 15000L),
                new Score("playerB", 250, now.plusMinutes(1), Difficulty.MEDIUM, 25000L),
                new Score("playerC", 100, now.plusMinutes(2), Difficulty.EASY, 12000L) // Same score as A, but faster
        );

        scoreboard.saveScores(scoresToSave);

        assertTrue(Files.exists(scoresFile));

        List<String> lines = Files.readAllLines(scoresFile);
        assertEquals(4, lines.size()); // Header + 3 scores
        assertTrue(lines.get(1).contains("playerA"));

        List<Score> readScores = scoreboard.readScores();

        assertNotNull(readScores);
        assertEquals(3, readScores.size());

        // Scores should be sorted: playerB (250), playerC (100, 12s), playerA (100, 15s)
        assertEquals("playerB", readScores.get(0).username());
        assertEquals(250, readScores.get(0).score());

        assertEquals("playerC", readScores.get(1).username());
        assertEquals(100, readScores.get(1).score());
        assertEquals(12000L, readScores.get(1).timeMillis());

        assertEquals("playerA", readScores.get(2).username());
        assertEquals(100, readScores.get(2).score());
        assertEquals(15000L, readScores.get(2).timeMillis());
    }

    @Test
    void testAddScore() {
        Path scoresFile = tempDir.resolve("add_score_test.csv");
        Scoreboard scoreboard = new Scoreboard(scoresFile);

        Score initialScore = new Score("initial", 50, LocalDateTime.now(), Difficulty.EASY, 10000L);
        scoreboard.addScore(initialScore);

        Score newScore = new Score("new_player", 500, LocalDateTime.now(), Difficulty.HARD, 5000L);
        scoreboard.addScore(newScore);

        List<Score> allScores = scoreboard.readScores();
        assertEquals(2, allScores.size());
        assertEquals("new_player", allScores.get(0).username()); // new_player should be first due to higher score
        assertEquals("initial", allScores.get(1).username());
    }

    @Test
    void testReadFromNonExistentFile() {
        Path scoresFile = tempDir.resolve("non_existent.csv");
        // Don't create the file, just the path
        Scoreboard scoreboard = new Scoreboard(scoresFile);
        // The constructor now creates the file, so we need to delete it for this test
        try {
            Files.deleteIfExists(scoresFile);
        } catch (IOException e) {
            fail("Could not delete file for test setup");
        }

        List<Score> scores = scoreboard.readScores();
        assertNotNull(scores);
        assertTrue(scores.isEmpty());
    }
}

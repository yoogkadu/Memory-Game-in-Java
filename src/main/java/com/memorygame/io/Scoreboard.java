package com.memorygame.io;

import com.memorygame.model.Difficulty;
import com.memorygame.model.Score;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Scoreboard {

    private static final String SCORES_DIR_NAME = ".memorygame";
    private static final String SCORES_FILE_NAME = "scores.csv";
    private static final Path DEFAULT_SCORES_PATH = Paths.get(System.getProperty("user.home"), SCORES_DIR_NAME);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final String CSV_HEADER = "username,score,timestamp,level,timeMillis";

    private final Path scoresFilePath;

    public Scoreboard() {
        this(DEFAULT_SCORES_PATH.resolve(SCORES_FILE_NAME));
    }

    // Constructor for testing
    public Scoreboard(Path scoresFilePath) {
        this.scoresFilePath = scoresFilePath;
        try {
            Path parentDir = scoresFilePath.getParent();
            if (parentDir != null && Files.notExists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            if (Files.notExists(this.scoresFilePath)) {
                Files.createFile(this.scoresFilePath);
                try (BufferedWriter writer = Files.newBufferedWriter(this.scoresFilePath, StandardOpenOption.WRITE)) {
                    writer.write(CSV_HEADER);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing scoreboard: " + e.getMessage());
        }
    }

    public List<Score> readScores() {
        List<Score> scores = new ArrayList<>();
        if (Files.notExists(scoresFilePath)) {
            return scores;
        }

        try (BufferedReader reader = Files.newBufferedReader(scoresFilePath)) {
            // Skip header
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    try {
                        String username = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        LocalDateTime timestamp = LocalDateTime.parse(parts[2], DATE_TIME_FORMATTER);
                        Difficulty level = Difficulty.valueOf(parts[3]);
                        long timeMillis = Long.parseLong(parts[4]);
                        scores.add(new Score(username, score, timestamp, level, timeMillis));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed score line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading scores from " + scoresFilePath + ": " + e.getMessage());
        }

        // Sort scores: highest score first, then by fastest time
        scores.sort(Comparator.comparing(Score::score).reversed().thenComparing(Score::timeMillis));

        return scores;
    }

    public void saveScores(List<Score> scores) {
        try (BufferedWriter writer = Files.newBufferedWriter(scoresFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            writer.write(CSV_HEADER);
            writer.newLine();
            for (Score score : scores) {
                String line = String.join(",",
                        score.username(),
                        String.valueOf(score.score()),
                        score.timestamp().format(DATE_TIME_FORMATTER),
                        score.level().name(),
                        String.valueOf(score.timeMillis())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving scores to " + scoresFilePath + ": " + e.getMessage());
        }
    }

    public void addScore(Score newScore) {
        List<Score> scores = readScores();
        scores.add(newScore);
        saveScores(scores);
    }
}

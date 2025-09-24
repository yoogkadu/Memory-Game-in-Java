package com.memorygame.model;

import java.util.List;

public class GameModel {

    private Difficulty difficulty;
    private String username;
    private int currentScore;
    private int currentLevel;
    private List<Integer> currentSequence;
    private long gameStartTime;

    public GameModel(String username, Difficulty difficulty) {
        this.username = username;
        this.difficulty = difficulty;
        this.currentScore = 0;
        this.currentLevel = 1;
        this.gameStartTime = System.currentTimeMillis();
    }

    public void startNewLevel() {
        this.currentSequence = SequenceGenerator.generateSequence(difficulty);
    }

    public int calculateScore(List<Integer> userSequence) {
        if (currentSequence == null || userSequence == null) {
            return 0;
        }
        int correctMatches = 0;
        for (int i = 0; i < currentSequence.size(); i++) {
            if (i < userSequence.size() && currentSequence.get(i).equals(userSequence.get(i))) {
                correctMatches++;
            }
        }

        // Full match: 100 points per number. Partial match: 25 points per number.
        if (correctMatches == currentSequence.size()) {
            return correctMatches * 100;
        } else {
            return correctMatches * 25;
        }
    }

    public void addToScore(int points) {
        this.currentScore += points;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getUsername() {
        return username;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void incrementLevel() {
        this.currentLevel++;
    }

    public List<Integer> getCurrentSequence() {
        return currentSequence;
    }

    public long getTotalGameTimeMillis() {
        return System.currentTimeMillis() - gameStartTime;
    }
}

package com.memorygame.controller;

import com.memorygame.io.Scoreboard;
import com.memorygame.model.Difficulty;
import com.memorygame.model.GameModel;
import com.memorygame.ui.MainFrame;

import javax.swing.Timer;
import javax.swing.JOptionPane;

public class GameController {

    private MainFrame mainFrame;
    private GameModel gameModel;

    public GameController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void startGame(String username, Difficulty difficulty) {
        this.gameModel = new GameModel(username, difficulty);
        mainFrame.showGamePanel();
        startNextLevel();
    }

    public void startNextLevel() {
        gameModel.startNewLevel();
        mainFrame.getGamePanel().showSequence(gameModel.getCurrentSequence());
        com.memorygame.util.SoundUtils.playSound("reveal.wav");

        // Use javax.swing.Timer for Swing-safe timer operations
        javax.swing.Timer timer = new javax.swing.Timer(gameModel.getDifficulty().getDisplayTimeMillis(), e -> {
            mainFrame.getGamePanel().hideSequence();
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start();
    }

    public void submitGuess(java.util.List<Integer> guess) {
        int points = gameModel.calculateScore(guess);
        gameModel.addToScore(points);
        System.out.println("Round score: " + points + ", Total score: " + gameModel.getCurrentScore());

        boolean isFullyCorrect = (points == gameModel.getCurrentSequence().size() * 100);
        mainFrame.getGamePanel().triggerFeedbackAnimation(isFullyCorrect);

        if (isFullyCorrect) {
            com.memorygame.util.SoundUtils.playSound("correct.wav");
        } else {
            com.memorygame.util.SoundUtils.playSound("incorrect.wav");
        }

        // A short delay after the feedback animation before starting the next level
        Timer nextLevelTimer = new Timer(500, e -> {
            if (gameModel.getCurrentLevel() < 5) { // 5 sequences per level
                gameModel.incrementLevel();
                startNextLevel();
            } else {
                endGame();
            }
        });
        nextLevelTimer.setRepeats(false);
        nextLevelTimer.start();
    }

    private void endGame() {
        System.out.println("Game Over! Final Score: " + gameModel.getCurrentScore());
        // Save score
        Scoreboard scoreboard = new Scoreboard();
        scoreboard.addScore(new com.memorygame.model.Score(
            gameModel.getUsername(),
            gameModel.getCurrentScore(),
            java.time.LocalDateTime.now(),
            gameModel.getDifficulty(),
            gameModel.getTotalGameTimeMillis()
        ));

        JOptionPane.showMessageDialog(mainFrame, "Game Over!\nYour final score is: " + gameModel.getCurrentScore(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
        showLeaderboard();
    }

    public void showLeaderboard() {
        mainFrame.showLeaderboardPanel();
    }

    public void showStartMenu() {
        mainFrame.showStartPanel();
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}

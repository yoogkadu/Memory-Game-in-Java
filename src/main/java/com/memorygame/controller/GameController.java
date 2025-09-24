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
    private Timer inputTimer;
    private int timeLeft;

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

        Timer displayTimer = new Timer(gameModel.getDifficulty().getDisplayTimeMillis(), e -> beginInputPhase());
        displayTimer.setRepeats(false);
        displayTimer.start();
    }

    private void beginInputPhase() {
        mainFrame.getGamePanel().hideSequence();

        timeLeft = gameModel.getDifficulty().getInputTimeMillis() / 1000;
        mainFrame.getGamePanel().updateTimer(timeLeft);

        inputTimer = new Timer(1000, e -> {
            timeLeft--;
            mainFrame.getGamePanel().updateTimer(timeLeft);
            if (timeLeft <= 0) {
                inputTimer.stop();
                endGame();
            }
        });
        inputTimer.start();
    }

    public void submitGuess(java.util.List<Integer> guess) {
        if (inputTimer != null) {
            inputTimer.stop();
        }

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

        // Create the new score object
        com.memorygame.model.Score newScore = new com.memorygame.model.Score(
                gameModel.getUsername(),
                gameModel.getCurrentScore(),
                java.time.LocalDateTime.now(),
                gameModel.getDifficulty(),
                gameModel.getTotalGameTimeMillis()
        );

        // Determine rank before saving
        Scoreboard scoreboard = new Scoreboard();
        java.util.List<com.memorygame.model.Score> scores = scoreboard.readScores();
        scores.add(newScore);
        scores.sort(java.util.Comparator.comparing(com.memorygame.model.Score::score).reversed().thenComparing(com.memorygame.model.Score::timeMillis));
        int rank = scores.indexOf(newScore) + 1;

        // Save the score
        scoreboard.addScore(newScore);

        String message = String.format("Game Over!\n\nYour Score: %d\nYour Rank: %d", gameModel.getCurrentScore(), rank);
        JOptionPane.showMessageDialog(mainFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        showLeaderboard();
    }

    public void showLeaderboard() {
        mainFrame.showLeaderboardPanel();
    }

    public void showStartMenu() {
        mainFrame.getStartPanel().updateLeaderboardPreview();
        mainFrame.showStartPanel();
    }

    public void showInstructions() {
        String instructions = """
                <html>
                <h1>How to Play Memory Game</h1>
                <p>Welcome to the Memory Game! The rules are simple:</p>
                <ol>
                    <li>Enter your username and select a difficulty.</li>
                    <li>Click <b>Start Game</b>.</li>
                    <li>A sequence of numbers will be briefly shown on the screen. Memorize them!</li>
                    <li>After the numbers disappear, a timer will start.</li>
                    <li>Type the sequence you remember into the input box, with each number separated by a space.</li>
                    <li>Press <b>Enter</b> to submit your answer before the time runs out.</li>
                </ol>
                <p><b>Scoring:</b></p>
                <ul>
                    <li><b>100 points</b> for each correct number in the correct position (if the whole sequence is right).</li>
                    <li><b>25 points</b> for each correct number in the correct position (if you make any mistakes).</li>
                </ul>
                <p>The game consists of 5 rounds. Good luck!</p>
                </html>
                """;
        JOptionPane.showMessageDialog(mainFrame, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    public GameModel getGameModel() {
        return gameModel;
    }
}

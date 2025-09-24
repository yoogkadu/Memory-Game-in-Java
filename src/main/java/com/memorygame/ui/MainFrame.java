package com.memorygame.ui;

import com.memorygame.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private StartPanel startPanel;
    private GamePanel gamePanel;
    private LeaderboardPanel leaderboardPanel;
    private GameController controller;

    public MainFrame() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels will be initialized after controller is set

        add(mainPanel);
    }

    public void setController(GameController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        startPanel = new StartPanel(controller);
        gamePanel = new GamePanel(controller);
        leaderboardPanel = new LeaderboardPanel(controller);

        mainPanel.add(startPanel, "START");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(leaderboardPanel, "LEADERBOARD");

        add(mainPanel);

        cardLayout.show(mainPanel, "START"); // Show start panel first
    }

    // Methods to switch panels will be added here later
    public void showStartPanel() {
        cardLayout.show(mainPanel, "START");
    }

    public void showGamePanel() {
        cardLayout.show(mainPanel, "GAME");
    }

    public void showLeaderboardPanel() {
        leaderboardPanel.refreshScores();
        cardLayout.show(mainPanel, "LEADERBOARD");
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public StartPanel getStartPanel() {
        return startPanel;
    }
}

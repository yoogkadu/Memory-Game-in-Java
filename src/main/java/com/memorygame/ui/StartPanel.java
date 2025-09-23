package com.memorygame.ui;

import com.memorygame.controller.GameController;
import com.memorygame.model.Difficulty;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {

    private GameController controller;
    private JTextField usernameField;
    private JComboBox<Difficulty> difficultyComboBox;

    public StartPanel(GameController controller) {
        this.controller = controller;

        // Main panel with a border
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        setLayout(new GridBagLayout());
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Memory Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        mainPanel.add(titleLabel, gbc);

        // Username
        mainPanel.add(new JLabel("Username:"), gbc);
        usernameField = new JTextField("Player1", 20);
        mainPanel.add(usernameField, gbc);

        // Difficulty
        mainPanel.add(new JLabel("Difficulty:"), gbc);
        difficultyComboBox = new JComboBox<>(Difficulty.values());
        mainPanel.add(difficultyComboBox, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.", "Username Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.startGame(username, (Difficulty) difficultyComboBox.getSelectedItem());
        });

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.addActionListener(e -> controller.showLeaderboard());

        JToggleButton soundButton = new JToggleButton("Sound ON", true);
        soundButton.addActionListener(e -> {
            boolean isSelected = soundButton.isSelected();
            soundButton.setText(isSelected ? "Sound ON" : "Sound OFF");
            com.memorygame.util.SoundUtils.setSoundEnabled(isSelected);
        });

        buttonPanel.add(startButton);
        buttonPanel.add(leaderboardButton);
        buttonPanel.add(soundButton);
        mainPanel.add(buttonPanel, gbc);
    }
}

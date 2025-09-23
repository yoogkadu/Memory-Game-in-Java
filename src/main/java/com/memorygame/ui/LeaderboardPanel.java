package com.memorygame.ui;

import com.memorygame.controller.GameController;
import com.memorygame.io.Scoreboard;
import com.memorygame.model.Score;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeaderboardPanel extends JPanel {

    private GameController controller;
    private Scoreboard scoreboard;
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    public LeaderboardPanel(GameController controller) {
        this.controller = controller;
        this.scoreboard = new Scoreboard();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 38));
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = {"Rank", "Username", "Score", "Difficulty", "Time (sec)", "Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        scoreTable = new JTable(tableModel);
        scoreTable.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreTable.setRowHeight(25);
        scoreTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> controller.showStartMenu());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshScores() {
        List<Score> scores = scoreboard.readScores();
        tableModel.setRowCount(0); // Clear existing data
        int rank = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Score score : scores) {
            Object[] rowData = {
                    rank++,
                    score.username(),
                    score.score(),
                    score.level(),
                    String.format("%.2f", score.timeMillis() / 1000.0),
                    score.timestamp().format(formatter)
            };
            tableModel.addRow(rowData);
        }
    }
}

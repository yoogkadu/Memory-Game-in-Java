package com.memorygame.ui;

import com.memorygame.controller.GameController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    private GameController controller;
    private List<Integer> currentSequence;
    private boolean showingSequence = false;

    // Animation
    private Timer animationTimer;
    private long animationStartTime;
    private final int ANIMATION_DURATION_MS = 300;

    private enum FeedbackState { NONE, CORRECT, INCORRECT }
    private FeedbackState feedbackState = FeedbackState.NONE;
    private long feedbackAnimationStart;
    private final int FEEDBACK_ANIMATION_DURATION_MS = 400;

    private JPanel sequenceDisplayPanel;
    private JPanel inputPanel;
    private JTextField inputField;
    private JLabel timerLabel;

    public GamePanel(GameController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        // Panel to display the sequence numbers (using custom painting)
        sequenceDisplayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                GamePanel.this.paintSequence(g);
            }
        };
        sequenceDisplayPanel.setOpaque(false);
        add(sequenceDisplayPanel, BorderLayout.CENTER);

        // Panel for user input fields
        inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        inputPanel.setOpaque(false);
        add(inputPanel, BorderLayout.SOUTH);

        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.BOLD, 24));
        inputField.setHorizontalAlignment(JTextField.CENTER);

        timerLabel = new JLabel("Time: --", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(timerLabel, BorderLayout.NORTH);

        setupKeyBindings();
    }

    public void updateTimer(int secondsLeft) {
        timerLabel.setText(String.format("Time Left: %d", secondsLeft));
    }

    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("ENTER"), "submit");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "escape");

        am.put("submit", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (inputField.isVisible()) {
                    handleSubmit();
                }
            }
        });

        am.put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                controller.showStartMenu();
            }
        });
    }

    private void handleSubmit() {
        String[] numbers = inputField.getText().trim().split("\\s+");
        java.util.List<Integer> userGuess = new java.util.ArrayList<>();

        if (numbers.length == 1 && numbers[0].isEmpty()) {
            // Handle case where input is empty
            controller.submitGuess(userGuess); // Submit an empty list
            return;
        }

        try {
            for (String numStr : numbers) {
                userGuess.add(Integer.parseInt(numStr));
            }
            controller.submitGuess(userGuess);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid, space-separated numbers.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void showSequence(List<Integer> sequence) {
        this.currentSequence = sequence;
        this.showingSequence = true;
        this.animationStartTime = System.currentTimeMillis();

        inputPanel.setVisible(false);
        sequenceDisplayPanel.setVisible(true);

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        // Timer to drive the animation frames
        animationTimer = new Timer(1000 / 60, e -> {
            long elapsed = System.currentTimeMillis() - animationStartTime;
            if (elapsed >= ANIMATION_DURATION_MS) {
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        animationTimer.start();
    }

    public void hideSequence() {
        this.showingSequence = false;
        sequenceDisplayPanel.setVisible(false);
        setupInputFields();
        inputPanel.setVisible(true);
        revalidate();
        repaint();
    }

    private void setupInputFields() {
        inputPanel.removeAll();
        inputField.setText(""); // Clear previous input
        inputPanel.add(new JLabel("Enter sequence (space-separated):"));
        inputPanel.add(inputField);

        // Request focus after the component is added and the panel is visible
        SwingUtilities.invokeLater(() -> inputField.requestFocusInWindow());
    }

    private void paintSequence(Graphics g) {
        if (showingSequence && currentSequence != null) {
            Graphics2D g2d = (Graphics2D) g.create(); // Create a copy to not affect other painting
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            g2d.setColor(Color.BLACK);

            long elapsed = System.currentTimeMillis() - animationStartTime;
            double progress = Math.min(1.0, (double) elapsed / ANIMATION_DURATION_MS);

            // Ease-out cubic interpolation for a smooth pop
            double easedProgress = 1 - Math.pow(1 - progress, 3);

            // Scale: 0.7 -> 1.05 -> 1.0
            double scale;
            if (easedProgress < 0.7) { // First 70% of animation: scale up to 1.05
                scale = 0.7 + (easedProgress / 0.7) * 0.35;
            } else { // Last 30% of animation: scale down to 1.0
                scale = 1.05 - ((easedProgress - 0.7) / 0.3) * 0.05;
            }

            int panelWidth = getWidth();
            int totalNumbersWidth = currentSequence.size() * 120; // Give more space
            int x = (panelWidth - totalNumbersWidth) / 2 + 60;
            int y = getHeight() / 2;

            for (int number : currentSequence) {
                String numStr = String.valueOf(number);
                FontMetrics fm = g2d.getFontMetrics();
                int numWidth = fm.stringWidth(numStr);

                java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
                g2d.translate(x, y);
                g2d.scale(scale, scale);
                g2d.drawString(numStr, -numWidth / 2, fm.getAscent() / 2);
                g2d.setTransform(oldTransform);

                x += 120;
            }
            g2d.dispose();
        }
    }

    public void triggerFeedbackAnimation(boolean correct) {
        this.feedbackState = correct ? FeedbackState.CORRECT : FeedbackState.INCORRECT;
        this.feedbackAnimationStart = System.currentTimeMillis();
        // This timer will reset the state after the animation, and drive repaint
        Timer feedbackTimer = new Timer(1000 / 60, e -> {
            long elapsed = System.currentTimeMillis() - feedbackAnimationStart;
            if (elapsed >= FEEDBACK_ANIMATION_DURATION_MS) {
                feedbackState = FeedbackState.NONE;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        feedbackTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (feedbackState != FeedbackState.NONE) {
            long elapsed = System.currentTimeMillis() - feedbackAnimationStart;
            double progress = Math.min(1.0, (double) elapsed / FEEDBACK_ANIMATION_DURATION_MS);

            // Fade out effect
            float alpha = (float) (1.0 - progress);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            if (feedbackState == FeedbackState.CORRECT) {
                g2d.setColor(new Color(0, 255, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            } else { // INCORRECT
                g2d.setColor(new Color(255, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Shake effect
                double shakeAmount = 10 * (1 - progress); // Shake decreases over time
                int dx = (int) ((Math.random() - 0.5) * shakeAmount);
                int dy = (int) ((Math.random() - 0.5) * shakeAmount);
                g2d.translate(dx, dy);
            }

            g2d.dispose();
        }
    }
}

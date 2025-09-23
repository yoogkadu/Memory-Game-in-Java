package com.memorygame;

import com.memorygame.controller.GameController;
import com.memorygame.ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            GameController controller = new GameController(mainFrame);
            mainFrame.setController(controller);
            mainFrame.setVisible(true);
        });
    }
}

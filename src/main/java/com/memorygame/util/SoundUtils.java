package com.memorygame.util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundUtils {

    private static boolean soundEnabled = true;

    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }

    public static boolean isSoundEnabled() {
        return soundEnabled;
    }

    public static synchronized void playSound(final String resourceName) {
        if (!soundEnabled) {
            return;
        }

        new Thread(() -> {
            try {
                // Use getResourceAsStream to load from JAR resources
                InputStream audioSrc = SoundUtils.class.getResourceAsStream("/sounds/" + resourceName);
                if (audioSrc == null) {
                    System.err.println("Warning: sound resource not found: " + resourceName);
                    return;
                }

                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn)) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioIn);
                    clip.start();
                }
            } catch (UnsupportedAudioFileException | LineUnavailableException | java.io.IOException e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }
}

package com.memorygame.model;

import java.time.LocalDateTime;

public record Score(String username, int score, LocalDateTime timestamp, Difficulty level, long timeMillis) {
}

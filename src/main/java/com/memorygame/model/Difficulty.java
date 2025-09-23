package com.memorygame.model;

public enum Difficulty {
    EASY(4, 3000, 1),    // 4 numbers, 3 seconds display, 1-digit numbers
    MEDIUM(6, 2000, 2),  // 6 numbers, 2 seconds display, up to 2-digit numbers
    HARD(8, 1500, 3);    // 8 numbers, 1.5 seconds display, up to 3-digit numbers

    private final int sequenceLength;
    private final int displayTimeMillis;
    private final int maxDigits;

    Difficulty(int sequenceLength, int displayTimeMillis, int maxDigits) {
        this.sequenceLength = sequenceLength;
        this.displayTimeMillis = displayTimeMillis;
        this.maxDigits = maxDigits;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public int getDisplayTimeMillis() {
        return displayTimeMillis;
    }

    public int getMaxDigits() {
        return maxDigits;
    }
}

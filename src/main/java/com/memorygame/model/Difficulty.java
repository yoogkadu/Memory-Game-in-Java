package com.memorygame.model;

public enum Difficulty {
    EASY(4, 3000, 1, 10000),    // 10 seconds for input
    MEDIUM(6, 2000, 2, 7000),   // 7 seconds for input
    HARD(8, 1500, 3, 5000);     // 5 seconds for input

    private final int sequenceLength;
    private final int displayTimeMillis;
    private final int maxDigits;
    private final int inputTimeMillis;

    Difficulty(int sequenceLength, int displayTimeMillis, int maxDigits, int inputTimeMillis) {
        this.sequenceLength = sequenceLength;
        this.displayTimeMillis = displayTimeMillis;
        this.maxDigits = maxDigits;
        this.inputTimeMillis = inputTimeMillis;
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

    public int getInputTimeMillis() {
        return inputTimeMillis;
    }
}

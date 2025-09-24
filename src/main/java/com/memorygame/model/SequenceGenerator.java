package com.memorygame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SequenceGenerator {

    public static List<Integer> generateSequence(Difficulty difficulty) {
        List<Integer> sequence = new ArrayList<>();
        Random random = ThreadLocalRandom.current();
        int sequenceLength = difficulty.getSequenceLength();
        int maxDigits = difficulty.getMaxDigits();
        int upperBound = (int) Math.pow(10, maxDigits) - 1;
        int lowerBound = (maxDigits > 1) ? (int) Math.pow(10, maxDigits - 1) : 0;

        for (int i = 0; i < sequenceLength; i++) {
            int number;
            if (upperBound == lowerBound) {
                number = upperBound;
            } else {
                number = random.nextInt(lowerBound, upperBound + 1);
            }
            sequence.add(number);
        }
        return sequence;
    }
}

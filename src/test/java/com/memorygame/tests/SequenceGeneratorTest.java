package com.memorygame.tests;

import com.memorygame.model.Difficulty;
import com.memorygame.model.SequenceGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SequenceGeneratorTest {

    @Test
    void testGenerateEasySequence() {
        Difficulty difficulty = Difficulty.EASY;
        List<Integer> sequence = SequenceGenerator.generateSequence(difficulty);

        assertNotNull(sequence);
        assertEquals(difficulty.getSequenceLength(), sequence.size());

        for (Integer number : sequence) {
            assertTrue(number >= 0 && number <= 9, "EASY numbers should be single-digit");
        }
    }

    @Test
    void testGenerateMediumSequence() {
        Difficulty difficulty = Difficulty.MEDIUM;
        List<Integer> sequence = SequenceGenerator.generateSequence(difficulty);

        assertNotNull(sequence);
        assertEquals(difficulty.getSequenceLength(), sequence.size());

        for (Integer number : sequence) {
            assertTrue(number >= 10 && number <= 99, "MEDIUM numbers should be two-digit");
        }
    }

    @Test
    void testGenerateHardSequence() {
        Difficulty difficulty = Difficulty.HARD;
        List<Integer> sequence = SequenceGenerator.generateSequence(difficulty);

        assertNotNull(sequence);
        assertEquals(difficulty.getSequenceLength(), sequence.size());

        for (Integer number : sequence) {
            assertTrue(number >= 100 && number <= 999, "HARD numbers should be three-digit");
        }
    }

    @Test
    void testSequenceIsRandom() {
        // It's hard to test for "true" randomness, but we can assert that two
        // generated sequences are not identical. The probability of a collision
        // with this simple approach is extremely low.
        List<Integer> sequence1 = SequenceGenerator.generateSequence(Difficulty.HARD);
        List<Integer> sequence2 = SequenceGenerator.generateSequence(Difficulty.HARD);

        assertNotEquals(sequence1, sequence2, "Two generated sequences for the same difficulty should not be identical.");
    }
}

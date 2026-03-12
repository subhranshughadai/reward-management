package com.charter.rewardmanagement.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for RewardCalculator.
 */
class RewardCalculatorTest {

    /**
     * Tests reward calculation for amount above 100.
     */
    @Test
    void testAbove100() {
        int points = RewardCalculator.calculatePoints(120);
        assertEquals(90, points);
    }

    /**
     * Tests reward calculation for amount between 50 and 100.
     */
    @Test
    void testBetween50And100() {
        int points = RewardCalculator.calculatePoints(70);
        assertEquals(20, points);
    }

    /**
     * Tests reward calculation for amount below 50.
     */
    @Test
    void testBelow50() {
        int points = RewardCalculator.calculatePoints(40);
        assertEquals(0, points);
    }

    /**
     * Tests reward calculation for exactly 50.
     */
    @Test
    void testExactly50() {
        int points = RewardCalculator.calculatePoints(50);
        assertEquals(0, points);
    }

    /**
     * Tests reward calculation for exactly 100.
     */
    @Test
    void testExactly100() {
        int points = RewardCalculator.calculatePoints(100);
        assertEquals(50, points);
    }

    /**
     * Tests reward calculation for exactly 51.
     */
    @Test
    void testExactly51() {
        int points = RewardCalculator.calculatePoints(51);
        assertEquals(1, points);
    }

    /**
     * Tests reward calculation for exactly 101.
     */
    @Test
    void testExactly101() {
        int points = RewardCalculator.calculatePoints(101);
        assertEquals(52, points);
    }

    /**
     * Tests reward calculation for zero amount.
     */
    @Test
    void testZeroAmount() {
        int points = RewardCalculator.calculatePoints(0);
        assertEquals(0, points);
    }

    /**
     * Tests reward calculation for negative amount.
     */
    @Test
    void testNegativeAmount() {
        int points = RewardCalculator.calculatePoints(-10);
        assertEquals(0, points);
    }

    /**
     * Tests reward calculation for large amount.
     */
    @Test
    void testLargeAmount() {
        int points = RewardCalculator.calculatePoints(1000);
        assertEquals(1850, points);
    }

    /**
     * Tests reward calculation for decimal amount.
     */
    @Test
    void testDecimalAmount() {
        int points = RewardCalculator.calculatePoints(120.99);
        assertEquals(90, points);
    }
}
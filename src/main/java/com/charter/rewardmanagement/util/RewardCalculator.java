package com.charter.rewardmanagement.util;

/**
 * Utility class for calculating reward points.
 */
public class RewardCalculator {

    private RewardCalculator() {}

    /**
     * Calculates reward points based on transaction amount.
     *
     * @param amount transaction amount
     * @return reward points
     */
    public static int calculatePoints(double amount) {

        if (amount <= 50) return 0;

        if (amount <= 100) {
            return (int) (amount - 50);
        }

        return 50 + (int) ((amount - 100) * 2);
    }
}
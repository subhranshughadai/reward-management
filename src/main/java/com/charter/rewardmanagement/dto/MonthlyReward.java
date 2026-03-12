package com.charter.rewardmanagement.dto;

/**
 * Represents monthly reward points for a customer.
 */
public class MonthlyReward {

    private String month;
    private int points;

    /**
     * Constructor for MonthlyReward.
     *
     * @param month the month of rewards
     * @param points reward points earned in the month
     */
    public MonthlyReward(String month, int points) {
        this.month = month;
        this.points = points;
    }

    public String getMonth() { return month; }
    public int getPoints() { return points; }
}

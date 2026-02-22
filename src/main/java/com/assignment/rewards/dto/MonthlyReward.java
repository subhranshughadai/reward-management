package com.assignment.rewards.dto;

public class MonthlyReward {

    private String month;
    private int points;

    public MonthlyReward(String month, int points) {
        this.month = month;
        this.points = points;
    }

    public String getMonth() { return month; }
    public int getPoints() { return points; }
}

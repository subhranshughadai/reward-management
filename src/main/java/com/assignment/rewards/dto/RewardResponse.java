package com.assignment.rewards.dto;

import java.util.List;

public class RewardResponse {

    private Long customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;

    public RewardResponse(Long customerId,
                          List<MonthlyReward> monthlyRewards,
                          int totalPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalPoints = totalPoints;
    }

    public Long getCustomerId() { return customerId; }
    public List<MonthlyReward> getMonthlyRewards() { return monthlyRewards; }
    public int getTotalPoints() { return totalPoints; }
}

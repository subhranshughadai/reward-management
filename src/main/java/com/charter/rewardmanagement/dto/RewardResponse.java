package com.charter.rewardmanagement.dto;

import java.util.List;

/**
 * Represents reward details for a customer.
 */
public class RewardResponse {

    private Long customerId;
    private List<MonthlyReward> monthlyRewards;
    private int totalPoints;

    /**
     * Constructor for RewardResponse.
     *
     * @param customerId customer identifier
     * @param monthlyRewards list of monthly reward points
     * @param totalPoints total reward points
     */
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
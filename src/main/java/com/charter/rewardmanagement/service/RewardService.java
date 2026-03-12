package com.charter.rewardmanagement.service;

import com.charter.rewardmanagement.dto.RewardResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service interface for reward calculations.
 */
@Service
public interface RewardService {

    /**
     * Gets paginated reward details for customers within a date range.
     *
     * @param start start date
     * @param end end date
     * @param page page number
     * @param size page size
     * @return paginated reward response
     */
    Page<RewardResponse> getAllCustomerRewards(
            LocalDate start,
            LocalDate end,
            int page,
            int size);
}
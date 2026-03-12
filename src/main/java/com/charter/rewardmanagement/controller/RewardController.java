package com.charter.rewardmanagement.controller;

import com.charter.rewardmanagement.dto.RewardResponse;
import com.charter.rewardmanagement.service.RewardService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST Controller for handling reward related APIs.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService rewardService;

    /**
     * Constructor for RewardController.
     *
     * @param rewardService service used to calculate rewards
     */
    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Fetch rewards for customers within a date range.
     *
     * @param start start date
     * @param end end date
     * @param page page number
     * @param size page size
     * @return paginated list of reward responses
     */
    @GetMapping("/customers/getRewards")
    public ResponseEntity<Page<RewardResponse>> getRewards(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<RewardResponse> rewards =
                rewardService.getAllCustomerRewards(start, end, page, size);

        return ResponseEntity.ok(rewards);
    }
}
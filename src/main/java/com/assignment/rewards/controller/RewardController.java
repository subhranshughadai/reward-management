package com.assignment.rewards.controller;

import com.assignment.rewards.dto.RewardResponse;
import com.assignment.rewards.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST Controller for reward APIs.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardService service;

    public RewardController(RewardService service) {
        this.service = service;
    }

    @Operation(summary = "Get rewards for all customers with pagination")
    @GetMapping
    public ResponseEntity<Page<RewardResponse>> getAllRewards(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        if (start.isAfter(end)) {
            return ResponseEntity.badRequest().build();
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<RewardResponse> response =
                service.getAllCustomerRewards(start, end, pageable);

        return ResponseEntity.ok(response);
    }
}
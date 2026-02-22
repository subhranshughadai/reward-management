package com.assignment.rewards.service;

import com.assignment.rewards.dto.*;
import com.assignment.rewards.entity.Transaction;
import com.assignment.rewards.repository.TransactionRepository;
import com.assignment.rewards.util.RewardCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service handling reward calculations.
 */
/**
 * Returns rewards for all customers within date range.
 *
 * @param start start date
 * @param end end date
 * @param pageable pagination info
 * @return paginated reward response
 */

@Service
public class RewardService {

    private final TransactionRepository repository;

    public RewardService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Cacheable("allRewards")
    public Page<RewardResponse> getAllCustomerRewards(
            LocalDate start,
            LocalDate end,
            Pageable pageable) {

        List<Transaction> transactions =
                repository.findByTransactionDateBetween(start, end);

        // Group by customer
        Map<Long, List<Transaction>> customerMap =
                transactions.stream()
                        .collect(Collectors.groupingBy(Transaction::getCustomerId));

        // Build reward response per customer
        List<RewardResponse> responses =
                customerMap.entrySet()
                        .stream()
                        .map(entry ->
                                calculateReward(entry.getKey(), entry.getValue()))
                        .toList();

        // Manual pagination on customers
        int startIndex = (int) pageable.getOffset();
        int endIndex =
                Math.min(startIndex + pageable.getPageSize(), responses.size());

        List<RewardResponse> paginated =
                responses.subList(startIndex, endIndex);

        return new PageImpl<>(paginated, pageable, responses.size());
    }

    private RewardResponse calculateReward(Long customerId,
                                           List<Transaction> transactions) {

        Map<Month, Integer> monthly = new HashMap<>();
        int total = 0;

        for (Transaction tx : transactions) {

            Month month = tx.getTransactionDate().getMonth();
            int points = RewardCalculator.calculatePoints(tx.getAmount());

            monthly.merge(month, points, Integer::sum);
            total += points;
        }

        List<MonthlyReward> monthlyRewards =
                monthly.entrySet()
                        .stream()
                        .map(e -> new MonthlyReward(
                                e.getKey().toString(),
                                e.getValue()))
                        .toList();

        return new RewardResponse(customerId, monthlyRewards, total);
    }
}

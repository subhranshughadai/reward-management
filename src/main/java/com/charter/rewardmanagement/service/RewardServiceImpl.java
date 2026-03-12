package com.charter.rewardmanagement.service;

import com.charter.rewardmanagement.dto.MonthlyReward;
import com.charter.rewardmanagement.dto.RewardResponse;
import com.charter.rewardmanagement.entity.Transaction;
import com.charter.rewardmanagement.repository.TransactionRepository;
import com.charter.rewardmanagement.util.RewardCalculator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of RewardService for calculating customer rewards.
 */
@Service
public class RewardServiceImpl implements RewardService {

    private final TransactionRepository repository;

    /**
     * Constructor for RewardServiceImpl.
     *
     * @param repository transaction repository
     */
    public RewardServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves paginated reward details for customers within a date range.
     *
     * @param start start date
     * @param end end date
     * @param page page number
     * @param size page size
     * @return paginated reward response
     */
    @Override
    @Cacheable("rewards")
    public Page<RewardResponse> getAllCustomerRewards(
            LocalDate start,
            LocalDate end,
            int page,
            int size) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        List<Transaction> transactions =
                repository.findByTransactionDateBetween(start, end);

        Map<Long, List<Transaction>> customerTransactions =
                transactions.stream()
                        .collect(Collectors.groupingBy(Transaction::getCustomerId));

        List<RewardResponse> responses = new ArrayList<>();

        for (Map.Entry<Long, List<Transaction>> entry : customerTransactions.entrySet()) {

            Long customerId = entry.getKey();

            Map<String, List<Transaction>> monthlyTransactions =
                    entry.getValue().stream()
                            .collect(Collectors.groupingBy(
                                    t -> t.getTransactionDate().getMonth().toString()));

            List<MonthlyReward> monthlyRewards = new ArrayList<>();
            int total = 0;

            for (Map.Entry<String, List<Transaction>> monthEntry : monthlyTransactions.entrySet()) {

                int points = monthEntry.getValue()
                        .stream()
                        .mapToInt(t -> RewardCalculator.calculatePoints(t.getAmount()))
                        .sum();

                total += points;

                monthlyRewards.add(new MonthlyReward(monthEntry.getKey(), points));
            }

            responses.add(new RewardResponse(customerId, monthlyRewards, total));
        }

        PageRequest pageable = PageRequest.of(page, size);

        int startIndex = Math.min(page * size, responses.size());
        int endIndex = Math.min(startIndex + size, responses.size());

        List<RewardResponse> pageContent = responses.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, responses.size());
    }
}
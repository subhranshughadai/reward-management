package com.charter.rewardmanagement.service;

import com.charter.rewardmanagement.dto.RewardResponse;
import com.charter.rewardmanagement.entity.Transaction;
import com.charter.rewardmanagement.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for RewardService.
 */
class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test reward calculation for valid transactions.
     */
    @Test
    void shouldCalculateRewardsForCustomers() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(1L);
        t2.setAmount(80.0);
        t2.setTransactionDate(LocalDate.of(2024, 1, 15));

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,3,31)))
                .thenReturn(transactions);

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    /**
     * Test when there are no transactions.
     */
    @Test
    void shouldReturnEmptyRewardsWhenNoTransactions() {

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,3,31)))
                .thenReturn(Collections.emptyList());

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    /**
     * Test invalid date range validation.
     */
    @Test
    void shouldThrowExceptionWhenStartDateAfterEndDate() {

        assertThrows(IllegalArgumentException.class, () -> {

            rewardService.getAllCustomerRewards(
                    LocalDate.of(2024,3,1),
                    LocalDate.of(2024,1,1),
                    0,
                    10
            );

        });
    }

    /**
     * Test multiple customers with different transaction amounts.
     */
    @Test
    void shouldCalculateRewardsForMultipleCustomers() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(2L);
        t2.setAmount(60.0);
        t2.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2));

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    /**
     * Test transactions spanning multiple months.
     */
    @Test
    void shouldGroupRewardsByMonth() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(1L);
        t2.setAmount(80.0);
        t2.setTransactionDate(LocalDate.of(2024, 2, 15));

        Transaction t3 = new Transaction();
        t3.setCustomerId(1L);
        t3.setAmount(150.0);
        t3.setTransactionDate(LocalDate.of(2024, 3, 20));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2, t3));

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        RewardResponse response = result.getContent().get(0);
        assertEquals(3, response.getMonthlyRewards().size());
    }

    /**
     * Test pagination with multiple pages.
     */
    @Test
    void shouldHandlePaginationCorrectly() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(100.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(2L);
        t2.setAmount(100.0);
        t2.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2));

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31),
                        0,
                        1
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
    }

    /**
     * Test transactions with amounts below reward threshold.
     */
    @Test
    void shouldReturnZeroPointsForLowAmounts() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(30.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(1L);
        t2.setAmount(40.0);
        t2.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2));

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getContent().get(0).getTotalPoints());
    }

    /**
     * Test same date for start and end.
     */
    @Test
    void shouldHandleSameDateRange() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 15)))
                .thenReturn(Collections.singletonList(t1));

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 15),
                        LocalDate.of(2024, 1, 15),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    /**
     * Test large number of transactions.
     */
    @Test
    void shouldHandleLargeNumberOfTransactions() {

        List<Transaction> transactions = Arrays.asList(
                createTransaction(1L, 100.0, LocalDate.of(2024, 1, 1)),
                createTransaction(1L, 150.0, LocalDate.of(2024, 1, 5)),
                createTransaction(2L, 200.0, LocalDate.of(2024, 1, 10)),
                createTransaction(2L, 75.0, LocalDate.of(2024, 1, 15)),
                createTransaction(3L, 300.0, LocalDate.of(2024, 2, 1))
        );

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(transactions);

        Page<RewardResponse> result =
                rewardService.getAllCustomerRewards(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31),
                        0,
                        10
                );

        assertNotNull(result);
        assertEquals(3, result.getContent().size());
    }

    private Transaction createTransaction(Long customerId, Double amount, LocalDate date) {
        Transaction t = new Transaction();
        t.setCustomerId(customerId);
        t.setAmount(amount);
        t.setTransactionDate(date);
        return t;
    }
}

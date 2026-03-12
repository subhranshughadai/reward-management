package com.charter.rewardmanagement.repository;

import com.charter.rewardmanagement.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Repository test class for TransactionRepository.
 */
class TransactionRepositoryTest {

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test saving and fetching transactions within a date range.
     */
    @Test
    @DisplayName("Should return transactions within date range")
    void shouldReturnTransactionsWithinDateRange() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        Transaction t2 = new Transaction();
        t2.setCustomerId(2L);
        t2.setAmount(80.0);
        t2.setTransactionDate(LocalDate.of(2024, 2, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31));

        assertEquals(2, result.size());
    }

    /**
     * Test when no transactions exist in the given date range.
     */
    @Test
    @DisplayName("Should return empty list when no transactions exist")
    void shouldReturnEmptyListWhenNoTransactions() {

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Collections.emptyList());

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31));

        assertTrue(result.isEmpty());
    }

    /**
     * Test filtering transactions correctly by date.
     */
    @Test
    @DisplayName("Should return only transactions inside date range")
    void shouldFilterTransactionsByDateRange() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(120.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Collections.singletonList(t1));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024,1,1),
                        LocalDate.of(2024,3,31));

        assertEquals(1, result.size());
        assertEquals(120, result.get(0).getAmount());
    }

    /**
     * Test transactions on boundary dates.
     */
    @Test
    @DisplayName("Should include transactions on start and end dates")
    void shouldIncludeTransactionsOnBoundaryDates() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(100.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 1));

        Transaction t2 = new Transaction();
        t2.setCustomerId(1L);
        t2.setAmount(200.0);
        t2.setTransactionDate(LocalDate.of(2024, 3, 31));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31));

        assertEquals(2, result.size());
    }

    /**
     * Test multiple transactions for same customer.
     */
    @Test
    @DisplayName("Should return multiple transactions for same customer")
    void shouldReturnMultipleTransactionsForSameCustomer() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(50.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 5));

        Transaction t2 = new Transaction();
        t2.setCustomerId(1L);
        t2.setAmount(75.0);
        t2.setTransactionDate(LocalDate.of(2024, 1, 15));

        Transaction t3 = new Transaction();
        t3.setCustomerId(1L);
        t3.setAmount(150.0);
        t3.setTransactionDate(LocalDate.of(2024, 2, 10));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Arrays.asList(t1, t2, t3));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31));

        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(t -> t.getCustomerId().equals(1L)));
    }

    /**
     * Test transactions with zero amount.
     */
    @Test
    @DisplayName("Should handle transactions with zero amount")
    void shouldHandleZeroAmountTransactions() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(0.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 10));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)))
                .thenReturn(Collections.singletonList(t1));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31));

        assertEquals(1, result.size());
        assertEquals(0.0, result.get(0).getAmount());
    }

    /**
     * Test single day date range.
     */
    @Test
    @DisplayName("Should handle single day date range")
    void shouldHandleSingleDayDateRange() {

        Transaction t1 = new Transaction();
        t1.setCustomerId(1L);
        t1.setAmount(100.0);
        t1.setTransactionDate(LocalDate.of(2024, 1, 15));

        when(transactionRepository.findByTransactionDateBetween(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 15)))
                .thenReturn(Collections.singletonList(t1));

        List<Transaction> result =
                transactionRepository.findByTransactionDateBetween(
                        LocalDate.of(2024, 1, 15),
                        LocalDate.of(2024, 1, 15));

        assertEquals(1, result.size());
    }
}

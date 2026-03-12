package com.charter.rewardmanagement.repository;

import com.charter.rewardmanagement.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Transaction entity.
 */
@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    /**
     * Finds transactions between given dates.
     *
     * @param start start date
     * @param end end date
     * @return list of transactions
     */
    List<Transaction> findByTransactionDateBetween(
            LocalDate start,
            LocalDate end);
}
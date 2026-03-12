package com.charter.rewardmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;

/**
 * Represents a customer transaction.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    /**
     * Default constructor.
     */
    public Transaction() {
    }

    /**
     * Constructor to create a transaction.
     *
     * @param id transaction id
     * @param customerId customer id
     * @param amount transaction amount
     * @param transactionDate date of transaction
     */
    public Transaction(Long id, Long customerId, Double amount, LocalDate transactionDate) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.transactionDate = transactionDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer id.
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the transaction date.
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}